FROM openjdk:24-bullseye

# Copy jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

COPY .env.prod .

# Correct ENTRYPOINT with separated arguments
ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "--sun-misc-unsafe-memory-access=allow","-jar", "/app.jar", "--spring.profiles.active=prod"]
