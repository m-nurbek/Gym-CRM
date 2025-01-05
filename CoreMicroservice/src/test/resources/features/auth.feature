Feature: Authentication

  Scenario Outline: Trainee Registration
    Given firstname: "<firstname>", lastname: "<lastname>" and password: "<password>"
    When trainee registers
    Then the status code is <statusCode>
    Then the username should start with "<username>"
    Then the password is "<password>"

    Examples:
      | firstname | lastname  | username      | password      | statusCode |
      | John      | Sina      | John.Sina     | Password1!@   | 201        |
      | Rock      | Johnson   | Rock.Johnson  | PsdafA1!@dsaf | 201        |
      | No        | Name      | No.Name       | TraewreQa@21! | 201        |

  Scenario: Trainer Registration
    Given firstname: "Samuel", lastname: "Johnson" and password: "NewPassword1!@"
    When trainer registers
    Then the status code is 201
    Then the username should start with "Samuel.Johnson"
    Then the password is "NewPassword1!@"

  Scenario: Trainee login
    Given firstname: "Peter", lastname: "Griffin" and password: "Password1!@"
    When trainee registers

    When login with username: "Peter.Griffin" and password: "Password1!@"
    Then the status code is 200
    Then access token and refresh token are valid

  Scenario: Trainer login
    Given firstname: "Lois", lastname: "Griffin" and password: "Password1!@"
    When trainer registers

    When login with username: "Lois.Griffin" and password: "Password1!@"
    Then the status code is 200
    Then access token and refresh token are valid

  Scenario: Trainee wants to change password, refresh token and logout
    Given firstname: "Rick", lastname: "Sanchez" and password: "Password1!@"
    When trainee registers
    
    When login with username: "Rick.Sanchez" and password: "Password1!@"
    Then the status code is 200
    Then access token and refresh token are valid

    When trying to refresh token
    Then the status code is 200
    Then access token and refresh token are valid

    When changing password to "NewPassword1!@"
    Then the status code is 204

    When logout
    Then the status code is 204

# ---------------------------------------------------------------------------------

  Scenario: Negative scenario when trainee tries to login with incorrect password
    Given firstname: "Peter", lastname: "Griffin" and password: "Password1!@"
    When trainee registers
    Then the status code is 201
    Then the username should start with "Peter.Griffin"
    Then the password is "Password1!@"

    When login with username: "Peter.Griffin" and password: "IncorrectPassword1!@"
    Then the status code is 401

  Scenario: Negative scenario when trainer tries to login with incorrect password
    Given firstname: "Peter", lastname: "Griffin" and password: "Password1!@"
    When trainer registers
    Then the status code is 201
    Then the username should start with "Peter.Griffin"
    Then the password is "Password1!@"

    When login with username: "Peter.Griffin" and password: "IncorrectPassword1!@"
    Then the status code is 401