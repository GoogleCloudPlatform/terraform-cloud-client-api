package com.google.cloudclientapi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

class SquirrelCensusDictionaryBuildResult {
  public Dictionary<String, SquirrelSegment> dictionary;
  public int numOfRowsProcessed;
  public int numOfRowsIgnored;
}

public class SquirrelCensusDictionaryBuilder {

  private static String[] CSV_COLUMNS = {"Primary Fur Color", "Age", "Location"};

  public static SquirrelCensusDictionaryBuildResult buildFromRawCsvFile(String csvFilePath)
      throws FileNotFoundException, IOException {
    Dictionary<String, SquirrelSegment> colorAgeLocationToCount =
        new Hashtable<String, SquirrelSegment>();
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
        String key =
            String.format(
                "%s/%s/%s",
                record.get("Primary Fur Color"), record.get("Age"), record.get("Location"));
        SquirrelSegment currSegment = colorAgeLocationToCount.get(key);
        if (currSegment == null) {
          currSegment = new SquirrelSegment();
          colorAgeLocationToCount.put(key, currSegment);
        }
        bumpSquirrelSegmentCountsForCsvRow(currSegment, record);
        currSegment._count++;
      }
    }

    SquirrelCensusDictionaryBuildResult result = new SquirrelCensusDictionaryBuildResult();
    result.dictionary = colorAgeLocationToCount;
    result.numOfRowsProcessed = numOfRowsProcessed;
    result.numOfRowsIgnored = numOfRowsIgnored;
    return result;
  }

  private static void bumpSquirrelSegmentCountsForCsvRow(
      SquirrelSegment squirrelSegment, CSVRecord row) {
    squirrelSegment._count++;
    if (row.get("Chasing").equals("true")) {
      squirrelSegment.Chasing++;
    }
    if (row.get("Climbing").equals("true")) {
      squirrelSegment.Climbing++;
    }
    if (row.get("Eating").equals("true")) {
      squirrelSegment.Eating++;
    }
    if (row.get("Foraging").equals("true")) {
      squirrelSegment.Foraging++;
    }
    if (row.get("Running").equals("true")) {
      squirrelSegment.Running++;
    }
  }
}
