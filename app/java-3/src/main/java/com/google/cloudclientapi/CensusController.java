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

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

class SquirrelSegment {
    public int _count = 10;
    public int Running = 1;
    public int Chasing = 2;
    public int Climbing = 3;
    public int Eating = 4;
    public int Foraging = 5;
}

@Controller
public class CensusController {
 
    private static final String PROCESSED_DATA_BUCKET = System.getenv().get("PROCESSED_DATA_BUCKET");
    private static final Logger logger = LoggerFactory.getLogger(CensusController.class);

    @GetMapping("/")
    public String main(
        @RequestParam(name="fur", required=false, defaultValue="Gray") String fur,
        @RequestParam(name="age", required=false, defaultValue="Adult") String age,
        @RequestParam(name="location", required=false, defaultValue="Above Ground") String location,
        Model model
    ) {
        logger.info("Request received for fur: {}, age: {}, location: {}", fur, age, location);

        // Default values (which would be rendered as "No data available.").
        model.addAttribute("squirrel_count", 0);
        model.addAttribute("data_points", Arrays.asList("0", "0", "0", "0", "0"));

        SquirrelSegment squirrelSegment = retrieveData(fur, age, location);
        if (squirrelSegment == null) {
            return "index";
        }

        model.addAttribute("squirrel_count", Integer.toString(squirrelSegment._count));
        List<String> dataPoints = Arrays.asList(
          Integer.toString(squirrelSegment.Running),
          Integer.toString(squirrelSegment.Chasing),
          Integer.toString(squirrelSegment.Climbing),
          Integer.toString(squirrelSegment.Eating),
          Integer.toString(squirrelSegment.Foraging)
        );
        model.addAttribute("data_points", dataPoints);
        return "index";
    }

    private SquirrelSegment retrieveData(String fur, String age, String location) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        String filePath =  fur + "/" + age + "/" + location + "/data.json";
        logger.info(filePath); // TODO: Remove this line.
        Blob blob = storage.get(PROCESSED_DATA_BUCKET, filePath);
        if (blob == null) {
            logger.info("Returning null"); // TODO: Remove this line.
           return null;
        }
        byte[] content = blob.getContent();
        logger.info(content.toString());
        logger.info("See above");
        return new SquirrelSegment();
    }
}
 