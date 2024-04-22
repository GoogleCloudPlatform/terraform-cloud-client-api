// Copyright 2024 Google LLC
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

/*
config.js - environment variables and default values,
            shared with the job and service.
*/

const config = {
  // Facets to segment data by, used for building processed data structure
  FACETS: ['Primary Fur Color', 'Age', 'Location'],

  // Segmentation to display data by, used for displaying processed data
  SEGMENTS: ['Running', 'Chasing', 'Climbing', 'Eating', 'Foraging'],

};

export default config;
