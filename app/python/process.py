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


"""
process.py - take data in raw_data bucket, process, and store in processed bucket.
"""

import csv
import json
import logging

import google.cloud.logging
import google.cloud.storage

from config import *

storage_client = google.cloud.storage.Client()
logging_client = google.cloud.storage.Client()
logging_client.setup_logging()


if RAW_DATA_BUCKET is None:
    raise ValueError("RAW_DATA_BUCKET required")

if PROCESSED_DATA_BUCKET is None:
    raise ValueError("PROCESSED_DATA_BUCKET required")


temp_datafile = "test.csv"

logging.info(f"Processing data from {RAW_DATA_BUCKET} to {PROCESSED_DATA_BUCKET}")

raw_bucket = storage_client.get_bucket(RAW_DATA_BUCKET)
processed_bucket = storage_client.get_bucket(PROCESSED_DATA_BUCKET)

raw_bucket.blob(RAW_DATA_FILE).download_to_filename(temp_datafile)

aggregate = {}

counter = {"recorded": 0, "removed": 0, "written": 0}


with open(temp_datafile) as f:
    csv_data = csv.DictReader(f)
    data = [row for row in csv_data]

    logging.info(f"Processing {len(data)} records from {RAW_DATA_FILE}")

    # Process each row.
    for row in data:
        process = True
        # Ignore any records with incomplete data
        for facet in FACETS:
            if row[facet] == "":
                process = False
            if row[facet] == "?":
                process = False

        if process:

            # pre-create data (could be better way?)
            if row[FACETS[0]] not in aggregate.keys():
                aggregate[row[FACETS[0]]] = {}
            if row[FACETS[1]] not in aggregate[row[FACETS[0]]].keys():
                aggregate[row[FACETS[0]]][row[FACETS[1]]] = {}
            if row[FACETS[2]] not in aggregate[row[FACETS[0]]][row[FACETS[1]]].keys():
                aggregate[row[FACETS[0]]][row[FACETS[1]]][row[FACETS[2]]] = {}

            data_loc = aggregate[row[FACETS[0]]][row[FACETS[1]]][row[FACETS[2]]]

            if "_counter" not in data_loc.keys():
                data_loc["_counter"] = 0

            data_loc["_counter"] += 1

            for segment in SEGMENTS:
                if segment not in data_loc.keys():
                    data_loc[segment] = 0
                if row[segment] == "true":
                    data_loc[segment] += 1

            counter["recorded"] += 1
        else:
            counter["removed"] += 1


for facet_a in aggregate.keys():
    for facet_b in aggregate[facet_a].keys():
        for facet_c in aggregate[facet_a][facet_b].keys():

            facet_data = aggregate[facet_a][facet_b][facet_c]

            data_file = f"{facet_a}/{facet_b}/{facet_c}/data.json"
            processed_bucket.blob(data_file).upload_from_string(json.dumps(facet_data))
            counter["written"] += 1

logging.info("Record processing complete.")
logging.info(counter)
