# Trainer Report Microservice REST API

This document provides instructions on how to use the Trainer Report Microservice REST API using Postman.

## Prerequisites

- Postman installed on your machine.
- Run RabbitMQ with `5672` port exposed
  - Don't forget to change the env.properties file based on your specific credentials under the resource folder
- Run Postgres with `5432` port exposed
  - Don't forget to change the env.properties file for your specific configuration under the resource folder
- Also check all application.properties files
- All microservices running and accessible.

Example of `env.properties` file in `CoreMicroservice`:
```text
JWT_SECRET=your_secret

# Database
DB_USERNAME=your_username
DB_PASSWORD=your_password

H2_DB_USERNAME=your_username
H2_DB_PASSWORD=your_password

# RabbitMQ
RABBITMQ_USERNAME=your_username     # default is `guest`
RABBITMQ_PASSWORD=your_password     # default is `guest`
```

Example of `env.properties` file in `TrainerReportMicroservice`:
```text
# RabbitMQ
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

# MongoDB
MONGODB_USERNAME=your_username
MONGODB_PASSWORD=your_password
```

## Importing the Postman Collection

1. Open Postman.
2. Click on the `Import` button in the top left corner.
3. Select the `File` tab.
4. Click on `Choose Files` and select the Postman collection file.
5. Click on `Import`.