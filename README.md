# Employee Management REST API

A beginner-friendly Spring Boot REST API for managing employee records. The project demonstrates a clean layered architecture, request validation, persistence with Spring Data JPA, and automated unit testing.

## Technologies

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 in-memory database
- Maven
- JUnit 5, Mockito, and AssertJ

## Project structure

- `controller`: receives HTTP requests and returns HTTP responses.
- `service`: contains the application operations and business logic.
- `repository`: provides database access through Spring Data JPA.
- `entity`: maps the `Employee` object to the database table.
- `dto`: defines request and response data exchanged by the API.
- `exception`: converts application errors and validation failures into clear JSON responses.

## Prerequisites

- Java Development Kit (JDK) 17 or newer
- Apache Maven 3.9 or newer
- Visual Studio Code with the Extension Pack for Java (recommended)

Check your installation:

```bash
java -version
mvn -version
```

## Run the application

From the project directory:

```bash
mvn spring-boot:run
```

The API starts at `http://localhost:8080`. H2 data is held in memory and is recreated when the application restarts.

## API endpoints

### Create an employee

`POST /api/employees`

```json
{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "email": "ada@example.com",
  "department": "Engineering",
  "salary": 75000
}
```

Returns `201 Created`.

### Get all employees

`GET /api/employees`

Returns `200 OK` with an array of employees.

### Get one employee

`GET /api/employees/{id}`

Returns `200 OK`, or `404 Not Found` when the ID does not exist.

### Update an employee

`PUT /api/employees/{id}`

Use the same JSON fields as the create endpoint. Returns `200 OK`, or `404 Not Found`.

### Delete an employee

`DELETE /api/employees/{id}`

Returns `204 No Content`, or `404 Not Found`.

Invalid or missing fields return `400 Bad Request` with a `messages` object describing the validation errors. Salary must be zero or greater and email must have a valid format.

## H2 console

For local development only, open `http://localhost:8080/h2-console` and use:

- JDBC URL: `jdbc:h2:mem:employeedb`
- User Name: `sa`
- Password: leave blank

## Run tests

```bash
mvn test
```

The test suite uses JUnit 5 and Mockito to test creation, retrieval, update, deletion, missing employees, service behavior, and invalid request validation.
