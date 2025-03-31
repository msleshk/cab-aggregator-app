Feature: Rating Service

  Scenario: Get all ratings
    When get all ratings with offset 0 and limit 10
    Then response status is 200

  Scenario: Get average rating by user
    When get average rating for user 3 with role "PASSENGER"
    Then response status is 200

  Scenario: Get rating by ride ID
    When get rating by ride id 10
    Then response status is 200

  Scenario: Get rating by user and role
    When get rating by user id 3 and role "DRIVER"
    Then response status is 200

  Scenario: Get rating by ID
    When get rating by id "67d2ecaf819ae251fec9f3e2"
    Then response status is 200

  Scenario: Create a new rating
    Given create new rating body
      """
      {
        "rideId": 10,
        "userId": 3,
        "userRole": "PASSENGER",
        "rating": 5,
        "comment": "Great ride!"
      }
      """
    When create new rating
    Then response status is 201

  Scenario: Update an existing rating
    Given update rating body
      """
      {
        "rating": 4,
        "comment": "Updated comment"
      }
      """
    When update rating with id "67d2ecaf819ae251fec9f3e2"
    Then response status is 200

  Scenario: Delete a rating
    When delete rating with id "67d2ecaf819ae251fec9f3e2"
    Then response status is 200
