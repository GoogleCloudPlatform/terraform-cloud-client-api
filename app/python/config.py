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
config.py - environment variables and default values,
            shared with the job and service.
"""

import os
import sys
import tomllib


import google.cloud.logging


# Where the original raw data lives
RAW_DATA_BUCKET = os.environ.get("RAW_DATA_BUCKET")
# Location of the raw data file
RAW_DATA_FILE = os.environ.get("DATA_FILE", "squirrels.csv")

# Where the processed data should go
PROCESSED_DATA_BUCKET = os.environ.get("PROCESSED_DATA_BUCKET")

# Facets to segment data by, used for building processed data structure
FACETS = ["Primary Fur Color", "Age", "Location"]

# Segmentation to display data by, used for displaying processed data
SEGMENTS = ["Chasing", "Climbing", "Eating", "Foraging", "Running"]


# Setup integrated logging https://cloud.google.com/logging/docs/setup/python
# But only when authentication available.
try:
    logging_client = google.cloud.logging.Client()
    logging_client.setup_logging()
    import logging  # noqa: E402
except google.auth.exceptions.GoogleAuthError:
    import logging  # noqa: E402

    logging.basicConfig(stream=sys.stdout, level=logging.INFO)


# Get version from pyproject.toml (not a package, just for convenience)
with open("pyproject.toml", "rb") as f:
    config = tomllib.load(f)
    version = config["project"]["version"]
