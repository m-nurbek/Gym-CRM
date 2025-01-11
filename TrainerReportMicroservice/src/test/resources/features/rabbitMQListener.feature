Feature: RabbitMQ listeners

  Scenario: 1) Handle add report request
    When the message comes to addReportQueue with message: "{\"username\":\"john_doe\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"isActive\":true,\"trainingDate\":\"2024-12-11\",\"duration\":60,\"actionType\":\"ADD\"}"
    Then the process is finished and no exceptions are thrown
    Then a trainer with username "john_doe" exists in the database

  Scenario: 2) Handle add report request
    When the message comes to addReportQueue with message: "{\"username\":\"jane_smith\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"isActive\":true,\"trainingDate\":\"2024-12-11\",\"duration\":60,\"actionType\":\"ADD\"}"
    Then the process is finished and no exceptions are thrown
    Then a trainer with username "jane_smith" exists in the database

  Scenario: 3) Handle add report request
    When the message comes to addReportQueue with message: "{\"username\":\"john_doe\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"isActive\":true,\"trainingDate\":\"2024-12-11\",\"duration\":60,\"actionType\":\"ADD\"}"
    Then the process is finished and no exceptions are thrown
    Then a trainer with username "john_doe" exists in the database

  Scenario: 4) Handle add report request
    When the message comes to addReportQueue with message: "{\"username\":\"lois_samuel\",\"firstName\":\"Lois\",\"lastName\":\"Samuel\",\"isActive\":true,\"trainingDate\":\"2024-12-11\",\"duration\":60,\"actionType\":\"ADD\"}"
    Then the process is finished and no exceptions are thrown
    Then a trainer with username "lois_samuel" exists in the database

  Scenario: Handle delete report request
    When the message comes to deleteReportQueue with message: "{\"trainerUsernames\":[\"john_doe\",\"jane_smith\"]}"
    Then the process is finished and no exceptions are thrown