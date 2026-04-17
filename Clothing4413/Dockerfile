FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/clothing4413-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080

#Launches application in the docker container
ENTRYPOINT ["java", "-jar", "/app.jar"]