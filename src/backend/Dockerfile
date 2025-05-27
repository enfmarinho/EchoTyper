FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY .env ./
COPY --from=builder /app/target/*.jar app.jar

# Create a non-root user and switch to it
RUN adduser --system --group spring
USER spring:spring

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
