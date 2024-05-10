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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CensusController.class)
class CensusApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  private final String VALID_SQUIRREL_SEGMENT_JSON = "{\"_counter\":561,\"Chasing\":63,\"Climbing\":361,\"Eating\":102,\"Foraging\":95,\"Running\":114}";

  @Test
  public void homePageShouldSayNoDataAvailable() throws Exception {
    // Mock environment variable(s)
    try (MockedStatic<EnvironmentVars> mockedEnvVars = Mockito.mockStatic(EnvironmentVars.class)) {
      mockedEnvVars
          .when(() -> EnvironmentVars.get("PROCESSED_DATA_BUCKET"))
          .thenReturn("Placeholder");

      // Mock GoogleCloudStorage
      try (MockedStatic<GoogleCloudStorage> mockedGcs = Mockito.mockStatic(GoogleCloudStorage.class)) {
        mockedGcs
            .when(() -> GoogleCloudStorage.downloadFileAsString(any(), any()))
            .thenReturn(null);

        expectAResponseContaining("No data available.");
      }
    }
  }

  @Test
  public void homePageShouldSaySquirrelsWereObserved() throws Exception {
    // Mock environment variable(s)
    try (MockedStatic<EnvironmentVars> mockedEnvVars = Mockito.mockStatic(EnvironmentVars.class)) {
      mockedEnvVars
          .when(() -> EnvironmentVars.get("PROCESSED_DATA_BUCKET"))
          .thenReturn("Placeholder");

      // Mock GoogleCloudStorage
      try (MockedStatic<GoogleCloudStorage> mockedGcs = Mockito.mockStatic(GoogleCloudStorage.class)) {
        mockedGcs
            .when(() -> GoogleCloudStorage.downloadFileAsString(any(), any()))
            .thenReturn(VALID_SQUIRREL_SEGMENT_JSON);

        this.expectAResponseContaining("squirrels were observed");
      }
    }
  }

  private void expectAResponseContaining(String substring) throws Exception {
    this.mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(substring)));
  }
}
