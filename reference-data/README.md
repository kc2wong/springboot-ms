# reference-data-service
Maintenance of master reference data such as Currency

- Spring data JPA to perform CRUD functions
- Spring data JPA auditor annotation
- Flyway migration to create database object and initial data
- Publish domain event to Kafka after data is persisted to database

To build and install rest client to maven local repository, run
```
cd rest-client
gradlew clean build publishToMavenLocal
```
Java but not Kotlin is used because Sleuth supports restTemplate seamlessly

To start spring boot application, run
```
cd spring-boot
gradlew clean bootRun
```

To test the spring-boot application, run
```
curl http://localhost:9000/reference-data-service/v1/currencies/HKD
```

