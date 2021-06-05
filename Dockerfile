FROM openjdk:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/fpx-app-0.0.1-SNAPSHOT-standalone.jar /fpx-app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/fpx-app/app.jar"]
