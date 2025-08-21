FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .

RUN mvn -B dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests



FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ARG PROFILE=dev
ARG JAR_FILE=/target/*.jar


COPY --from=builder /build/${JAR_FILE} /app/app.jar
COPY --from=builder /build/target/classes/logback-spring.xml ./config/logback-spring.xml

COPY .env.${PROFILE} .

RUN mkdir -p /logs

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=${PROFILE}

ENTRYPOINT ["java", "-Dlogging.config=./config/logback-spring.xml", "-jar", "/app/app.jar"]
CMD ["--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]