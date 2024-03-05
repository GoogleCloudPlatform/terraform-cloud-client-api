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
app.js - application
*/

'use strict';

import express from 'express';
import logger from './logger.js'
import { engine } from 'express-handlebars';

const app = express();

app.engine('handlebars', engine());
app.set('view engine', 'handlebars');
app.set('views', './views');

app.get('/', async (req, res) => {
    logger.info("Request received (TODO: data)")

    // TODO: get real values from dataset.
    var squirrel_count = 5
    var data_points = [1, 2, 3, 4, 5]

    res.render("index", {
        layout: false,
        squirrel_count: squirrel_count, data_points: data_points
    })
});

export default app;
