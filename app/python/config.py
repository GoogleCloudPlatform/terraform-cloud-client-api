
import os

# Where the original raw data lives
RAW_DATA_BUCKET = os.environ.get("RAW_DATA_BUCKET")
# Location of the raw data file
RAW_DATA_FILE = os.environ.get("DATA_FILE", "squirrels.csv")

# Where the processed data should go
PROCESSED_DATA_BUCKET = os.environ.get("PROCESSED_DATA_BUCKET")

# Facets to segment data by, used for building processed data structure
FACETS = ["Primary Fur Color", "Age", "Location"]

# Segmentation to display data by, used for displaying processed data
SEGMENTS = ["Running", "Chasing", "Climbing", "Eating", "Foraging"]
