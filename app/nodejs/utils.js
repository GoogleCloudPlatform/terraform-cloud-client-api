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
utils.js - utility functions
*/

'use strict';

import logger from './logger.js';
import { Storage } from '@google-cloud/storage';

const storage = new Storage();

/**
 * Retrieve data from Cloud Storage, based on query strings
 * @param {string} fur Fur type (Gray, Black, Cinnamon)
 * @param {string} age Age (Adult, Juvenile)
 * @param {string} location Location (Above Ground, Ground Level)
 */
async function retrieveData(fur, age, location) {
    const dataFilename = [fur, age, location].join('/') + '/data.json';
    var dataFile = ""

    try {
      dataFile = await storage
        .bucket(process.env.PROCESSED_DATA_BUCKET)
        .file(dataFilename)
        .download();

    } catch (err) {
      if (err.code == 404) {
        logger.log('warn', `${process.env.PROCESSED_DATA_BUCKET} does not contain ${dataFilename}.` +
          ` Has the job been run?`);
      } else {
        logger.log('error', err)
      }
      // Template knows to handle empty data as data not available.
      return [0, []];
    }

    const data = JSON.parse(dataFile);
    const squirrelCount = data._counter;
    delete data._counter;

    logger.info(`Retrieved data for ${squirrelCount} entities.`);

    // Get aggregate counter values, sorted by their respective key
    const dataValues = [];
    Object.keys(data).sort().forEach(function (key) {
      dataValues.push(data[key]);
    });

    return [squirrelCount, dataValues];
  }

export default { retrieveData };
