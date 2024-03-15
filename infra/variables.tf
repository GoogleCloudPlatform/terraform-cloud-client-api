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


variable "image_version" {
  type        = string
  description = "Version of application image to use"
  default     = "0.3.0" // x-release-please-version
}

variable "project_id" {
  type        = string
  description = "The Google Cloud project ID where resources will be deployed."
}

variable "region" {
  type        = string
  description = "The Google Cloud region where resources will be deployed."
  default     = "us-central1"
}

variable "deployment_name" {
  type        = string
  description = "Identifier for deployment, included in resource names."
  default     = "client-api"
}

variable "labels" {
  type        = map(string)
  description = "A set of key/value label pairs to assign to the resources deployed by this solution."
  default     = {}
}

variable "language" {
  type        = string
  description = "Programming language implementation to use (nodejs, java, python)"

  validation {
    condition     = contains(["nodejs", "java", "python"], var.language)
    error_message = "Valid names for language: nodejs, java, python."
  }
}
