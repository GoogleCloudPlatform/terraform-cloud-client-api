"""
process.py - take data in raw_data bucket, process, and store in processed bucket.
"""
import json
import csv

from config import *

if RAW_DATA_BUCKET is None: 
    raise ValueError("RAW_DATA_BUCKET required")

if PROCESSED_DATA_BUCKET is None: 
    raise ValueError("PROCESSED_DATA_BUCKET required")

from google.cloud import storage

temp_datafile = "test.csv"

storage_client = storage.Client()

raw_bucket = storage_client.get_bucket(RAW_DATA_BUCKET)
processed_bucket = storage_client.get_bucket(PROCESSED_DATA_BUCKET)

raw_bucket.blob(RAW_DATA_FILE).download_to_filename(temp_datafile)

aggregate_data = {}

counter = {"recorded": 0, "removed": 0, "written": 0}

with open(temp_datafile) as f:
    csv_data = csv.DictReader(f)
    data = [row for row in csv_data]

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
            if row[FACETS[0]] not in aggregate_data.keys():
                aggregate_data[row[FACETS[0]]] = {}
            if row[FACETS[1]] not in aggregate_data[row[FACETS[0]]].keys():
                aggregate_data[row[FACETS[0]]][row[FACETS[1]]] = {}
            if row[FACETS[2]] not in aggregate_data[row[FACETS[0]]][row[FACETS[1]]].keys():
                aggregate_data[row[FACETS[0]]][row[FACETS[1]]][row[FACETS[2]]] = {}

            data_loc = aggregate_data[row[FACETS[0]]][row[FACETS[1]]][row[FACETS[2]]] 

            for segment in SEGMENTS:
                if segment not in data_loc.keys(): 
                    data_loc[segment] = 0
                if row[segment] == "true": 
                    data_loc[segment] += 1
            
        
            counter["recorded"] += 1
        else:
            counter["removed"] += 1


for facet_a in aggregate_data.keys(): 
    for facet_b in aggregate_data[facet_a].keys(): 
        for facet_c in aggregate_data[facet_a][facet_b].keys(): 

            facet_data = aggregate_data[facet_a][facet_b][facet_c]

            data_file = f"{facet_a}/{facet_b}/{facet_c}/data.json"
            processed_bucket.blob(data_file).upload_from_string(json.dumps(facet_data))
            counter["written"] += 1

print("Records processed: " + str(counter))