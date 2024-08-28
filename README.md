# Gym-CRM


USER - Trainer: 1 to 1
USER - Trainee: 1 to 1

Trainer - TrainingType: Many to 1  (for one trainingType there could be many trainers)
Training - TrainingType: Many to 1  (for one trainingType there could be many trainings)

Trainee - Training: 1 to many (for one Trainee there could be many trainings)
Trainer - Training: 1 to many (for one Trainer there could be many trainings)