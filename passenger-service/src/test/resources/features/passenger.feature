Feature: Passenger Management

  Scenario: Add a new passenger
    When send POST request to add a passenger with body:
      """
      {
        "name": "John Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "+375(33)677-90-90"
      }
      """
    Then response status should be 201

  Scenario: Get all passengers
    When send GET request to get all passengers with offset 0 and limit 10
    Then response status should be 200

  Scenario: Get passenger by ID
    When send GET request to get passenger by ID 13
    Then response status should be 200

  Scenario: Update passenger
    When send PATCH request to update passenger with ID 17 and body:
      """
      {
        "name": "Jane Doe",
        "email": "jane.doe@example.com",
        "phoneNumber": "+375(44)676-90-90"
      }
      """
    Then response status should be 200

  Scenario: Add passenger with existing email
    When send POST request to add a passenger with body:
      """
      {
        "name": "John Doe",
        "email": "jane.doe@example.com",
        "phoneNumber": "+375(44)678-90-90"
      }
      """
    Then response status should be 409
    And response should contain error message "Email already been taken!"

  Scenario: Delete passenger
    When send DELETE request to delete passenger with ID 17
    Then response status should be 200

  Scenario: Get non-existing passenger
    When send GET request to get passenger by ID 999
    Then response status should be 404
    And response should contain error message "No such passenger!"

  Scenario: Add invalid passenger
    When send POST request to add a passenger with body:
      """
      {
        "name": "Invalid",
        "email": "invalid@",
        "phone": "123"
      }
      """
    Then response status should be 400
