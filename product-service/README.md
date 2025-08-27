# README - Product Microservice

## Description

The Product Microservice is part of an e-commerce microservices architecture. It's responsible for managing products, their categories, prices, and other attributes related to the product catalog.

## Features

- Product management (create, update, delete, search)
- Product categorization
- Product search by various criteria
- Result pagination
- Data caching with automatic expiration (TTL)
- Performance monitoring and metrics

## Technologies

### Core

- Java
- Spring Boot
- Spring Data JPA
- Spring Cloud (Eureka integration)
- H2 Database (in-memory database)

### Performance and Caching

- Caffeine Cache - high-performance Java caching library
- TTL (Time-To-Live) for different types of cached data
- Varied cache parameters for different data types

### API Documentation

- OpenAPI 3.0 (Swagger) - automatic API documentation
- Available at `/swagger-ui`

### Monitoring

- Spring Boot Actuator - application health monitoring
- Micrometer - metrics collection
- Prometheus - metrics export format
- Cache metrics (hits, misses, response time)

## Architecture

The microservice is implemented using a layered pattern:

1. **Controller layer** - handling HTTP requests
2. **Service layer** - business logic
3. **Repository layer** - data access
4. **DTO layer** - data transfer objects
5. **Entity layer** - data model

## Configuration

Main settings are defined in the `application.properties` file:

- Server port: 8081
- Registration with Eureka Discovery Server
- H2 Database configuration
- JPA/Hibernate settings
- Swagger configuration
- Monitoring and cache settings

## How to Run

1. Make sure the Discovery Server is running
2. Run the microservice using Maven:
   ```
   mvn spring-boot:run
   ```
3. The microservice will start on port 8081 and register with Eureka

## Available Endpoints

- **Product API**: `/api/products`
- **Swagger Documentation**: `/swagger-ui`
- **Metrics**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`
- **H2 Console**: `/h2-console`

## Implemented Optimizations

- **Caching with TTL**:
    - Product cache (1 hour TTL)
    - Product category cache (15 minutes TTL)
    - Price range cache (30 minutes TTL)

- **Transaction Management**:
    - Optimal database transaction handling
    - Transaction isolation for read and write operations

- **Pagination**:
    - Result paging for large data sets
    - Parameterized sorting and filtering

## Monitoring

The microservice provides various performance metrics through Spring Boot Actuator:

- Cache statistics (hits, misses)
- Endpoint response times
- Resource utilization
- Database statistics

All metrics are available in Prometheus format at `/actuator/prometheus`.

## Integration with Other Microservices

The product microservice is registered with the Eureka Discovery Server, allowing other microservices (e.g., order service) to discover and communicate with it.