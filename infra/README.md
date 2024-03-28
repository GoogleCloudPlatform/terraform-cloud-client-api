
% terraform-cloud-client-api

# Tagline

Interact with Google Cloud using the Cloud SDK Client Libraries to transform and query information.

## Detailed

This solution deploys a processing job, rendering service, and initial unprocessed data, which is then manipulated by the processing job to be viewed by the rendering service.

## Predeploy

To deploy this blueprint you must have an active billing account and billing permissions.

## Documentation

TODO

## Usage

Basic usage of this module is as follows:


```hcl
module "cloud-client-api" {
  source = ".."

  project_id = var.project_id
  region     = "us-central1"
  language   = "python"
}
```


Functional examples are included in the [examples](./examples/) directory.

<!-- BEGINNING OF PRE-COMMIT-TERRAFORM DOCS HOOK -->
## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| deployment\_name | Identifier for deployment, included in resource names. | `string` | `"client-api"` | no |
| image\_version | Version of application image to use | `string` | `"0.3.0"` | no |
| labels | A set of key/value label pairs to assign to the resources deployed by this solution. | `map(string)` | `{}` | no |
| language | Programming language implementation to use (nodejs, java, python) | `string` | n/a | yes |
| project\_id | The Google Cloud project ID where resources will be deployed. | `string` | n/a | yes |
| region | The Google Cloud region where resources will be deployed. | `string` | `"us-central1"` | no |

## Outputs

| Name | Description |
|------|-------------|
| job\_name | Name of the deployed Cloud Run Job |
| project\_id | Google Cloud project ID |
| service\_url | URL of the deployed Cloud Run service |

<!-- END OF PRE-COMMIT-TERRAFORM DOCS HOOK -->
