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

'use strict';

import app from '../app.js';
import utils from '../utils.js';
import assert from 'assert';
import supertest from 'supertest'
import sinon from 'sinon';


describe('No configuration set', () => {
  let request;
  
  before(async () => {
    process.env.RAW_DATA_BUCKET = '';
    process.env.PROCESSED_DATA_BUCKET = '';

    request = supertest(app);
  });

  it('should return error on GET /', async () => {
    const response = await request.get("/")
    assert.equal(response.statusCode, 500)
    assert.equal(response.text, 'Environment variable PROCESSED_DATA_BUCKET required');
  });
});

describe("Application with mock data", () => {
  let stubUtils;
  let request;

  before(async() => {
    request = supertest(app);
    stubUtils = sinon.stub(utils, 'retrieveData').returns([20, [2, 2, 3, 5, 8]]);
  })

  after(async() => {
    stubUtils.restore();
  });

  it('should display valid data', (done) => {
    process.env.PROCESSED_DATA_BUCKET = "faux-bucket-123";
    
    request.get('/')
      .then(response => {
        assert.equal(response.status, 200);
        assert.contains(response.text, /var count = 20/);
      })
      .finally(error => {
        return error ? done(err) : done();
      });
  });
})
