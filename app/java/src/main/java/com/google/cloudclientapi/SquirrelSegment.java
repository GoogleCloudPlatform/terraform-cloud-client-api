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

/*
 * A "segment" is a unique combination of a specific fur, age, and location.
 */
class SquirrelSegment {

  // Variables names may not follow Java conventions because these names need
  // to match names used for the other versions/languages (e.g. Python)
  // of this Jump Start Solution and the names originally used in the raw data file (CSV).
  public int _counter = 0;
  public int Chasing = 0;
  public int Climbing = 0;
  public int Eating = 0;
  public int Foraging = 0;
  public int Running = 0;
}
