Feature: Parking

  Scenario: park the vehicle

    when the customer park the car
    then the customer receives status code of 202
    and the customer receives the parking ticket number