FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/rating-service-0.0.1-SNAPSHOT.jar rating-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "rating-service.jar"]