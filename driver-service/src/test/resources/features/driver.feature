Feature: Driver Management

  Scenario: Add a new driver
    When send POST request to "/drivers" with body:
      """
      {
        "name": "John Driver",
        "email": "john.driver@example.com",
        "phoneNumber": "+375(33)666-00-00",
        "gender": "MALE",
        "carId": null
      }
      """
    Then response status should be 201

  Scenario: Get all drivers
    When send GET request to "/drivers" with offset 0 and limit 10
    Then response status should be 200

  Scenario: Get driver by ID
    When send GET request to "/drivers/15"
    Then response status should be 200

  Scenario: Update driver
    When send PATCH request to "/drivers/22" with body:
      """
      {
        "name": "Jane Driver",
        "email": "jane.driver@example.com",
        "phoneNumber": "+375(33)666-00-00",
        "gender": "FEMALE",
        "carId": null
      }
      """
    Then response status should be 200

  Scenario: Add existing driver
    When send POST request to "/drivers" with body:
      """
      {
        "name": "John Driver",
        "email": "jane.driver@example.com",
        "phoneNumber": "+375(33)666-00-00",
        "gender": "MALE",
        "carId": null
      }
      """
    Then response status should be 409
    And response should contain error message "This email already taken!"

  Scenario: Delete driver
    When send DELETE request to "/drivers/22"
    Then response status should be 200

  Scenario: Get non-existing driver
    When send GET request to "/drivers/999"
    Then response status should be 400
    And response should contain error message "Driver with this id not found!"

  Scenario: Add invalid driver
    When send POST request to "/drivers" with body:
      """
      {
        "name": "Invalid",
        "email": "invalid@",
        "phoneNumber": "123",
        "gender": "MALE",
        "carId": null
      }
      """
    Then response status should be 400
