// Copyright 2024 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/*
process.js - take data in raw_data bucket, process,
             and store in processed bucket.
*/

'use strict';

import logger from './logger.js';
import fs from 'fs';
import os from 'os';
import path from 'path';

import csv from 'csv-parser';
import {Storage} from '@google-cloud/storage';

import config from './config.js';

const storage = new Storage();


/**
 * Download raw data from Cloud Storage into local file for processing
 * @return {string} Local filename containing downloaded data
 */
async function downloadRawData() {
  logger.info('downloadRawData: start downloading data');

  if (!config.RAW_DATA_BUCKET) {
    throw new Error('RAW_DATA_BUCKET required');
  }
  if (!config.PROCESSED_DATA_BUCKET) {
    throw new Error('PROCESSED_DATA_BUCKET required');
  }

  const tempDir = fs.mkdtempSync(path.join(os.tmpdir(), 'rawData'));
  const tempDataFile = path.join(tempDir, 'raw_data.csv');

  logger.info(`downloadRawData: processing from ${config.RAW_DATA_BUCKET} ` +
      `to ${config.PROCESSED_DATA_BUCKET}`);

  await storage
      .bucket(config.RAW_DATA_BUCKET)
      .file(config.RAW_DATA_FILE)
      .download({destination: tempDataFile});

  logger.info(`downloadRawData: downloaded data to ${tempDataFile}`);

  return tempDataFile;
}
/**
 * Process local file, producing aggregate data
 * @param {string} tempDataFile Local filename containing downloaded data
 * @return {object} Correlated aggregate data
 */
async function processRawData(tempDataFile) {
  logger.info('processRawData: start processing data');

  return new Promise((resolve) => {
    const aggregate = {};
    let ignoredRecords = 0;
    let countedRecords = 0;
    const results = [];

    fs.createReadStream(tempDataFile)
        .pipe(csv())
        .on('data', (data) => results.push(data))
        .on('end', () => {
          results.forEach(function(row) {
            let validRow = true;

            config.FACETS.forEach(function(facet) {
              if ((row[facet] == '') || (row[facet] == '?')) {
                ignoredRecords += 1;
                validRow = false;
              }
            });

            if (validRow) {
            // build aggregate identifier
              const rowKeyValues = [];
              config.FACETS.forEach(function(facet) {
                rowKeyValues.push(row[facet]);
              });
              const rowKey = rowKeyValues.join('/');

              // Build the base data structure on first interaction
              if (!aggregate[rowKey]) {
                aggregate[rowKey] = {'_counter': 0};
                config.SEGMENTS.forEach(function(segment) {
                  if (!aggregate[rowKey][segment]) {
                    aggregate[rowKey][segment] = 0;
                  }
                });
              }

              // Record the relevant data
              config.SEGMENTS.forEach(function(segment) {
                if (row[segment] == 'true') {
                  aggregate[rowKey][segment] += 1;
                }
              });

              // Increment counters
              aggregate[rowKey]['_counter'] += 1;
              countedRecords += 1;
            }
          });

          logger.info(`processRawData: processed ${countedRecords} ` +
          `records, removed ${ignoredRecords}`);

          resolve(aggregate);
        });
  });
}


/**
 * Write aggregate data to Cloud Storage
 * @param {object} aggregate Aggregated data
 */
function writeProcessedData(aggregate) {
  logger.info('writeProcessedData: start writing');

  let counter = 0;

  const processedBucket = storage.bucket(config.PROCESSED_DATA_BUCKET);

  const writeData = new Promise((resolve) => {
    Object.keys(aggregate).forEach(async function(rowKey) {
      const facetData = aggregate[rowKey];
      processedBucket
          .file(`${rowKey}/data.json`)
          .save(JSON.stringify(facetData));
      counter += 1;
    });

    resolve(counter);
  });

  writeData.then(function(counter) {
    logger.info(`writeProcessedData: wrote ${counter} files`);
  });
}


const main = async () => {
  logger.info(`🟢 Start process.py with: ` +
    `${config.RAW_DATA_BUCKET},${config.PROCESSED_DATA_BUCKET}`);

  const dataFile = await downloadRawData();
  processRawData(dataFile)
      .then(function(result) {
        writeProcessedData(result);
      });
};

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
