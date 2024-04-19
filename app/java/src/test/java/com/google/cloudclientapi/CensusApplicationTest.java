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
import static org.mockito.ArgumentMatchers.anyString;
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

  @Autowired private MockMvc mockMvc;

  private final String validSquirrelSegmentJson =
      "{\"_count\":561,\"Chasing\":63,\"Climbing\":361,\"Eating\":102,\"Foraging\":95,\"Running\":114}";

  @Test
  void homePageShouldSayNoDataAvailable() throws Exception {
    try (MockedStatic<GoogleCloudStorage> mockedGcs =
        Mockito.mockStatic(GoogleCloudStorage.class)) {
      mockedGcs
          .when(() -> GoogleCloudStorage.downloadFileAsString(anyString(), anyString()))
          .thenReturn(null);
      this.mockMvc
          .perform(get("/"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().string(containsString("No data available.")));
    }
  }

  @Test
  void homePageShouldSaySquirrelsWereObserved() throws Exception {
    try (MockedStatic<GoogleCloudStorage> mockedGcs =
        Mockito.mockStatic(GoogleCloudStorage.class)) {
      mockedGcs
          .when(() -> GoogleCloudStorage.downloadFileAsString(anyString(), anyString()))
          .thenReturn(validSquirrelSegmentJson);
      this.mockMvc
          .perform(get("/"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().string(containsString("squirrels were observed")));
    }
  }
}
