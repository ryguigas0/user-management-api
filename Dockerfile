FROM openjdk:23-slim

# installing curl for healthcheck
RUN apt -y update && apt install curl -y

WORKDIR /

COPY pom.xml /pom.xml
COPY mvnw /mvnw
COPY .mvn /.mvn
COPY src /src
COPY target /target

RUN ./mvnw install package

ENTRYPOINT [ "java", "-jar", "/target/user_management-0.0.1-SNAPSHOT.jar" ]