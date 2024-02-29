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

from flask import Flask, render_template, request
import json
from google.cloud import storage

from config import *

app = Flask(__name__)
storage_client = storage.Client()


if PROCESSED_DATA_BUCKET is None: 
    raise ValueError("PROCESSED_DATA_BUCKET required")

processed_bucket = storage_client.get_bucket(PROCESSED_DATA_BUCKET)

def retrieve_data(fur, age, location):
    prefix = "/".join([fur, age, location])

    squirrel_count = 0
    data_points = {}
    for segment in SEGMENTS: 
        data_points[segment] = 0

    blobs = storage_client.list_blobs(PROCESSED_DATA_BUCKET, prefix=prefix)

    for file in blobs: 
        fragment = processed_bucket.blob(file.name).download_as_string()
        data = json.loads(fragment)
        squirrel_count += data["_counter"]
        for segment in SEGMENTS: 
            data_points[segment] += data[segment]

    return squirrel_count, list(data_points.values())


@app.route("/")
def home():
    fur = request.args.get("fur", "*")
    age = request.args.get("age", "*")
    location = request.args.get("location", "*")

    squirrel_count, data_points = retrieve_data(fur=fur, age=age, location=location)

    # required data: squirrel_count, data_points, (selectors)
    content = {}
    return render_template("index.html", squirrel_count=squirrel_count, data_points=data_points)


if __name__ == "__main__":
    app.run(host="localhost", port=8080, debug=True)
