# --- STAGE 1: Builder ---
# Uses a JDK/Maven image to compile and package the application
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the build file (pom.xml) first to leverage Docker's caching
COPY pom.xml .
# Download dependencies for caching (speeds up subsequent builds)
RUN mvn dependency:go-offline

# Copy all source code
COPY src ./src

# Build the application, creating the final JAR file
RUN mvn package -DskipTests

# --- STAGE 2: Runner ---
# Uses a lightweight JRE image to run the final JAR
FROM openjdk:21-ea-28-jdk
WORKDIR /app

# Copy the built JAR from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar","app.jar"]