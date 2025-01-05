Feature: Trainee endpoints
  
  Background:
    Given firstname: "Samuel", lastname: "Jackson" and password: "MyPassword1!@"
    When trainee registers
    Then the status code is 201
    Then the username should start with "Samuel.Jackson"
    Then the password is "MyPassword1!@"

    When login with username: "Samuel.Jackson" and password: "MyPassword1!@"
    Then the status code is 200
    
  Scenario: trainee tries to access its endpoints
    When trying to get trainee profile
    Then the status code is 200
    Then the firstName is "Samuel" and lastName is "Jackson"
    
    When trying to update trainee profile with new lastname: "Smurf"
    Then the status code is 200
    Then the firstName is "Samuel" and lastName is "Smurf"
    
    When trying to delete profile
    Then the status code is 204