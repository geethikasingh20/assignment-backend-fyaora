# Use official OpenJDK image
#FROM openjdk:17-jdk-slim
#FROM openjdk:17.0.2-jdk-slim-buster
FROM eclipse-temurin:17-jdk-jammy AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and grant execute permissions
COPY mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw

# Copy and prepare dependencies
COPY pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copy source and build
COPY src ./src
COPY src/main/resources/static/ ./src/main/resources/static/
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["java", "-jar", "target/app.jar"]