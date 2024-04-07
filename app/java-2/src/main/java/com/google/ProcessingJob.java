package com.google;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        try {
            downloadRawData();
        } catch (Exception e) {
            System.out.println("ProcessingJob failed: " + e.getMessage());
        }
    }

    public static String downloadRawData() throws IOException 
    {
        System.out.println("downloadRawData: start downloading data");
        if (RAW_DATA_BUCKET == null || RAW_DATA_BUCKET.isEmpty()) {
            throw new IllegalArgumentException("RAW_DATA_BUCKET required");
        }
        if (PROCESSED_DATA_BUCKET == null || PROCESSED_DATA_BUCKET.isEmpty()) {
            throw new IllegalArgumentException("PROCESSED_DATA_BUCKET required");
        }

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

    public static void processRawData() {
        // TODO: Implement
    }

    public static void writeProcessedData() {
        // TODO: Implement
    }
}
