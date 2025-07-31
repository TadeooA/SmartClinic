FROM openjdk:17-jdk-slim

LABEL maintainer="Smart Clinic Management System"

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/clinical_system-0.0.1-SNAPSHOT.jar"]