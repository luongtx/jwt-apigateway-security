# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Spring Boot microservices architecture** implementing JWT-based API gateway security for a Swiggy-like food delivery system. The project demonstrates:
- Service discovery with Eureka
- API Gateway with JWT authentication/authorization
- Microservices communication
- Docker containerization

## Architecture

### Services
1. **swiggy-service-registry** (port 8761) - Eureka Server for service discovery
2. **swiggy-gateway** (port 8080) - Spring Cloud Gateway with JWT authentication filter
3. **identity-service** (port 9898) - Authentication/authorization, user management, JWT generation
4. **restaurant-service** (port 8082) - Restaurant and order management backend
5. **swiggy-app** (port 8081) - Client-facing application service

### Security Flow
- Gateway validates JWT tokens via `JwtUtil` for secured endpoints
- Open endpoints (no JWT required): `/auth/register`, `/auth/token`, `/auth/validate`, `/eureka`
- Identity-service uses Spring Security with BCrypt password encoding
- JWT secret is hardcoded in both `JwtService` (identity-service) and `JwtUtil` (gateway)

### Request Flow
1. Client → Gateway (port 8080) with `Authorization: Bearer <token>`
2. Gateway checks `RouteValidator` for secured endpoints
3. Gateway validates JWT via `JwtUtil.validateToken()`
4. Gateway routes to appropriate service using `lb://SERVICE-NAME` (load-balanced via Eureka)
5. Services register with Eureka and discover each other

## Development Environment

### Prerequisites
- JDK 17
- Maven 3.9+ (or use included `mvnw` wrappers)
- Docker & Docker Compose
- MySQL 8.0 (or use docker-compose)

### Quick Start

**Start all services with Docker:**
```bash
docker-compose up -d
```

**Start specific service in debug mode:**
```bash
./debug.sh -s identity-service
# or with rebuild:
./debug.sh -r -s swiggy-gateway
```

**Access services:**
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
- Identity Service: http://localhost:9898
- Restaurant Service: http://localhost:8082
- Swiggy App: http://localhost:8081

### Build & Test Commands

**Build all services:**
```bash
# From root - builds all modules
./mvnw clean install
# or
mvn clean install
```

**Build single service:**
```bash
cd identity-service
../mvnw clean package
# or use Maven reactor:
mvn -pl identity-service clean package
```

**Run tests:**
```bash
# All tests
mvn test

# Single test class
mvn test -Dtest=IdentityServiceApplicationTests

# Single test method
mvn test -Dtest=IdentityServiceApplicationTests#contextLoads
```

**Run a service locally (without Docker):**
```bash
cd identity-service
./mvnw spring-boot:run
```

**Skip tests during build:**
```bash
mvn clean package -DskipTests
```

**Generate Docker images:**
```bash
docker-compose build
```

**View logs:**
```bash
docker-compose logs -f [service-name]
```

## Project Structure

```
.
├── docker-compose.yml          # Orchestrates all services
├── debug.sh                    # Helper script for debug mode
├── docker/
│   └── java-entrypoint.sh     # Container entrypoint
├── identity-service/          # Auth & JWT generation
│   ├── src/main/java/com/javatechie/
│   │   ├── controller/AuthController.java
│   │   ├── config/AuthConfig.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   └── JwtService.java
│   │   ├── entity/UserCredential.java
│   │   ├── repository/UserCredentialRepository.java
│   │   └── config/CustomUserDetailsService.java
│   └── src/main/resources/application.yml
├── swiggy-gateway/            # API Gateway + JWT filter
│   ├── src/main/java/com/javatechie/
│   │   ├── filter/
│   │   │   ├── AuthenticationFilter.java
│   │   │   └── RouteValidator.java
│   │   ├── util/JwtUtil.java
│   │   └── config/AppConfig.java
│   └── src/main/resources/application.yml
├── swiggy-service-registry/   # Eureka Server
├── swiggy-app/                # Client service
│   ├── client/RestaurantServiceClient.java
│   └── controller/SwiggyAppController.java
└── restaurant-service/        # Restaurant backend
    ├── controller/RestaurantController.java
    └── service/RestaurantService.java
```

### Key Files
- **Gateway configuration**: `swiggy-gateway/src/main/resources/application.yml` - Defines route predicates and filters
- **Security config**: `identity-service/src/main/java/com/javatechie/config/AuthConfig.java` - Spring Security setup
- **JWT validation**: `swiggy-gateway/src/main/java/com/javatechie/filter/AuthenticationFilter.java`
- **Docker orchestration**: `docker-compose.yml`

## Testing the API

**1. Register a user:**
```bash
curl -X POST 'http://localhost:8080/auth/register' \
  -H 'Content-Type: application/json' \
  -d '{"name":"Basant","password":"Pwd1","email":"basant@gmail.com"}'
```

**2. Get JWT token:**
```bash
curl -X POST 'http://localhost:8080/auth/token' \
  -H 'Content-Type: application/json' \
  -d '{"username":"Basant","password":"Pwd1"}'
```

**3. Access protected endpoints:**
```bash
curl -X GET 'http://localhost:8080/swiggy/37jbd832' \
  -H 'Authorization: Bearer <JWT_TOKEN>'
```

## Important Notes

- Services communicate via Eureka service discovery (load balancing with `lb://` prefix)
- Gateway runs on port 8080, all external clients must go through it
- Identity-service uses MySQL (container: `mysql:8.0`)
- JWT secret is hardcoded (for production, use environment variables/secret management)
- Open API endpoints bypass JWT validation at gateway level
- All services extend `SpringBootApplication` and enable Eureka client (except registry itself)
- Test classes follow pattern: `*ApplicationTests.java` with `@SpringBootTest`

## Debugging

**Debug with JDWP:**
```bash
# The debug.sh script sets DEBUG=true and exposes port 5005
./debug.sh -r -s identity-service
# Then attach debugger to localhost:5005
```

**JMX/JMC monitoring:** Ports 9011-9015 are exposed for each service.

**View Eureka dashboard:** http://localhost:8761 (no auth required)
