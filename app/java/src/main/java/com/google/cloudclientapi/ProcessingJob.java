/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//
// ProcessingJob.java - take data in raw_data bucket,
//                      process, and store in processed bucket.
//

package com.google.cloudclientapi;

abstract class ProcessingJob {
  private static String RAW_DATA_BUCKET =
      System.getenv().get("RAW_DATA_BUCKET");
  private static String PROCESSED_DATA_BUCKET =
      System.getenv().get("PROCESSED_DATA_BUCKET");

  public static void main(String[] args) {
    System.out.println(
        String.format(
            "🟢 Start process.py with: %s, %s", RAW_DATA_BUCKET, PROCESSED_DATA_BUCKET));

    // TODO: real processing.
    System.out.println("TODO: IMPLEMENTATION");
  }
}
