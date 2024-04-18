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

# Install dependencies
install:
	mvn dependency:resolve

# Build the web app and processing job
build:
	mvn package

# Start Spring Boot development server (web app)
dev:
	mvn spring-boot:run

# Run unit tests
test:
	mvn test

# Show outstanding linting issues
lint:
	mvn com.spotify.fmt:fmt-maven-plugin:check

# Fix some outstanding linting issues
format:
	mvn com.spotify.fmt:fmt-maven-plugin:format
