FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/passenger-service-0.0.1-SNAPSHOT.jar passenger-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "passenger-service.jar"]