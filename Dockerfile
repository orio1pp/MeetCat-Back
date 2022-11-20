FROM openjdk:11-jre-slim
EXPOSE 8080
ADD target/meetcat-back-docker.jar meetcat-back-docker.jar
ENTRYPOINT ["java","-jar","meetcat-back-docker.jar"]