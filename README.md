# HMCTS Dev Test Backend
This will be the backend for the brand new HMCTS case management system. As a potential candidate we are leaving
this in your hands. Please refer to the brief for the complete list of tasks! Complete as much as you can and be
as creative as you want.

You should be able to run `./gradlew build` to start with to ensure it builds successfully. Then from that you
can run the service in IntelliJ (or your IDE of choice) or however you normally would.

There is an example endpoint provided to retrieve an example of a case. You are free to add/remove fields as you
wish.

## Task Management API
A task management API has been implemented alongside the provided example case endpoint.

## Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database
- Gradle
- JUnit 5
- RestAssured

## Running the Application
```bash
./gradlew bootRun
```

Application runs at:
http://localhost:8080

## API Documentation
Swagger UI:
http://localhost:8080/swagger-ui.html

## Database
H2 Console:
http://localhost:8080/h2-console

JDBC URL: `jdbc:h2:mem:taskdb`
Username: `sa`
Password: empty

## Endpoints
- `POST /tasks` — create a task
- `GET /tasks` — retrieve all tasks
- `GET /tasks/{id}` — retrieve a task by ID
- `PUT /tasks/{id}` — update a task
- `PATCH /tasks/{id}/status` — update task status
- `DELETE /tasks/{id}` — delete a task

## Running Tests
```bash
./gradlew test
./gradlew integrationTest
./gradlew check
```

## Notes
- Validation is handled using Jakarta Bean Validation
- Global exception handling ensures consistent error responses
- H2 is used as the in-memory database
- JPA auditing automatically sets created and updated timestamps
