# Application

This solution implements the same functionality in multiple different programming languages:

 * Python: [Flask](https://flask.palletsprojects.com/) and [Python Cloud Client Libraries](https://cloud.google.com/python/docs/reference)
 * Node: [Express](https://expressjs.com/) and [Node.js Cloud Client Libraries](https://cloud.google.com/nodejs/docs/reference)
 * Java: [Spring Boot](https://spring.io/projects/spring-boot) and  [Java Cloud Client Libraries](https://cloud.google.com/java/docs/reference).

Each implementation has two main features:

 * A processing script that takes initial raw data (.csv file) and produces aggregate information, and
 * A web page that uses the aggregate information to serve a [Chart.js radar](https://www.chartjs.org/docs/latest/charts/radar.html) graph.

The index page for each implementation is served by a [generated template](./templates/README.md).

## Local development

Use [Google Cloud Shell](https://cloud.google.com/shell), or a local development installation of the programming language of choice (see [this configuration](../.github/workflows/unit-test.yaml) for language versions currently in use).

Each language includes a `makefile` file that includes common development steps:

 * `make install`: install the package dependencies
 * `make dev`: run a local development web server
 * `make process`: run the processing job locally
 * `make test`: run unit tests
 * `make lint`: run linting checks
 * `make format`: apply linting fixes

## Deployment

Each implementation contains a custom [Procfile](https://devcenter.heroku.com/articles/procfile) that defines multiple entrypoints:

 * `web` (default): a production web server
 * `process`: a script to run the aggregate processing

This image built using [Google Cloud's buildpacks](https://cloud.google.com/docs/buildpacks/overview) in [Cloud Build configurations](../build/images.cloudbuild.yaml), and deployed as both a Cloud Run service and a Cloud Run job using [Terraform](../infra/).

## Language specifics

* For Node.js, direct commands are encoded into the `package.json`, and referenced by both `makefile` and `Procfile`.
* For Java, we leverage two different [Spring Boot Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html) — `default` and `locallogging`. The `default` Profile pushes logs to Cloud Logging while `locallogging` simply pushes logs to standard output. The `locallogging` Profile is used for local development and unit tests while `default` is used for production and integration tests.
