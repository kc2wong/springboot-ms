# market-data-service
Perform stock market price enquiry using Yahoo Finance API

To build and install rest client to maven local repository, run
```
cd rest-client
gradlew clean openApiGenerate build publishToMavenLocal
```

To start spring boot application, run
```
cd spring-boot
gradlew clean openApiGenerate bootRun
```

To test the spring-boot application, run
```
curl http://localhost:9100/market-data-service/v1/market-price/HKG/0005
```

