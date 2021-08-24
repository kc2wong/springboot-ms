# its-web-api
Provide restful api for frontend web application.  Upon receiving the request, it calls different backend API, aggregates the result and constructs the response

To increase performance, Spring cache is used to cache currency object got from market-data-service.  Cached currency object will be evicted upon receiving the domain event published by market-data-service 

To start spring boot application, run
```
gradlew clean openApiGenerate bootRun
```

To test the spring-boot application, run
```
curl http://localhost:8000/its-web-api/market-price/HKG/0005
```

