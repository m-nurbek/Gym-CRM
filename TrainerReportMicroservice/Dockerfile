FROM amazoncorretto:21 AS production
LABEL authors="Nurbek Malikov"
WORKDIR /app
COPY target/TrainerReportMicroservice-1.1.0.jar .

EXPOSE 8900
ENTRYPOINT ["java", "-jar", "./TrainerReportMicroservice-1.1.0.jar"]