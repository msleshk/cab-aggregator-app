Feature: Ride Management

  Scenario: Create a new ride
    Given create new ride body
      """
      {
        "passengerId": 6,
        "departureAddress": "123 Main St",
        "arrivalAddress": "456 Elm St",
        "distance": "20"
      }
      """
    When create new ride
    Then response status is 201

  Scenario: Get ride by ID
    When get ride with id 1
    Then response status is 200
    And response body contains ride details
      """
      {
        "id": 1,
        "driverId": "1",
        "passengerId": 1,
        "departureAddress": "126 Pobedy Avenue, Minsk",
        "arrivalAddress": "34 Alexeeva Street, Minsk",
        "status": "COMPLETED"
      }
      """

  Scenario: Get all rides with pagination
    When get all rides with offset 0 and limit 10
    Then response status is 200

  Scenario: Delete a ride
    When delete ride with id 1
    Then response status is 200

  Scenario: Update an existing ride
    Given update ride with id 1
    """
    {
      "departureAddress": "789 Oak St",
      "arrivalAddress": "654 Pine St",
      "distance": "22"
    }
    """
    Then response status is 200

  Scenario: Update ride status
    When update ride status for ride 1 to "IN_PROGRESS"
    Then response status is 200
