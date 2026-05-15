# Project Overview: Spring Roomescape Member

This project is a Spring Boot application for a Roomescape reservation system, developed as part of the Woowacourse Level 2 Mission. It provides APIs to manage reservations, reservation times, and themes.

## Core Technologies
- **Language:** Java 21
- **Framework:** Spring Boot 3.4.4
- **Database:** H2 (In-memory)
- **Persistence:** Spring JDBC (`JdbcTemplate`)
- **Testing:** JUnit 5, REST Assured
- **Build Tool:** Gradle

## Architecture
The project follows a layered architecture:
- **Controller:** Handles HTTP requests and responses using DTOs.
- **Service:** Contains business logic and orchestrates data flow between repositories.
- **Repository/DAO:** Manages data persistence using `JdbcTemplate`.
- **Domain/Entity:** Represents core business objects and database entities.

## Key Domains
- **Reservation:** Represents a booking made by a user (name, date, time, theme).
- **ReservationTime:** Defines available time slots for reservations.
- **Theme:** Represents the different escape room themes available.

## Building and Running
- **Build:** `./gradlew build`
- **Run:** `./gradlew bootRun`
- **Test:** `./gradlew test`
- **H2 Console:** Accessible at `/h2-console` (configured in `application.properties`).

## Development Conventions
- **DTOs:** Use separate classes for API requests and responses (e.g., `CreateReservationRequest`, `ReservationResponse`).
- **Database Initialization:** `schema.sql` and `dummy.sql` in `src/main/resources` are used for automatic schema creation and data seeding.
- **Testing:**
    - Integration tests are located in `src/test/java/roomescape/MissionStepXTest.java`.
    - Use REST Assured for end-to-end API testing.
    - `@DirtiesContext` is frequently used in tests to ensure a clean state between test methods.
- **Coding Style:** Follow standard Java and Spring Boot conventions. Prefer using `JdbcTemplate` over complex ORMs as per project requirements.
