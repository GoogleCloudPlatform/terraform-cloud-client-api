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
main.py - the server-side
"""

import json

from config import PROCESSED_DATA_BUCKET, logging

from flask import Flask, render_template, request

import google.cloud.storage


app = Flask(__name__)


def retrieve_data(fur, age, location):
    filename = "/".join([fur, age, location]) + "/data.json"

    storage_client = google.cloud.storage.Client()
    processed_bucket = storage_client.get_bucket(PROCESSED_DATA_BUCKET)
    blob = processed_bucket.blob(filename)

    if not blob.exists():
        logging.warning(
            f"{PROCESSED_DATA_BUCKET} does not contain {filename}. "
            "Has the job been run?"
        )
        return 0, []

    fragment = blob.download_as_string()
    data = json.loads(fragment)

    squirrel_count = data.pop("_counter")

    logging.info(f"Retrieved data for {squirrel_count} entities.")

    # Ensure data values are returned ordered by key
    data_points = list(dict(sorted(data.items())).values())
    return squirrel_count, data_points


@app.route("/")
def home():
    if PROCESSED_DATA_BUCKET is None:
        return "Environment variable PROCESSED_DATA_BUCKET required", 500

    fur = request.args.get("fur", "Gray")
    age = request.args.get("age", "Adult")
    location = request.args.get("location", "Above Ground")

    logging.info(
        f"Request received for fur: {fur}, age: {age}, location: {location}"
    )

    squirrel_count, data_points = retrieve_data(
        fur=fur, age=age, location=location
    )

    # for validation testing, make array have no whitespace
    data_points = str(data_points).replace(" ", "")

    return render_template(
        "index.html", squirrel_count=squirrel_count, data_points=data_points
    )


if __name__ == "__main__":
    app.run(host="localhost", port=8080, debug=True)
