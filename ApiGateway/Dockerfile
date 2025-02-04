FROM openjdk:21-jdk AS production
LABEL authors="Nurbek Malikov"
WORKDIR /app
COPY target/ApiGateway-1.1.0.jar .

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "./ApiGateway-1.1.0.jar"]