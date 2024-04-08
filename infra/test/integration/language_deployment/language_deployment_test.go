// Copyright 2023 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package language_deployment

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"strings"
	"testing"
	"time"

	"github.com/GoogleCloudPlatform/cloud-foundation-toolkit/infra/blueprint-test/pkg/gcloud"
	"github.com/GoogleCloudPlatform/cloud-foundation-toolkit/infra/blueprint-test/pkg/tft"
	"github.com/GoogleCloudPlatform/cloud-foundation-toolkit/infra/blueprint-test/pkg/utils"
	"github.com/stretchr/testify/assert"
)

func TestLanguageDeployment(t *testing.T) {
	region := regionFromEnv()

	cft := tft.NewTFBlueprintTest(t, tft.WithVars(map[string]interface{}{
		"region":   region,
		"language": "python", // TODO: generalize
	}))

	cft.DefineVerify(func(assert *assert.Assertions) {
		cft.DefaultVerify(assert)

		projectID := cft.GetTFSetupStringOutput("project_id")
		serviceURL := cft.GetStringOutput("service_url")
		jobName := cft.GetStringOutput("job_name")

		// Save common arguments for gcloud calls.
		gcloudOps := gcloud.WithCommonArgs([]string{"--project", projectID, "--region", region})

		assert.Truef(strings.HasSuffix(serviceURL, ".run.app"), "unexpected service URL %q", serviceURL)

		// On initial deployment, website is serving, but has no data.
		assertResponseContains(t, assert, serviceURL, "/", "No data available.")

		// Run job
		gcloud.Run(t, fmt.Sprintf("run jobs execute  %s --wait", jobName), gcloudOps)

		// After job run, expect website to be serving valid data
		assertResponseContains(t, assert, serviceURL, "2018 Squirrel Census")
		// Check some known values to confirm data processing
		assertResponseContains(t, assert, serviceURL+"/?age=Adult&fur=Black&location=Ground+Plane", "count = 66")
		assertResponseContains(t, assert, serviceURL+"/?age=Juvenile&fur=Gray&location=Above+Ground", "count = 95")

		// Ensure processed files appear as they should in Cloud Storage
		// Retrieve processed bucket from service envvar
		process_job := gcloud.Run(t, fmt.Sprintf("run jobs describe %s --format json)", jobName), gcloudOps)
		process_bucket := process_job.Get("spec.template.spec.containers[0].env[PROCESS_BUCKET_NAME].value")
		bucket_objects := gcloud.Run(t,
			fmt.Sprintf("gcloud storage objects list --exhaustive gs://%s/**/data.json --format value(name)", process_bucket),
			gcloudOps).Array()
		assert.Contains(bucket_objects, "Cinnamon/Juvenile/Ground Plane/data.json")
		assert.Contains(bucket_objects, "Grey/Adult/Above Ground/data.json")

	})

	cft.DefineTeardown(func(assert *assert.Assertions) {
		cft.DefaultTeardown(assert)
	})
	cft.Test()
}

///////////////////////////////////////////////////////////////////////////////////////

// Helper for calling URLs
func assertResponseContains(t *testing.T, assert *assert.Assertions, url string, text ...string) {
	t.Helper()
	var code int
	var responseBody string
	var err error

	fn := func() (bool, error) {
		t.Logf("HTTP Request - GET %s", url)
		code, responseBody, err = httpGetRequest(url)
		retry := err != nil || code < 200 || code > 299
		switch {
		case retry && err == nil:
			t.Logf("Failed HTTP Request: Status Code %d", code)
		case retry && err != nil:
			t.Logf("Failed HTTP Request: %v", err)
		default:
			// In Verbose mode with success, the asserts below are a "silent pass" during test output.
			// Facilitates real-time evaluation during long test process.
			t.Log("Successful HTTP Request")
		}
		return retry, nil
	}
	utils.Poll(t, fn, 6, 10*time.Second)

	// Assert expectations of the last checked response.
	assert.Nil(err)
	assert.GreaterOrEqual(code, 200)
	assert.LessOrEqual(code, 299)

	for _, fragment := range text {
		assert.Containsf(responseBody, fragment, "couldn't find %q in response body", fragment)
	}
}

// HTTP GET helper
func httpGetRequest(url string) (statusCode int, body string, err error) {
	res, err := http.Get(url)
	if err != nil {
		return 0, "", err
	}
	defer res.Body.Close()

	buffer, err := io.ReadAll(res.Body)
	return res.StatusCode, string(buffer), err
}

// Region gatherer
func regionFromEnv() string {
	if r := os.Getenv("GOOGLE_CLOUD_REGION"); r != "" {
		return r
	}
	return "us-central1"
}
