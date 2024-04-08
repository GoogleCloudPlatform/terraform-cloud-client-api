package com.google;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

class SquirrelCensusDictionaryBuildResult {
    public Dictionary<String, SquirrelSegment> dictionary;
    public int numOfRowsProcessed;
    public int numOfRowsIgnored;
}

class SquirrelSegment {
    
    // Variables names may not follow Java conventions because these names need
    // to match names used for the Python and JavaScript version of this code.
    public int _count = 0;
    public int Running = 0;
    public int Chasing = 0;
    public int Climbing = 0;
    public int Eating = 0;
    public int Foraging = 0;

    public void bumpCountsBasedOnCsvRow(CSVRecord row) {
        this._count++;
        if (row.get("Running").equals("true")) {
            this.Running++;
        }
        if (row.get("Chasing").equals("true")) {
            this.Chasing++;
        }
        if (row.get("Climbing").equals("true")) {
            this.Climbing++;
        }
        if (row.get("Eating").equals("true")) {
            this.Eating++;
        }
        if (row.get("Foraging").equals("true")) {
            this.Foraging++;
        }
    }

}

public class SquirrelCensusDictionaryBuilder {

    private static String[] CSV_COLUMNS = {"Primary Fur Color", "Age", "Location"};

    public static SquirrelCensusDictionaryBuildResult buildFromRawCsvFile(String csvFilePath) 
        throws FileNotFoundException, IOException {
        Dictionary<String, SquirrelSegment> 
            colorAgeLocationToCount = new Hashtable<String, SquirrelSegment>();
        int numOfRowsProcessed = 0;
        int numOfRowsIgnored = 0;
        
        // Go through each row of the CSV file.
        FileReader reader = new FileReader(csvFilePath);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
        for (CSVRecord record : csvParser) {
            numOfRowsProcessed++;
            // Ignore rows where crucial columns are empty/unknown.
            boolean shouldIgnoreRow = false;
            for (String column : CSV_COLUMNS) {
                String value = record.get(column);
                if (value == null || value.isEmpty() || value.equals("?")) {
                    numOfRowsIgnored++;        
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
                SquirrelSegment currSegment = colorAgeLocationToCount.get(key);
                if (currSegment == null) {
                    currSegment = new SquirrelSegment();
                    colorAgeLocationToCount.put(key, currSegment);
                }
                currSegment.bumpCountsBasedOnCsvRow(record);
                currSegment._count++;
            }
        }

        SquirrelCensusDictionaryBuildResult result = new SquirrelCensusDictionaryBuildResult();
        result.dictionary = colorAgeLocationToCount;
        result.numOfRowsProcessed = numOfRowsProcessed;
        result.numOfRowsIgnored = numOfRowsIgnored;
        return result;
    }

}
