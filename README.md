# springboot-ms
A simple application that demonstrates microservice development with spring boot

##### Design pattern
- Backend For Frontend (BFF) - A stateless application that acts as a single entry point for frontend application such as web application.  Upon receiving a request from consumer, it then calls various restful api(s), transforms and aggregates the result.  BFF is not reusable in nature and is specifically for a particular page or use case
- Interservice communication - Downstream services communicates with upstream services using restful api.  Upstream services communicates with downstream services using pub-sub in order to avoid circular dependency
- Onion architecture - Protocol (rest / jms) Layer -> Service Layer -> Data Layer


##### Technologies
- Springboot data jpa for data persistence.  Disable OSIV and control transaction explicitly, use of entity graph to reduce number of SQLs to execute
- Spring web for restful api development
- Debezium to publish domain events to Kafka
- Spring Kafka to subscribe domain events
- Spring cache Redis to cache data
- Spring cloud Sleuth for tracing
- Adopt contract first approach with openAPI generator to generate stub code and rest client from openapi specification
- Flyway migration to create database objects and initial data
- Mapstruct for data transformation between different layers

##### Architecture
![system-architecture](./docs/architecture.jpg)

##### Quickstart
1. Setup the following infrastructure services (https://github.com/kc2wong/docker-for-dev)
   1. MySQL
   2. Redis
   4. Apache ZooKeeper and Kafka
   5. Debezium Kafka Connector for MySQL
2. Compile and deploy common library (https://github.com/kc2wong/springboot-ms/tree/main/library)
3. Create database for reference-data-service
   1. Login to MySQL docker container
      1. ```docker exec -it mysql bash```
      2. ```mysql -u root -p```
   2. Create database
      1. ```create database its_refdata;```
      2. ```grant all privileges on * . * to 'dbadmin'@'%';```
4. Compile and deploy reference-data-service (https://github.com/kc2wong/springboot-ms/tree/main/reference-data)
   1. Compile and deploy rest-client 
   2. Start spring-boot service
5. Compile and deploy market-data-service (https://github.com/kc2wong/springboot-ms/tree/main/market-data)
   1. Compile and deploy rest-client 
   2. Start spring-boot service
6. Create Debezium Kafka connector for MySQL
   1. ```curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-mysql.json```
7. Compile and start web-api (https://github.com/kc2wong/springboot-ms/tree/main/web-api)

