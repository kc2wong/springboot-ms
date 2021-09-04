# reference-data-service
Maintenance of master reference data such as Currency

- Spring data JPA to perform CRUD functions
- Spring data JPA auditor annotation
- Flyway migration to create database object and initial data
- Publish domain event by inserting a record to table __evt_currency__ in the same transaction with changes in entities.  This gets rid of the need to have XA between database commit and message publishing
- Debezium Kafka Connector will pick up the inserted record from table __evt_currency__, convert to DomainEvent object and publish to Kafka

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

