FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/driver-service-0.0.1-SNAPSHOT.jar driver-service.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "driver-service.jar"]