# Gym-CRM Application Documentation

## Overview
The Gym-CRM application is a comprehensive system designed to manage gym operations, including users, trainers, trainees, training types, and training sessions. It is built using Java, Spring Core, and Maven.

## Key Entities and Relationships

### Entities
1. **User**: Represents a user in the system, which can be either a trainer or a trainee.
2. **Trainer**: A user who provides training sessions.
3. **Trainee**: A user who receives training sessions.
4. **TrainingType**: Represents different types of training available.
5. **Training**: Represents a training session between a trainer and a trainee.

### Relationships
- **User - Trainer**: One-to-one relationship.
- **User - Trainee**: One-to-one relationship.
- **Trainer - TrainingType**: Many-to-one relationship (one training type can have many trainers).
- **Training - TrainingType**: Many-to-one relationship (one training type can have many training sessions).
- **Trainee - Training**: One-to-many relationship (one trainee can have many training sessions).
- **Trainer - Training**: One-to-many relationship (one trainer can have many training sessions).

## Project Structure

### `src/main/java/com/epam`
- **`facade/command/UpdateCommand.java`**: Contains methods to update various entities like User, Trainer, Trainee, TrainingType, and Training.
- **`facade/command/CreateCommand.java`**: Contains methods to create various entities like User, Trainer, Trainee, TrainingType, and Training.
- **`facade/command/DeleteCommand.java`**: Contains methods to delete various entities like User, Trainer, Trainee, TrainingType, and Training.
- **`facade/command/ListCommand.java`**: Contains methods to list various entities like User, Trainer, Trainee, TrainingType, and Training.
- **`facade/command/FindCommand.java`**: Contains methods to find various entities like User, Trainer, Trainee, TrainingType, and Training.
- **`service/TrainingService.java`**: Service class for managing training sessions.
- **`service/TrainingTypeService.java`**: Service class for managing training types.
- **`service/UserService.java`**: Service class for managing users.
- **`service/TrainerService.java`**: Service class for managing trainers.
- **`service/TraineeService.java`**: Service class for managing trainees.
- **`dto/`**: Contains DTO classes.
- **`entity/`**: Contains entity classes.
- **`repository/`**: Contains repository classes.
- **`service/`**: Contains service classes.
- **`util/`**: Contains utility classes.
- **`aop/`**: Contains Aspect-Oriented Programming classes.

### `src/main/resources`
- **`data/trainingTypes.json`**: JSON file containing predefined training types.
- **`data/users.json`**: JSON file containing predefined users.
- **`data/trainers.json`**: JSON file containing predefined trainers.
- **`data/trainees.json`**: JSON file containing predefined trainees.
- **`data/trainings.json`**: JSON file containing predefined training sessions.
- **`application.properties`**: Contains application properties.
- **`logback.xml`**: Configuration file for logging.

## Services

### Service
- **Methods**:
    - `add(Dto trainerDto)`: Adds a new trainer.
    - `update(Dto trainerDto)`: Updates an existing trainer.
    - `delete(BigInteger id)`: Deletes a trainer by ID.
    - `get(BigInteger id)`: Retrieves a trainer by ID.
    - `getAll()`: Retrieves all trainers.

## DTOs

### TrainerDto
- **Fields**:
    - `BigInteger id`: Unique identifier.
    - `TrainingTypeDto specialization`: Specialization of the trainer.
    - `UserDto user`: User details of the trainer.

### TraineeDto
- **Fields**:
    - `BigInteger id`: Unique identifier.
    - `Date dob`: Date of birth.
    - `String address`: Address of the trainee.
    - `UserDto user`: User details of the trainee.

### UserDto
- **Fields**:
    - `BigInteger id`: Unique identifier.
    - `String firstName`: Name of the user.
    - `String lastName`: Email of the user.
    - `String username`: Phone number of the user.
    - `String password`: Password of the user.

### TrainingTypeDto
- **Fields**:
    - `BigInteger id`: Unique identifier.
    - `String name`: Name of the training type.

### TrainingDto
- **Fields**:
    - `BigInteger id`: Unique identifier.
    - `TrainerDto trainer`: Trainer details.
    - `TraineeDto trainee`: Trainee details.
    - `TrainingTypeDto trainingType`: Training type details.
    - `Date date`: Date of the training session.
    - `String duration`: Duration of the training session.

### Commands
- **`find`**: Finds an entity by ID.
- **`create`**: Creates a new entity.
- **`update`**: Updates an existing entity.
- **`delete`**: Deletes an entity.
- **`list`**: Lists all entities.
- **`help`**: Displays help information.

## Conclusion
This documentation provides an overview of the Gym-CRM application, including its key entities, relationships, project structure, services, DTOs, entities, JSON data, and usage examples. This should help in understanding and extending the application.