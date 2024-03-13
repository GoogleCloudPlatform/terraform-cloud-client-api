/**
 * Copyright 2023 Google LLC
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

variable "project_id" {
  description = "The Google Cloud project ID where resources will be deployed."
}

variable "region" {
  description = "The Google Cloud region where resources will be deployed."
}

variable "language" {
  description = "Programming language implementation to use (nodejs, java, python)"
}

variable "image_version" {
  description = "Version of application image to use."
}
