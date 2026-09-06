# Blogs App

A simple REST API for creating, reading, updating, and deleting blog posts, built with Spring Boot 4 and an H2 file database.

## Tech Stack
- Java, Spring Boot 4.1.1
- Spring Data JPA + H2 (file-based)
- Lombok
- JUnit 5, Mockito, MockMvc

## How to Run
./mvnw spring-boot:run

The app starts on `http://localhost:8080`. A test user and post are seeded automatically on startup.

## API Endpoints

| Method | URL              | Description                          | Requires `X-User-Id` header |
|--------|------------------|---------------------------------------|------------------------------|
| POST   | /api/posts       | Create a new post                     | Yes |
| GET    | /api/posts       | Get all posts                         | No |
| GET    | /api/posts/{id}  | Get a single post                     | No |
| PUT    | /api/posts/{id}  | Update a post (author only)           | Yes |
| DELETE | /api/posts/{id}  | Delete a post (author only)           | Yes |

Example create request body:
```json
{
  "title": "My Post",
  "body": "Post content here"
}
```

## Design Decisions
- **H2 with a file** instead of an in-memory list, so data survives restarts and the setup is closer to a real database.
- **Layered structure**: Controller → Service → Repository, with DTOs so the API never exposes raw database entities directly.
- **Author ownership**: a `Post` belongs to a `User`. Only the author can update or delete their own post. Since there's no real login system, the "current user" is passed via an `X-User-Id` header — this stands in for authentication, which was out of scope for this assessment.
- **Errors**: not found → 404, not the author → 403, invalid input → 400. Handled in one place (`GlobalExceptionHandler`).

## Tests
./mvnw test

13 tests covering service-layer logic (ownership checks, not-found handling) and controller-layer behavior (status codes, validation, JSON shape).
