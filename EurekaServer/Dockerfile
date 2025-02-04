FROM openjdk:21-jdk AS production
LABEL authors="Nurbek Malikov"
WORKDIR /app
COPY target/EurekaServer-1.1.0.jar .

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "./EurekaServer-1.1.0.jar"]