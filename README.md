# Parking-Lot

### Endpoints

- [POST] http://localhost:9090/api/v1/parking/park/:vehicleType
    - Response
      ```
      {   
         "ticketNumber": "001",   
         "spotNumber": "SLOT-80",   
         "entryDateTime": "2023-03-11T16:17:38"   
      }
      ```
- [POST] http://localhost:9090/api/v1/parking/un_park/:ticketNumber
    - Response
      ```
      {
         "receiptNumber": "R-001",
         "entryDateTime": "2023-03-11T16:17:38",
         "exitDateTime": "2023-03-11T16:18:09",
         "fee": 40
      }
        ```
- [GET] http://localhost:9090/api/v1/parking/availableSlots/:vehicleType
    - Response
      ```
        10
      ```
- [GET] http://localhost:9090/api/v1/parking/availableSlots
    - Response
      ```
      {
         "CARS": 80,
         "BUSES": 10,
         "MOTORCYCLES": 100
      }
      ```

### Technology

- Java 17
- Embedded Tomcat
- Maven 3
- JUnit 5
- Mockito
- Spring Boot 3
- MAP as a DB

### Steps To run the app

We have provided an `init.sh` scripts to bootstrap the application. This script will `clean`, `compile`, `package`
, `run unit tests` and finally `launch` the spring boot app.

```
$ sh scripts/init.sh
```

### Considerations

Have created this as a simple boot app, where I am using Map as a database for type of adding a vehicle details,
deleting an un park vehicle details, maintaining parking ticket number etc. To avoid extra overhead and due to time
limits.

By default, app will start with location as `MALL` (configured in `application.yaml` file) and default available slots
configured in `ParkingRepository.java`.

We can override the `location` and `slots` behaviour by changing the value in the `application.yaml` and
`ParkingRepository.java` -> property `NUMBER_OF_SPOTS_MAP`

```
static 
{
    NUMBER_OF_SPOTS_MAP.put(MOTORCYCLES, 100);
    NUMBER_OF_SPOTS_MAP.put(CARS, 80);
    NUMBER_OF_SPOTS_MAP.put(BUSES, 10);
}

```

```
parking:
  location: MALL
```


