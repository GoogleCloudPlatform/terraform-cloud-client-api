#
# Copyright 2024 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#	  http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


# virtual environment's python executable.
PYTHON=venv/bin/python3

# Setup virtual environment
venv: 
	python3 -m pip install --upgrade pip
	python3 -m pip install virtualenv
	virtualenv venv

# Install dependencies
install: 
	$(PYTHON) -m pip install -r requirements.txt -r tests/requirements.txt

# Start development server
dev: venv
	$(PYTHON) main.py

# Run unit tests
test: venv
	$(PYTHON) -m pytest

# Show outstanding linting issues
lint: venv
	$(PYTHON) -m flake8 *.py tests/*.py

# Fix outstanding linting issues
format: venv
	$(PYTHON) -m black . -l 79
