FROM openjdk:21-jdk

# Copy jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

COPY target/classes/logback-spring.xml /config/logback-spring.xml

COPY .env.prod .

RUN mkdir -p /logs

# Correct ENTRYPOINT with separated arguments
ENTRYPOINT ["java",  "-Dlogging.config=/config/logback-spring.xml", "-jar", "/app.jar", "--spring.profiles.active=prod"]
