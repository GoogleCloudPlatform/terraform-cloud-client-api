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

const {RAW_DATA_BUCKET, PROCESSED_DATA_BUCKET} = process.env;

const main = async () => {
  logger.info(`🟢 Start process.py with: ` +
    `${RAW_DATA_BUCKET},${PROCESSED_DATA_BUCKET}`);

  // TODO: real processing
  logger.info('TODO: IMPLEMENTATION');
};

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
