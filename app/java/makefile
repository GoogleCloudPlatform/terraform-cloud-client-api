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

# If you're on Windows, use mvnw.cmd instead of ./mvnw.

# Install dependencies
install:
	./mvnw dependency:resolve

# Build the web app and processing job
build:
	./mvnw package

# Start Spring Boot development server (web app)
dev:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=locallogging

# Run unit tests
test:
	./mvnw test -Dspring-boot.run.profiles=locallogging

# Show outstanding linting issues
lint:
	./mvnw com.spotify.fmt:fmt-maven-plugin:check

# Fix some outstanding linting issues
format:
	./mvnw com.spotify.fmt:fmt-maven-plugin:format
