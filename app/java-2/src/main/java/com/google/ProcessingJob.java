package com.google;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class ProcessingJob
{
    private static String RAW_DATA_BUCKET = System.getenv().get("RAW_DATA_BUCKET");
    private static String PROCESSED_DATA_BUCKET = System.getenv().get("PROCESSED_DATA_BUCKET");
    private static String RAW_DATA_FILE = "squirrels.csv";
    private static String[] CSV_COLUMNS = {"Primary Fur Color", "Age", "Location"};

    public static void main( String[] args )
    {
        System.out.println( 
            String.format(
            "🟢 Start ProcessingJob with: %s, %s", RAW_DATA_BUCKET, PROCESSED_DATA_BUCKET)
        );
        throwErrorIfInvalidArgs();
        try {
            String tempDataFilePath = downloadRawData();
            Dictionary<String, Integer> colorAgeLocationToCount = processRawData(tempDataFilePath);
            writeProcessedData(colorAgeLocationToCount);
        } catch (Exception e) {
            System.out.println("ProcessingJob failed: " + e.getMessage());
        }
    }

    private static void throwErrorIfInvalidArgs() {
        if (RAW_DATA_BUCKET == null || RAW_DATA_BUCKET.isEmpty()) {
            throw new IllegalArgumentException("RAW_DATA_BUCKET required");
        }
        if (PROCESSED_DATA_BUCKET == null || PROCESSED_DATA_BUCKET.isEmpty()) {
            throw new IllegalArgumentException("PROCESSED_DATA_BUCKET required");
        }
    }

    public static String downloadRawData() throws IOException {
        System.out.println("downloadRawData: start downloading data");

        // Create temporary folder and file
        Path tempDir = Files.createTempDirectory("rawData");
        File tempDataFile = new File(tempDir.toFile(), "raw_data.csv");
        System.out.println(
            "downloadRawData: processing from " + RAW_DATA_BUCKET +
                " to " + PROCESSED_DATA_BUCKET);

        // Read from Google Cloud Storage bucket
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.get(RAW_DATA_BUCKET, RAW_DATA_FILE);
        blob.downloadTo(tempDataFile.toPath());
        System.out.println("downloadRawData: downloaded data to " + tempDataFile.getAbsolutePath());
        return tempDataFile.getAbsolutePath();
    }

    public static Dictionary<String, Integer> processRawData(String tempDataFilePath) 
        throws FileNotFoundException, IOException {
        System.out.println("processRawData: start processing data");
        Dictionary<String, Integer> colorAgeLocationToCount = new Hashtable<String, Integer>();
        int numOfProcessedRows = 0;
        int numOfIgnoredRows = 0;
        // Go through each row of the CSV file.
        FileReader reader = new FileReader(tempDataFilePath);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
        for (CSVRecord record : csvParser) {
            numOfProcessedRows++;
            // Ignore rows where crucial columns are empty/unknown.
            boolean shouldIgnoreRow = false;
            for (String column : CSV_COLUMNS) {
                String value = record.get(column);
                if (value == null || value.isEmpty() || value.equals("?")) {
                    numOfIgnoredRows++;        
                    shouldIgnoreRow = true;
                }
            }

            // Bump count in "Color/Age/Location" dictionary.
            if (!shouldIgnoreRow) {
                String key = String.format(
                    "%s/%s/%s",
                    record.get("Primary Fur Color"),
                    record.get("Age"),
                    record.get("Location")
                );
                Integer currCount = colorAgeLocationToCount.get(key);
                if (currCount == null) {
                    currCount = 0;
                }
                colorAgeLocationToCount.put(key, currCount + 1);
            }
        }
        System.out.println(String.format(
            "processRawData: processed %s records, removed %s",
            numOfProcessedRows, 
            numOfIgnoredRows));
        return colorAgeLocationToCount;
    }

    /**
     * Uploads a JSON file (to a Google Cloud Storage bucket)
     * for each combination of facets (for each color-age-location combination).
     */
    public static void writeProcessedData(Dictionary<String, Integer> colorAgeLocationToCount) 
        throws UnsupportedEncodingException {
        System.out.println("writeProcessedData: start writing");
        Gson gson = new Gson();
        Enumeration<String> keys = colorAgeLocationToCount.keys();
        Storage storage = StorageOptions.getDefaultInstance().getService();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String jsonString = gson.toJson(colorAgeLocationToCount.get(key));
            BlobId cloudStorageBlobId = BlobId.of(PROCESSED_DATA_BUCKET, key + "/data.json");
            BlobInfo cloudStorageBlobInfo = BlobInfo
                .newBuilder(cloudStorageBlobId)
                .setContentType("application/json")
                .build();
            String utf8CharsetName = StandardCharsets.UTF_8.name();
            byte[] jsonStringAsByes = jsonString.getBytes(utf8CharsetName);
            storage.create(cloudStorageBlobInfo, jsonStringAsByes, 0, jsonStringAsByes.length);
        }
        System.out.println("writeProcessedData: wrote " + colorAgeLocationToCount.size() + " files");
    }
}
