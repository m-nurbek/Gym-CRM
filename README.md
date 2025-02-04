# Gym-CRM Application Documentation

![Gym-CRM](https://github.com/user-attachments/assets/4c100587-ee7b-4c0d-a6e4-088c59424197)

## Project Overview
Gym-CRM is a robust Customer Relationship Management (CRM) system designed for gym and fitness centers. It leverages a microservices architecture to ensure scalability, reliability, and maintainability. The system includes the following microservices:
- **ApiGateway**: Serves as the entry point for all client requests.
- **Eureka Server**: Service discovery server.
- **Core Microservice**: Manages core functionalities and uses PostgreSQL for data storage.
- **TrainerReportMicroservice**: Handles trainer reports and uses MongoDB for data storage.

The project also integrates various tools and frameworks to enhance its capabilities, including:
- **Zipkin** for distributed tracing.
- **RabbitMQ** for messaging.
- **PostgreSQL** for CoreMicroservice database.
- **MongoDB** for TrainerReportMicroservice database.
- **Spring Boot** features like Spring Cloud, Spring Security, and Spring Data.
- **Resilience4j** for CircuitBreaker pattern to handle faults gracefully.

## Features
- **Microservices Architecture**: Decoupled services for better scalability and maintainability.
- **Service Discovery**: Eureka Server for dynamic service registration and discovery.
- **API Gateway**: Centralized entry point for all requests.
- **Secure**: Implements Spring Security and adheres to OWASP Top 10 security practices.
- **Distributed Tracing**: Zipkin integration for tracing and debugging distributed transactions.
- **Message Broker**: RabbitMQ for asynchronous communication between services.
- **Database Integration**: Uses PostgreSQL and MongoDB for data persistence.

## Installation
To get started with Gym-CRM, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/m-nurbek/Gym-CRM.git
   cd Gym-CRM
   ```

2. **Set up the databases**:
   - PostgreSQL: Install and configure PostgreSQL for CoreMicroservice.
   - MongoDB: Install and configure MongoDB for TrainerReportMicroservice.

3. **Start RabbitMQ**:
   Ensure RabbitMQ is installed and running.

4. **Configure Zipkin**:
   Start Zipkin server for distributed tracing.

5. **Build and run the microservices**:
   Use Maven to build and run each microservice.
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Usage
### Swagger UI
Swagger is available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html). Use it to explore and test the API endpoints.

## Contributing
We welcome contributions to enhance the Gym-CRM system. Please follow these guidelines:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Contact
For any inquiries or support, please contact [m-nurbek](https://github.com/m-nurbek).

Feel free to reach out if you have any questions or need further assistance!
