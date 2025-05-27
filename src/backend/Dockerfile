FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml pom.xml
COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre
COPY .env .env
COPY --from=builder /app/target/*.jar app.jar

# Fix to permission bugs
RUN adduser --system --group spring
USER spring:spring

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
