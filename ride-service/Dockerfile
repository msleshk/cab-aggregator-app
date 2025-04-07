FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/ride-service-0.0.1-SNAPSHOT.jar ride-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "ride-service.jar"]