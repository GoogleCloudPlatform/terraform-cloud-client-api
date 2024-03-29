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
logger.js - configure functionality for Cloud Logging
*/

'use strict';

import winston from 'winston';
import {LoggingWinston} from '@google-cloud/logging-winston';

// Setup Logging
const loggingWinston = new LoggingWinston();

const logger = winston.createLogger({
  level: 'info',
  transports: [new winston.transports.Console()],
});

// Enable Cloud Logging only when deployed to Cloud Run
if (process.env.K_SERVICE) {
  logger.transports.push(loggingWinston);
}

export default logger;
