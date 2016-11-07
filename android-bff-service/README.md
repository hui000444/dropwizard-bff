# Bff

How to start the Bff application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/android-bff-service-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

## How to launch hello-world
---

1. Run `mvn package` to build your application
2. Start application with `java -jar target/android-bff-service-1.0-SNAPSHOT.jar server hello-world.yml`
3. To check that your application is running enter url `http://localhost:8080/api/hello-world?name=hi`

You should get the the response: `{"id":1,"content":"Hello, hi!"}`

## How to launch Swagger UI

1. Start application with `java -jar target/android-bff-service-1.0-SNAPSHOT.jar server hello-world.yml`
2. Enter url `http://localhost:8080/swagger/index.html`
