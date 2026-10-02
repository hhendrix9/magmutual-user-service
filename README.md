# MagMutual User Service

A Spring Boot REST API for retrieving and searching user data.

The application loads the provided CSV dataset into an in-memory H2 database at startup and exposes endpoints for retrieving individual users, filtering users by creation date, and searching across multiple user fields.

## Technologies

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Apache Commons CSV
- Jakarta Bean Validation
- JUnit / Mockito / MockMvc
- Springdoc OpenAPI / Swagger UI
- Maven

## Features

- Retrieve a user by ID
- Retrieve users created within a specified date range
- Search users across first name, last name, email, profession, country, and city
- Input validation and centralized exception handling
- Unit and integration testing
- Interactive API documentation through Swagger UI

## Running the Application

**Prerequisites**

- Java 17

From the project root:

```bash
./mvnw spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

The provided CSV data is automatically loaded into the in-memory H2 database at startup.

## API Endpoints

**Get User by ID**

```http
GET /api/v1/users/{id}
```

Example:

```http
GET /api/v1/users/100
```

Returns `404 Not Found` if the user does not exist.

**Get Users by Creation Date Range**

```http
GET /api/v1/users?startDate={startDate}&endDate={endDate}
```

Example:

```http
GET /api/v1/users?startDate=2021-03-20&endDate=2021-04-24
```

Dates use `YYYY-MM-DD` format. The range is inclusive and results are ordered by creation date.

**Search Users**

```http
GET /api/v1/users/search?q={query}
```

Example:

```http
GET /api/v1/users/search?q=Houston
```

Search matches results across first name, last name, email, profession, country, and city.

## Swagger / OpenAPI

Interactive API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI can be used to view and execute all API operations.

## H2 Database Console

The H2 console is available at:

```text
http://localhost:8080/h2-console
```

Connection details:

```text
JDBC URL: jdbc:h2:mem:magmutual
Username: sa
Password:
```

The database is recreated whenever the application restarts.

## Testing

Run the test suite with:

```bash
./mvnw test
```

The project contains 23 automated tests covering the service layer, controllers, CSV parsing, validation, error scenarios, and API integration.

## Project Reflection

**What did you think of the project?**

I enjoyed the project. The requirements were straightforward, but there was still flexibility to make decisions around the API design, application structure, validation, error handling, and testing. It was also similar to the type of Java and Spring Boot API development I've worked with throughout my career.

**What didn't you like about the project?**
There wasn't anything I really disliked about the project. I would have liked a little more context around how the service would be used in a real-world environment, particularly the expected data volume and how the API would be consumed. That information could influence decisions around database design, performance, scalability, etc.

**How would you change the project or approach?**

I wouldn't significantly change the approach I took, but if this were moving toward production, I would consider pagination as the data grows, a more scalable search solution, and replacing the in-memory database with a production database. I would also add appropriate security and observability.

**Anything else you would like to share?**

I enjoyed putting the service together and working through the different implementation decisions, from the API design and data handling to validation and testing. I also appreciated being able to choose the technologies and make my own decisions around how the service should be designed and structured.