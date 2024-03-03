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
		"region": region,
	}))

	cft.DefineVerify(func(assert *assert.Assertions) {
		cft.DefaultVerify(assert)

		projectID := cft.GetTFSetupStringOutput("project_id")
		serviceURL := cft.GetTFSetupStringOutput("service_url")
		jobName := cft.GetTFSetupStringOutput("job_name")

		assert.Truef(strings.HasSuffix(serviceURL, ".run.app"), "unexpected service URL %q", serviceURL)

		// On initial deployment, not expected to have any data.
		assertResponseContains(t, assert, serviceURL, "/", "No data available.")

		// Run job
		gcloud.Runf(t, "run jobs execute  %s --project %s --region %s  --wait", jobName, projectID, region)

		// After job run, expected to have data.
		assertResponseContains(t, assert, serviceURL, "/", "2018 Squirrel Census")
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
	utils.Poll(t, fn, 36, 10*time.Second)

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
