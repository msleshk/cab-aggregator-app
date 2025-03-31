Feature: Car Management

  Scenario: Add a new car
    When send POST request to "/cars" with body:
      """
      {
        "brand": "Toyota",
        "model": "Corolla",
        "color": "White",
        "carNumber": "1234AA-1"
      }
      """
    Then response status should be 201

  Scenario: Get all cars
    When send GET request to "/cars" with offset 0 and limit 10
    Then response status should be 200

  Scenario: Get car by ID
    When send GET request to "/cars/9"
    Then response status should be 200

  Scenario: Update car
    When send PATCH request to "/cars/12" with body:
      """
      {
        "brand": "Toyota",
        "model": "Corolla",
        "color": "Black",
        "carNumber": "1234AA-1"
      }
      """
    Then response status should be 200

  Scenario: Add existing car
    When send POST request to "/cars" with body:
      """
      {
        "brand": "Toyota",
        "model": "Corolla",
        "color": "Black",
        "carNumber": "1234AA-1"
      }
      """
    Then response status should be 409

  Scenario: Delete car
    When send DELETE request to "/cars/12"
    Then response status should be 200

  Scenario: Get non-existing car
    When send GET request to "/cars/999"
    Then response status should be 400
    And response should contain error message "Car with this id not found!"

  Scenario: Add invalid car
    When send POST request to "/cars" with body:
      """
      {
        "brand": "",
        "model": "Corolla",
        "color": "White",
        "carNumber": "123"
      }
      """
    Then response status should be 400
