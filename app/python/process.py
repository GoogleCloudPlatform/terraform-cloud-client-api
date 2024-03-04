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
import os

import google.cloud.logging
import google.cloud.storage

from config import *

# Enable Cloud Logging only when deployed to Cloud Run
if os.environ.get("K_SERVICE"):
    logging_client = google.cloud.storage.Client()
    logging_client.setup_logging()


def download_raw_data():
    """
    Download raw data from Cloud Storage into local file for processing
    """
    logging.info("  download_raw_data: start downloading data")

    if RAW_DATA_BUCKET is None:
        raise ValueError("RAW_DATA_BUCKET required")

    if PROCESSED_DATA_BUCKET is None:
        raise ValueError("PROCESSED_DATA_BUCKET required")

    temp_datafile = "test.csv"
    logging.info(
        f"  download_raw_data: processing from {RAW_DATA_BUCKET} to {PROCESSED_DATA_BUCKET}"
    )

    storage_client = google.cloud.storage.Client()
    raw_bucket = storage_client.get_bucket(RAW_DATA_BUCKET)

    raw_bucket.blob(RAW_DATA_FILE).download_to_filename(temp_datafile)

    logging.info("  download_raw_data: downloaded data to {temp_datafile}")
    return temp_datafile


def process_raw_data(temp_datafile):
    """
    Process local file, producing aggregate data
    """
    logging.info("  process_raw_data: start processing data")

    aggregate = {}

    count_recorded, count_removed = 0, 0

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

            count_recorded += 1
        else:
            # Note count of records not recorded (due to data cleanup)
            count_removed += 1

    logging.info(
        f"  process_raw_data: processed {count_recorded} records, removed {count_removed}."
    )
    return aggregate


def write_processed_data(aggregate):
    """
    Write aggregate data to Cloud Storage
    """
    logging.info("  write_processed_data: start writing data.")
    counter = 0

    storage_client = google.cloud.storage.Client()
    processed_bucket = storage_client.get_bucket(PROCESSED_DATA_BUCKET)

    for facet_a in aggregate.keys():
        for facet_b in aggregate[facet_a].keys():
            for facet_c in aggregate[facet_a][facet_b].keys():

                facet_data = aggregate[facet_a][facet_b][facet_c]

                data_file = f"{facet_a}/{facet_b}/{facet_c}/data.json"
                processed_bucket.blob(data_file).upload_from_string(
                    json.dumps(facet_data)
                )
                counter += 1

    logging.info(f"  write_processed_data: wrote {counter} files.")


if __name__ == "__main__":

    logging.info(
        f"🟢 Start process.py with: {RAW_DATA_BUCKET}, {PROCESSED_DATA_BUCKET}."
    )

    datafile = download_raw_data()
    result = process_raw_data(datafile)
    write_processed_data(result)

    logging.info(f"🏁 process.py complete.")
