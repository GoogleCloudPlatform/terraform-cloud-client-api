# Java Deployment

This example illustrates how to use the `cloud-client-api` module, specifying the Java implementation

<!-- BEGINNING OF PRE-COMMIT-TERRAFORM DOCS HOOK -->
## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project\_id | The Google Cloud project ID where resources will be deployed. | `string` | n/a | yes |
| region | The Google Cloud region where resources will be deployed. | `string` | n/a | yes |

## Outputs

| Name | Description |
|------|-------------|
| job\_name | Name of the deployed Cloud Run Job |
| service\_url | URL of the deployed Cloud Run service |

<!-- END OF PRE-COMMIT-TERRAFORM DOCS HOOK -->
To provision this example, run the following from within this directory:
- `terraform init` to get the plugins
- `terraform plan` to see the infrastructure plan
- `terraform apply` to apply the infrastructure build
- `terraform destroy` to destroy the built infrastructure
