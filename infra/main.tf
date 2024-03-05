/**
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

locals {
  unique = "${random_id.default.hex}-${var.deployment_name}"

  # TODO(glasnt): temporary
  application_image = "us-docker.pkg.dev/glasnt-squirrel-10019/containers/cloud-client-api/${var.language}:latest"
}

resource "random_id" "default" {
  byte_length = 4
}


#######################################################################################
# APIs

module "project_services" {
  source                      = "terraform-google-modules/project-factory/google//modules/project_services"
  version                     = "~> 14.0"
  disable_services_on_destroy = false
  project_id                  = var.project_id

  activate_apis = [
    "run.googleapis.com",
  ]
}

#######################################################################################
# Cloud Storage

resource "google_storage_bucket" "processed_data" {
  name          = "processed-${local.unique}-${var.project_id}"
  location      = var.region
  storage_class = "REGIONAL"

  labels        = var.labels
  force_destroy = true
}

resource "google_storage_bucket" "raw_data" {
  name          = "raw_data-${local.unique}-${var.project_id}"
  location      = var.region
  storage_class = "REGIONAL"

  labels        = var.labels
  force_destroy = true
}

resource "google_storage_bucket_object" "default" {
  bucket = google_storage_bucket.raw_data.name
  name   = "squirrels.csv"
  source = "${path.module}/data/squirrels.csv"
}

#######################################################################################
# Cloud Run Service

resource "google_cloud_run_v2_service" "default" {
  name     = "chart-${local.unique}"
  location = var.region

  labels = var.labels

  template {
    service_account = google_service_account.reader.email
    containers {
      image = local.application_image

      env {
        name  = "PROCESSED_DATA_BUCKET"
        value = google_storage_bucket.processed_data.name
      }
    }
  }

  depends_on = [ module.project_services ]
}

resource "google_cloud_run_v2_service_iam_member" "public" {
  location = google_cloud_run_v2_service.default.location
  name     = google_cloud_run_v2_service.default.name
  role     = "roles/run.invoker"
  member   = "allUsers"
}

#######################################################################################
# Cloud Run Job

resource "google_cloud_run_v2_job" "default" {
  name     = "process-${local.unique}"
  location = var.region

  labels = var.labels

  template {
    template {
      service_account = google_service_account.writer.email
      containers {
        image   = local.application_image
        command = ["process"]

        env {
          name  = "RAW_DATA_BUCKET"
          value = google_storage_bucket.raw_data.name
        }
        env {
          name  = "PROCESSED_DATA_BUCKET"
          value = google_storage_bucket.processed_data.name
        }
      }
    }
  }

  depends_on = [ module.project_services ]
}

data "google_project" "project" {
  depends_on = [ module.project_services ]
}

#######################################################################################
# Service account - Data Writer 

resource "google_service_account" "writer" {
  account_id   = "writer-${local.unique}"
  display_name = "Account with read/write access to data."
}

// Client APIs need to get the bucket to then get the object. 
resource "google_project_iam_custom_role" "object_downloader" {
  role_id     = "objectDownloader"
  title       = "Cloud Storage Object Downloader"
  description = "Permissions to download an object from a Cloud Storage bucket"
  permissions = ["storage.objects.get", "storage.objects.list", "storage.buckets.get"]
}

resource "google_storage_bucket_iam_member" "writer_raw_data" {
  bucket = google_storage_bucket.raw_data.name
  role   = google_project_iam_custom_role.object_downloader.id
  member = "serviceAccount:${google_service_account.writer.email}"
}

resource "google_storage_bucket_iam_member" "writer_processed_data" {
  bucket = google_storage_bucket.processed_data.name
  role   = "roles/storage.admin"
  member = "serviceAccount:${google_service_account.writer.email}"
}

resource "google_project_iam_member" "writer_logging" {
  project = var.project_id
  role    = "roles/logging.logWriter"
  member  = "serviceAccount:${google_service_account.writer.email}"
}

#######################################################################################
# Service account - Read-only

resource "google_service_account" "reader" {
  account_id   = "read-only-${local.unique}"
  display_name = "Account with read-only access to data."
}

resource "google_storage_bucket_iam_member" "reader_processed_data" {
  bucket = google_storage_bucket.processed_data.name
  role   = google_project_iam_custom_role.object_downloader.id
  member = "serviceAccount:${google_service_account.reader.email}"
}

resource "google_project_iam_member" "reader_logging" {
  project = var.project_id
  role    = "roles/logging.logWriter"
  member  = "serviceAccount:${google_service_account.reader.email}"
}
