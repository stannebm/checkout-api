FROM openjdk:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/stanne-0.0.1-SNAPSHOT-standalone.jar /stanne/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/stanne/app.jar"]
