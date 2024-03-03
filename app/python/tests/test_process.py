#!/usr/bin/python
#
# Copyright 2024 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from process import process_raw_data

## Use real data validation
DATAFILE_NAME = "../../data/squirrels.csv"
DATAFILE_LEN = 3024


def test_raw_data_validate():
    """Run checks on test data, to ensure processed data is correct"""

    num_lines = sum(1 for _ in open(DATAFILE_NAME))
    assert num_lines == DATAFILE_LEN


def test_process_raw_data():
    results = process_raw_data(DATAFILE_NAME)

    # Check known data results
    assert "Gray" in results.keys()
    assert "Cinnamon" in results.keys()
    assert "Black" in results.keys()

    # Validate a specific aggregate result
    old_tree_sq = results["Gray"]["Adult"]["Above Ground"]
    assert old_tree_sq["_counter"] == 561
    assert old_tree_sq["Running"] == 114
