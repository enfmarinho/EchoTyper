FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY src ./src

EXPOSE 8080

RUN mvn clean install -DskipTests
CMD ["mvn", "spring-boot:run"]
