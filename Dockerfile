FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/meetcat-back-docker.jar meetcat-back-docker.jar
ENTRYPOINT ["java","-jar","meetcat-back-docker.jar"]