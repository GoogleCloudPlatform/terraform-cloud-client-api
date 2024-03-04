#!/usr/bin/python
#
# Copyright 2024 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


import pytest

from main import app as flask_app


@pytest.fixture()
def client():
    return flask_app.test_client()


def test_not_configured(client):
    response = client.get("/")
    assert response.status_code == 500
    assert b"PROCESSED_DATA_BUCKET required" in response.data


# TODO: add additional unit tests here, for example:
#   * mocking data from retrieve_data
