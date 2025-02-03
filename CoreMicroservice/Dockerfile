FROM openjdk:21-jdk AS production
LABEL authors="Nurbek Malikov"
WORKDIR /app
COPY target/CoreMicroservice-1.1.0.jar .

ENTRYPOINT ["java", "-jar", "./CoreMicroservice-1.1.0.jar"]