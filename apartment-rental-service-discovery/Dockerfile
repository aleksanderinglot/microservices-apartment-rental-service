FROM maven:3.8.3-openjdk-17 as build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /build/target/apartment-rental-service-discovery-0.0.1-SNAPSHOT.jar ./app.jar
COPY --from=build /build/src/main/resources/application.properties ./application.properties
EXPOSE 8761
CMD ["java", "-jar", "app.jar"]
