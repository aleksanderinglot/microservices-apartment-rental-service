FROM openjdk:17-jdk
WORKDIR /app
COPY target/notification-0.0.1-SNAPSHOT.jar .
COPY src/main/resources/application.properties .
EXPOSE 8070
CMD ["java", "-jar", "notification-0.0.1-SNAPSHOT.jar"]