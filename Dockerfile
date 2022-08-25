FROM openjdk:8-jre-alpine

WORKDIR /var/david-apis

COPY target/batigen-0.0.1-SNAPSHOT.jar /var/david-apis/app.jar
COPY config.yml /var/david-apis/config.yml

EXPOSE 12345 19000

ENTRYPOINT ["java", "-jar", "app.jar", "server", "config.yml"]