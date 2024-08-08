FROM maven:3.9.7-eclipse-temurin-21 AS maven
MAINTAINER microsoft.com
ADD https://repo1.maven.org/maven2/io/zipkin/zipkin-server/3.2.1/zipkin-server-3.2.1-exec.jar /zipkin-server.jar
ENTRYPOINT ["java","-jar","/zipkin-server.jar"]
