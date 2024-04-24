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

package com.google.cloudclientapi;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessingJob {

  private static String RAW_DATA_BUCKET = System.getenv().get("RAW_DATA_BUCKET");
  private static String PROCESSED_DATA_BUCKET = System.getenv().get("PROCESSED_DATA_BUCKET");
  private static String RAW_DATA_FILE = "squirrels.csv";
  private static final Logger logger = LoggerFactory.getLogger(CensusController.class);

  public static void main(String[] args) {
    logger.info(
        String.format(
            "🟢 Start ProcessingJob with: %s, %s", RAW_DATA_BUCKET, PROCESSED_DATA_BUCKET));
    throwErrorIfInvalidArgs();
    try {
      String tempDataFilePath = downloadRawData();
      Dictionary<String, SquirrelSegment> colorAgeLocationToCount =
          processRawData(tempDataFilePath);
      writeProcessedData(colorAgeLocationToCount);
    } catch (Exception e) {
      logger.error("ProcessingJob failed: " + e.getMessage());
    }
  }

  private static void throwErrorIfInvalidArgs() {
    if (RAW_DATA_BUCKET == null || RAW_DATA_BUCKET.isEmpty()) {
      throw new IllegalArgumentException("RAW_DATA_BUCKET required");
    }
    if (PROCESSED_DATA_BUCKET == null || PROCESSED_DATA_BUCKET.isEmpty()) {
      throw new IllegalArgumentException("PROCESSED_DATA_BUCKET required");
    }
  }

  public static String downloadRawData() throws IOException {
    logger.info("downloadRawData: start downloading data");

    // Create temporary folder and file
    Path tempDir = Files.createTempDirectory("rawData");
    File tempDataFile = new File(tempDir.toFile(), "raw_data.csv");
    logger.info(
        "downloadRawData: processing from " + RAW_DATA_BUCKET);

    // Read from Google Cloud Storage bucket
    GoogleCloudStorage.downloadFileToFilePath(
        RAW_DATA_BUCKET, RAW_DATA_FILE, tempDataFile.toPath());
    logger.info("downloadRawData: downloaded data to " + tempDataFile.getAbsolutePath());
    return tempDataFile.getAbsolutePath();
  }

  public static Dictionary<String, SquirrelSegment> processRawData(String tempDataFilePath)
      throws FileNotFoundException, IOException {
    logger.info("processRawData: start processing data");
    SquirrelCensusDictionaryBuildResult result =
        SquirrelCensusDictionaryBuilder.buildFromRawCsvFile(tempDataFilePath);
    logger.info(
        String.format(
            "processRawData: processed %s records, removed %s",
            result.numOfRowsProcessed, result.numOfRowsIgnored));
    return result.dictionary;
  }

  /**
   * Uploads a JSON file (to a Google Cloud Storage bucket) for each combination of facets (for each
   * color-age-location combination).
   */
  public static void writeProcessedData(Dictionary<String, SquirrelSegment> colorAgeLocationToCount)
      throws UnsupportedEncodingException {
    logger.info("writeProcessedData: start writing");
    Gson gson = new Gson();
    Enumeration<String> keys = colorAgeLocationToCount.keys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String jsonString = gson.toJson(colorAgeLocationToCount.get(key));
      GoogleCloudStorage.upload(PROCESSED_DATA_BUCKET, key + "/data.json", jsonString);
    }
    logger.info("writeProcessedData: wrote " + colorAgeLocationToCount.size() + " files");
  }
}
