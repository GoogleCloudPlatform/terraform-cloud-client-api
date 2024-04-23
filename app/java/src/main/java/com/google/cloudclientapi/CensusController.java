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
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CensusController {

  private static final String PROCESSED_DATA_BUCKET = System.getenv().get("PROCESSED_DATA_BUCKET");
  private static final Logger logger = LoggerFactory.getLogger(CensusController.class);

  @GetMapping("/")
  public String main(
      @RequestParam(name = "fur", required = false, defaultValue = "Gray") String fur,
      @RequestParam(name = "age", required = false, defaultValue = "Adult") String age,
      @RequestParam(name = "location", required = false, defaultValue = "Above Ground")
          String location,
      Model model) {
    logger.info("Request received for fur: {}, age: {}, location: {}", fur, age, location);

    // Default values (which would be rendered as "No data available.").
    model.addAttribute("squirrel_count", 0);
    model.addAttribute("data_points", Arrays.asList("0", "0", "0", "0", "0"));

    SquirrelSegment squirrelSegment = retrieveData(fur, age, location);
    if (squirrelSegment == null) {
      return "index";
    }

    model.addAttribute("squirrel_count", squirrelSegment._counter);
    List<Integer> dataPoints =
        Arrays.asList(
            squirrelSegment.Chasing,
            squirrelSegment.Climbing,
            squirrelSegment.Eating,
            squirrelSegment.Foraging,
            squirrelSegment.Running);
    model.addAttribute("data_points", dataPoints);
    return "index";
  }

  /**
   * @return A SquirrelSegment object representing the contents of the JSON file. Returns null if
   *     we're unable to find the file inside the Cloud Storage bucket.
   */
  public SquirrelSegment retrieveData(String fur, String age, String location) {
    String filePath = fur + "/" + age + "/" + location + "/data.json";
    String jsonAsString = GoogleCloudStorage.downloadFileAsString(PROCESSED_DATA_BUCKET, filePath);
    if (jsonAsString == null) {
      return null;
    }
    Gson gson = new Gson();
    SquirrelSegment squirrelSegment = gson.fromJson(jsonAsString, SquirrelSegment.class);
    logger.info("Retrieved data for {} entities.", squirrelSegment._counter);
    return squirrelSegment;
  }
}
