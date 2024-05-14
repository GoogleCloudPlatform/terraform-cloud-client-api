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

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("locallogging") // Code being unit tested should log locally — not to Cloud Logging
class SquirrelCensusDictionaryBuilderTest {

  @Test
  public void testBuildFromRawCsvFile() throws IOException {
    String csvFilePath = "../../infra/data/squirrels.csv";
    SquirrelCensusDictionaryBuildResult result =
        SquirrelCensusDictionaryBuilder.buildFromRawCsvFile(csvFilePath);
    SquirrelSegment segment = result.dictionary.get("Gray/Adult/Above Ground");
    Assertions.assertThat(segment._counter).isEqualTo(561);
    Assertions.assertThat(segment.Running).isEqualTo(114);
    Assertions.assertThat(segment.Chasing).isEqualTo(63);
  }
}
