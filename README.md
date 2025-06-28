# Spring Boot Layered Application
A well-structured Spring Boot application following layered architecture best practices.

## Features
- Layered architecture (Controller, Service, Repository, etc.)
- Global exception handling
- Docker Compose for PostgreSQL database
- Environment configuration with .env files
- Security configuration
- Unit and integration tests with Testcontainers

## Prerequisites
- Java 21
- Gradle
- Docker and Docker Compose

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── _2rkdev/
│   │           └── dischard/
│   │               ├── config/       # Configuration classes
│   │               ├── controller/   # REST controllers
│   │               ├── dto/          # Data Transfer Objects
│   │               ├── entity/       # JPA entities
│   │               ├── exception/    # Custom exceptions and handlers
│   │               ├── mapper/       # Object mappers
│   │               ├── repository/   # Data repositories
│   │               ├── service/      # Business logic
│   │               └── utils/         # Utility classes
│   └── resources/
│       ├── application.yml   # Application configuration
│       └── .env              # Environment variables
└── test/                     # Test classes
```

## Getting Started

### Setting Up the Environment
1. Clone the repository:
```bash
   git clone https://github.com/2RK-dev/disc-hard.git
```
or :
```bash
    git clone git@github.com:2RK-dev/disc-hard.git
```
and :
```bash
   cd layered-app
```

2. Configure your environment variables in the `.env` file located in the `src/main/resources/` directory.

3. Start the database using Docker Compose:
```bash
   docker-compose up -d
```

4. Create/update the .env file in src/main/resources/ with appropriate values:
```env
DB_URL=jdbc:postgresql://localhost:5432/discharddb
DB_USERNAME=admin #change me
DB_PASSWORD=admin #change me
APP_SECRET_KEY=your_secret_key_for_jwt
```

5. Run the application:
```bash
    ./gradlew bootRun
```

or just click the play button in your IDE

6. Access the API documentation:
```
http://localhost:8080/api/v1/swagger-ui.html
```
7. API Documentation
The application uses OpenAPI/Swagger for API documentation which provides:

* Interactive API documentation
* Request/response models
* Testing capabilities
* OpenAPI specification is available at /api/v1/api-docs `Swagger` UI is available at `/api/v1/swagger-ui.html`

8. Database Migrations
   The application uses `Flyway` for database migrations. Migration scripts are located in `src/main/resources/db/migration`.

## API Endpoints
- `GET /api/v1/{controllerName}`

## Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.4/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.4/gradle-plugin/packaging-oci-image.html)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/3.4.4/reference/testing/testcontainers.html#testing.testcontainers)
* [Testcontainers Postgres Module Reference Guide](https://java.testcontainers.org/modules/databases/postgres/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.4/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Testcontainers](https://java.testcontainers.org/)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.4/reference/web/servlet.html)