# GEMINI.md

## Project Overview
This project is a Java-based microservices application implementing the **API Gateway Pattern** with **JWT Security**. It simulates a Swiggy-like backend architecture where multiple services are managed and secured through a centralized entry point.

### Main Components
- **`swiggy-gateway`**: A Spring Cloud Gateway application that acts as the entry point. It implements a custom `AuthenticationFilter` for JWT validation.
- **`identity-service`**: Manages user registration and JWT generation using Spring Security and MySQL.
- **`swiggy-service-registry`**: A Netflix Eureka server for service discovery.
- **`restaurant-service` & `swiggy-app`**: Sample backend microservices that provide business functionality.

## Tech Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.0.4, Spring Cloud 2022.0.1
- **Security**: JWT (JJWT), Spring Security
- **Discovery**: Netflix Eureka
- **Database**: MySQL 8.0
- **Orchestration**: Docker & Docker Compose

## Building and Running

### Prerequisites
- Java 17
- Maven
- Docker and Docker Compose

### Building the Project
Each service is an independent Maven project. You can build them using the Maven wrapper within each directory:
```bash
# Example for one service
cd swiggy-gateway
./mvnw clean package
```

### Running the Services
The easiest way to run the entire system is via Docker Compose:
```bash
docker-compose up -d
```
*Note: Ensure you have a shared network named `shared` or modify `docker-compose.yml` accordingly.*

### Service Ports
- **Service Registry**: 8761
- **API Gateway**: 8080
- **Identity Service**: 9898
- **Restaurant Service**: 8082
- **Swiggy App**: 8081
- **MySQL**: 3306

## Development Conventions

### Security Workflow
1.  **Public Endpoints**: `/auth/register`, `/auth/token`, and `/eureka` are open (configured in `RouteValidator.java`).
2.  **Authentication**: Requests to `/swiggy/**` and `/restaurant/**` must include a valid `Authorization: Bearer <token>` header.
3.  **Token Validation**: The Gateway validates the token using its own `JwtUtil` (sharing the same secret as `identity-service`).

### Routing
The Gateway routes requests based on path predicates defined in `swiggy-gateway/src/main/resources/application.yml`:
- `/swiggy/**` -> `SWIGGY-APP`
- `/restaurant/**` -> `RESTAURANT-SERVICE`
- `/auth/**` -> `IDENTITY-SERVICE`

## Key API Endpoints (via Gateway on 8080)

### 1. Register a User
`POST /auth/register`
```json
{
    "name": "username",
    "email": "user@example.com",
    "password": "password"
}
```

### 2. Generate Token
`POST /auth/token`
```json
{
    "username": "username",
    "password": "password"
}
```

### 3. Access Secured Service
`GET /swiggy/order-id` or `GET /restaurant/orders/status/order-id`
- **Header**: `Authorization: Bearer <JWT_TOKEN>`
