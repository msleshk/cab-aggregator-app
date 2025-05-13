Feature: Payment Service

  Scenario: Get driver balance
    When get driver balance by id 17
    Then response status is 200

  Scenario: Get passenger balance
    When get passenger balance by id 13
    Then response status is 200

  Scenario: Get payment by ID
    When get payment by id 11
    Then response status is 200

  Scenario: Get payment by ride ID
    When get payment by ride id 23
    Then response status is 200

  Scenario: Get all payments with pagination
    When get all payments with offset 0 and limit 10
    Then response status is 200

  Scenario: Create a new payment
    When create a new payment for ride 45 with passenger 13, driver 17 and cost 200.50
    Then response status is 201

  Scenario: Pay for a ride with promo code
    When pay for ride 45 with passenger 13 and promo code "DISCOUNT10"
    Then response status is 200

  Scenario: Pay for a ride without promo code
    When pay for ride 45 with passenger 13 and promo code ""
    Then response status is 200

  Scenario: Delete a payment
    When delete payment by id 11
    Then response status is 204

