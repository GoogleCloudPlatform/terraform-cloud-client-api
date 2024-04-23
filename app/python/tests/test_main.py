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


from bs4 import BeautifulSoup as bs

from main import app as flask_app

import pytest


@pytest.fixture()
def client():
    return flask_app.test_client()


def test_not_configured(client):
    response = client.get("/")
    assert response.status_code == 500
    assert b"PROCESSED_DATA_BUCKET required" in response.data


def test_mock_data(client, mocker):
    squirrel_count = 20
    data_points = [2, 2, 3, 5, 8]

    mocker.patch(
        "main.retrieve_data", return_value=(squirrel_count, data_points)
    )
    mocker.patch("main.PROCESSED_DATA_BUCKET", return_value="notarealbucket")

    response = client.get("/?fur=Gray")
    assert response.status_code == 200

    soup = bs(response.data, "html.parser")
    facets = soup.find("div", {"id": "facets"})
    facets_text = " ".join(facets.text.split())
    assert str(squirrel_count) in facets_text
    assert "2018 Squirrel Census" in facets_text

    # Can't check the canvas, so check the javascript
    response_html = response.data.decode("UTF-8")
    assert f"var count = 20" in response_html
    assert f"var points = [2,2,3,5,8]" in response_html
