# Minimart API

🚀 **A secure, scalable, and production-ready e-commerce backend built with Spring Boot.**

---

## 📦 Features Overview

* Clean Architecture (Domain-Driven Design)
* JWT Authentication with Refresh Token
* Role-Based Access Control (Admin & Customer)
* Product Catalog with Image Upload
* Cart & Order Management System
* Simulated Payment Integration
* API Versioning
* Caching with Redis
* Global Response Wrapping
* Rate Limiting
* Email Confirmation Support (via Mailtrap)
* Fully Dockerized

---

## 📁 Project Structure

```bash
springboot-minimart-api/
├── src/main/java/com/minimart/
│   ├── MiniMartApplication.java
│   ├── api/                # API Layer (Controllers, DTOs)
│   │   └── v1/             # Versioned API structure
│   ├── application/        # Services & Use Cases
│   ├── domain/             # Core Models & Repositories
│   ├── infrastructure/     # Persistence, Security, Config
│   └── common/             # Utilities, Global Exceptions
├── resources/
│   └── application.yml
├── test/                   # Unit & Integration Tests
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## ✅ Phase 1: Core Setup

### 1. Project Initialization

* Spring Web, Spring Data JPA, Spring Security
* PostgreSQL, Validation, Lombok
* Spring Cache, Mail, OpenAPI Docs

### 2. Domain Modeling

* Entities: Product, Category, User, Cart, CartItem, Order, OrderItem, Payment, Invoice
* JPA relationships (OneToMany, ManyToOne, OneToOne)
* Repositories using `JpaRepository<>`

### 3. Input Validation

* DTO-level validation using `@NotBlank`, `@Size`, etc.
* Global exception handling with `@RestControllerAdvice`

---

## 🔐 Phase 2: Auth & Roles

### 4. JWT Auth

* Login/Register with JWT Access & Refresh tokens
* Passwords hashed with `BCryptPasswordEncoder`

### 5. Refresh Token

* Store in DB or Redis
* Secure `/auth/refresh` endpoint

### 6. RBAC

* Roles: `USER`, `ADMIN`
* Use `@PreAuthorize("hasRole('ADMIN')")` for securing endpoints

---

## 🏭 Phase 3: Core E-Commerce Features

### 7. Product API

* Admin: Create/Update/Delete
* Public: List/View
* Image Upload with Multipart

### 8. Pagination & Filtering

* Endpoint: `/api/v1/products`
* Params: `page`, `size`, `category`, `minPrice`, `maxPrice`

### 9. Cart & Orders

* Authenticated cart operations
* Checkout triggers transactional order creation

### 10. Payment Simulation

* Mark order as paid
* Store `PaymentInfo`

---

## ⚙️ Phase 4: Infrastructure

### 11. API Versioning

* URL path: `/api/v1/...`

### 12. Global Response Wrapper

```json
{
  "timestamp": "2025-07-22T00:00:00Z",
  "status": 200,
  "message": "success",
  "data": {},
  "path": "/api/v1/products"
}
```

### 13. Caching

* Enable with `@EnableCaching`
* Use `@Cacheable` with TTL settings

### 14. Rate Limiting

* Implement with Bucket4j or Redis
* Limit sensitive actions (e.g., checkout)

### 15. Email Confirmation

* Send email on registration & order events using [Mailtrap](https://mailtrap.io/)

---

## 🧪 Phase 5: Dev & Deployment

### 16. DevTools

* Hot reload with Spring Boot DevTools

### 17. Testing

* JUnit, Mockito
* `@SpringBootTest`, `MockMvc`, `@DataJpaTest`

### 18. OpenAPI (Swagger)

* Endpoint: `/swagger-ui.html`
* Documented via annotations

### 19. Docker Support

* `Dockerfile` for Spring App
* `docker-compose.yml` for Postgres, Redis, App

---

## 🌐 API Endpoints

### Auth

* `POST /api/v1/auth/register`
* `GET /api/v1/auth/verify?token=...`
* `POST /api/v1/auth/login`
* `POST /api/v1/auth/refresh`
* `POST /api/v1/auth/logout`

### Products

* `GET /api/v1/products`
* `GET /api/v1/products/{id}`
* `POST /api/v1/products` *(Admin)*
* `PUT /api/v1/products/{id}` *(Admin)*
* `DELETE /api/v1/products/{id}` *(Admin)*

### Categories

* `POST /api/v1/categories` *(Admin)*
* `GET /api/v1/categories`

### Cart

* `POST /api/v1/cart/items`
* `PUT /api/v1/cart/items/{itemId}`
* `DELETE /api/v1/cart/items/{itemId}`
* `GET /api/v1/cart`

### Orders

* `POST /api/v1/orders/checkout`
* `GET /api/v1/orders`
* `GET /api/v1/orders/{orderId}`

### Payments

* `POST /api/v1/payments`
* `GET /api/v1/payments/{paymentId}`

### Files

* `POST /api/v1/files/upload` *(Admin)*

---

## 📃 ER Diagram (Simplified)

* User → 1 Cart
* Cart → Many CartItems
* User → Many Orders
* Order → Many OrderItems
* Order → 1 Payment
* Order → 1 Invoice
* Category → Many Products

---

## 🧰 Tech Stack

* Java 24 + Spring Boot 3
* Spring Security + JWT
* PostgreSQL + JPA + Redis
* Docker + Docker Compose
* Swagger / OpenAPI 3
* Email (SMTP via Mailtrap)

---

## 🏁 Getting Started

### Prerequisites:

* Java 17
* Docker

### Run via Docker Compose

```bash
docker-compose up --build
```

Access:

* Swagger UI: `http://localhost:8080/swagger-ui.html`
* Postgres: `localhost:5432`
* Redis: `localhost:6379`

---

## 📄 License

MIT [License](./LICENSE). Feel free to use and contribute!
