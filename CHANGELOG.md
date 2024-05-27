# Changelog

## [0.6.2](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.6.1...v0.6.2) (2024-05-27)


### Bug Fixes

* **deps:** update dependency org.springframework.boot:spring-boot-starter-parent to v3.3.0 ([#111](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/111)) ([156551e](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/156551e52aca7d5fd5f283394651da00bc422c13))

## [0.6.1](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.6.0...v0.6.1) (2024-05-23)


### Bug Fixes

* **deps:** update dependency com.google.cloud:google-cloud-storage to v2.39.0 ([#109](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/109)) ([01803db](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/01803dbaf92d980d6d868f3a167cffd666e813bd))

## [0.6.0](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.5.0...v0.6.0) (2024-05-23)


### Features

* Support Cloud Logging in Java via Logback ([#88](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/88)) ([4ca933f](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/4ca933f95c4ba8dcab942ab21f74e73ce18c4052))


### Bug Fixes

* Add 500 response for missing env var in Java ([#86](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/86)) ([f3bdc97](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/f3bdc97349f80b5b49bbfac5cf2500e6a41782ac))
* add make process to assist with local development ([#92](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/92)) ([a1897d1](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/a1897d1005ae78da22503a81c3ffe43eeeb3125a))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.6 ([#101](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/101)) ([616e9fb](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/616e9fb4fdd08eabdf2d3a074ae7573d67a08164))
* **deps:** update dependency ch.qos.logback:logback-core to v1.5.6 ([#100](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/100)) ([7f0712f](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/7f0712f24a0ae19f0bf6231135e069f06330ba3c))
* **deps:** update dependency com.google.cloud:google-cloud-storage to v2.38.0 ([#104](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/104)) ([d31b281](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/d31b281839600c0dba5def631ec40eb29faa7f04))
* **deps:** update dependency com.google.code.gson:gson to v2.11.0 ([#106](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/106)) ([8ee9b47](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/8ee9b47213a22763ae87df1503dfe8ab48663f44))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.13 ([#94](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/94)) ([07fd463](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/07fd463d379f49c935695daad34e98ed65e675f8))
* Fix wording and typos in comment ([#103](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/103)) ([e1f827a](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/e1f827ac4d2ce154088f2e4ceaad540410b9b556))
* **python:** use download_as_text instead of as_string ([#105](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/105)) ([29fb97b](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/29fb97b2173780bce08830499081180285ece150))
* Rename main in CensusController to index ([#93](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/93)) ([709eca3](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/709eca371589790a1a6eaa0c2c92f9f96323ee0e))
* revert pinned PR version of image ([#91](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/91)) ([5fa3dc4](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/5fa3dc46c0892c4e25d6c3c5ea5094ff1848399e))

## [0.5.0](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.4.1...v0.5.0) (2024-05-07)


### Features

* nodejs application ([#30](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/30)) ([7463f0f](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/7463f0f01607bc79489e82be1fcfe61ace703836))


### Bug Fixes

* add ignore for maven-wrapper ([#79](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/79)) ([4b9633a](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/4b9633af285cb40b0805db54671082c94c3e9fb1))
* **deps:** update dependency org.apache.commons:commons-csv to v1.11.0 ([#82](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/82)) ([7e7a1af](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/7e7a1af98100aee4949738ae9a5ad1cfdcea1461))

## [0.4.1](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.4.0...v0.4.1) (2024-05-01)


### Bug Fixes

* add link to postdeploy neos ([#76](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/76)) ([d5db4ef](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/d5db4ef18d1e834986c1c59a478eb41d487bcf36))
* **deps:** update dependency com.google.cloud:google-cloud-storage to v2.37.0 ([#71](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/71)) ([c69320c](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/c69320cfd738ff9f52b82acd9ec6262d66031251))

## [0.4.0](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.3.2...v0.4.0) (2024-04-26)


### Features

* Implement Java web app & processing job ([#32](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/32)) ([8dea674](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/8dea674f93472794dbdb39bc8cc53dbe460c7b3a))


### Bug Fixes

* **deps:** update dependency org.apache.commons:commons-csv to v1.10.0 ([#70](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/70)) ([e3021ea](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/e3021ea5e4bdcbf607500e38ee3946a111fd6cc4))
* **deps:** update dependency org.springframework.boot:spring-boot-starter-parent to v3.2.5 ([#62](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/62)) ([784dd00](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/784dd00027dca50911be0d48032dfef3e0642cd9))
* extend validation of setup ([#35](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/35)) ([c5fa7ba](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/c5fa7ba95738018be4fb943ddc93185202b945f6))

## [0.3.2](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.3.1...v0.3.2) (2024-04-18)


### Bug Fixes

* Ensure all segments are sorted alphabetically by key ([#39](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/39)) ([8bdc14d](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/8bdc14daa59c0bec8119d7036760819096aa4484))
* ensure custom role is unique ([#34](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/34)) ([89f7ea3](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/89f7ea37d8421ba4d32f597a5e77230f5851d095))
* Use alphabetical graph label order ([#36](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/36)) ([a5afc8b](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/a5afc8bb60d63fc12c7402e53763d935fd168829))

## [0.3.1](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.3.0...v0.3.1) (2024-03-28)


### Bug Fixes

* automatically update infra/README.md version on release ([#29](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/29)) ([e01e269](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/e01e269cc01696d63c50eb884f6e2506b60809ad))
* metadata, resource length limits ([#25](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/25)) ([bec3cc8](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/bec3cc8760aa611ade4f856585d43ac58a9a8a84))
* remove Makefile conflict by renaming app makefiles ([#27](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/27)) ([59ec37e](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/59ec37e7b541987e7bf65cff795ba3a67b762c2d))

## [0.3.0](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.2.0...v0.3.0) (2024-03-15)


### Features

* show python version in processing job ([#20](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/20)) ([317e7cc](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/317e7cca48b58d2bab9d557b34f27416be4d5bde))


### Bug Fixes

* correct release-please config ([#21](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/21)) ([1c26f27](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/1c26f275b0981692fe793a939bdd3bac9a1fc5a3))

## [0.2.0](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.1.1...v0.2.0) (2024-03-15)


### Features

* release-please config ([#15](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/15)) ([481d8e1](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/481d8e19cb4f9cce24ed5047a3698cabd671927d))

## [0.1.1](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/compare/v0.1.0...v0.1.1) (2024-03-13)


### Bug Fixes

* correct context-specific logging ([#12](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/12)) ([71f45f6](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/71f45f61791393de378826550745f1671b408458))

## 0.1.0 (2024-03-08)


### Features

* add cloud logging to cloud run job ([fb66005](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/fb66005d6edbd34324aa2346ca4e136e826237aa))
* add java job structure ([#6](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/6)) ([c801e34](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/c801e34364adcee41098cfbd2dcfad3f9b5f938a))
* add nodejs job structure ([#7](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/7)) ([86b7b69](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/86b7b69171c9c78ff10439cf1b46f8318ec6076c))
* basic functionality ([01101a4](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/01101a4e9bd129683d376464f296122ea2f24ad0))
* infrastructure testing ([#3](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/3)) ([07041b0](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/07041b07fb958492341d1199e0801dcd1f2c013b))
* initial processing functionality ([c314364](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/c314364258e0bf517766b188ec571c5f636ea7f3))
* initial template generation ([bf15bb3](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/bf15bb34101c46c4c25372bc9b4acc85e3f9f8b3))
* terraform ([036d6ba](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/036d6bab25eae96dff8476c5aa3698181fcac3ed))


### Bug Fixes

* add custom Procfile ([a5cf71f](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/a5cf71f456c82ae4d171561969f02113f7b740a3))
* add helper for terraform ([63eea3f](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/63eea3fd36247275e23b4a324c41d960250a87c9))
* add infra/Makefile ([#4](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/4)) ([b4ff54e](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/b4ff54ed71535066984f92edbbd5c40bee66e270))
* correct nodejs logging ([8adba31](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/8adba3151ddab75513618f2f8efd03a551cb75f3))
* correct syntax ([f4e4b87](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/f4e4b8739723631be1c02028f59dd0fc2d361b45))
* simplify python logic ([#5](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/issues/5)) ([56cdbbf](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/56cdbbfe8f28519c3baf705ee900e0eca4740c2e))
* update Python application ([35382b3](https://github.com/GoogleCloudPlatform/terraform-cloud-client-api/commit/35382b3a33e89d7e6e3c32974a7724e0aa40aa39))

## Changelog
