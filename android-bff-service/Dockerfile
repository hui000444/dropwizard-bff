FROM openjdk:8
FROM maven:3-jdk-8

ADD ./target /app/target
ADD ./hello-world.yml /app

#RUN mvn package

CMD java -jar /app/target/android-bff-service-1.0-SNAPSHOT.jar server /app/hello-world.yml

EXPOSE 8080