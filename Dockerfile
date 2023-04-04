# Build
FROM maven:3.8.1-openjdk-17-slim as build

RUN mkdir /project

WORKDIR /project

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src/ ./src/

RUN mvn clean compile assembly:single

# Run
FROM openjdk:17-jdk-alpine3.14

RUN mkdir /app

COPY --from=build /project/target/sidstar-project-1.0-SNAPSHOT.jar /app/java-application.jar

WORKDIR /app

CMD "java" "-jar" "java-application.jar"
