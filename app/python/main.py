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
