# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-jammy
#WORKDIR /app

# coping maven to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# downloading dependencies
#RUN ./mvnw dependency:resolve

# coping source code
COPY src ./src

# running application
CMD ["./mvnw", "spring-boot:run"]
