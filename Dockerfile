FROM openjdk:8-jdk-alpine

EXPOSE 8081

ADD target/CloudStorageDiploma-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/myapp.jar"]