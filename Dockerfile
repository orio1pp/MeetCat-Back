FROM openjdk:11-jdk-alpine
EXPOSE 8080
ADD target/meetcat-back-docker.jar meetcat-back-docker.jar
ENTRYPOINT ["java","-jar","/app.jar","meetcat-back-docker.jar"]