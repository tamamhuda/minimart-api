# MiniMart API 🛒

A complete Spring Boot backend for a MiniMart e-commerce application featuring JWT authentication, payment integration with Xendit, and comprehensive RESTful APIs.

## 📋 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Authentication](#-authentication)
- [Payment Integration](#-payment-integration)
- [Development](#-development)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### Core Features
- **User Management**: Registration, authentication, profile management
- **Product Catalog**: CRUD operations with categories and image upload
- **Shopping Cart**: Add, update, remove items with quantity management
- **Order Processing**: Checkout flow with order tracking
- **Payment System**: Xendit integration for secure payments
- **File Upload**: Profile and product image management

### Security & Infrastructure
- **JWT Authentication**: Access and refresh token strategy
- **Role-Based Access Control**: Admin and Customer roles
- **Rate Limiting**: Bucket4j with Redis backend
- **Caching**: Redis integration for performance
- **Email Notifications**: Mailtrap API for development and production
- **Account Verification**: TOTP-based email verification with global secret
- **File Storage**: AWS S3 integration for images and uploads
- **Global Exception Handling**: Consistent error responses
- **API Versioning**: `/api/v1` context path structure

## 🛠 Tech Stack

### Core Technologies
- **Java 17** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Primary database
- **Redis** - Caching & session storage

### Additional Libraries
- **MapStruct** - DTO mapping
- **Lombok** - Boilerplate code reduction
- **Bean Validation** - Input validation
- **Xendit Java SDK** - Payment processing
- **Bucket4j** - Rate limiting with Redis backend
- **SpringDoc OpenAPI** - API documentation
- **Mailtrap API** - Email notifications
- **OTP-Java** - TOTP generation
- **AWS SDK** - S3 file storage

### DevOps & Tools
- **Docker & Docker Compose** - Containerization
- **Maven** - Dependency management
- **JUnit & Mockito** - Testing

## 📁 Project Structure

```
minimart-api/
├── src/
│   ├── main/
│   │   ├── java/com/tamamhuda/minimart/
│   │   │   ├── api/v1/controller/          # REST Controllers
│   │   │   ├── application/
│   │   │   │   ├── dto/                    # Data Transfer Objects
│   │   │   │   ├── mapper/                 # MapStruct Mappers
│   │   │   │   ├── schema/                 # Response Schemas
│   │   │   │   └── service/                # Business Logic
│   │   │   ├── common/
│   │   │   │   ├── advice/                 # Global Exception Handler
│   │   │   │   ├── annotation/             # Custom Annotations
│   │   │   │   ├── authorization/          # Security Components
│   │   │   │   ├── dto/                    # Common DTOs
│   │   │   │   ├── exception/              # Custom Exceptions
│   │   │   │   ├── util/                   # Utility Classes
│   │   │   │   └── validation/             # Validation Groups
│   │   │   ├── config/                     # Configuration Classes
│   │   │   └── domain/
│   │   │       ├── entity/                 # JPA Entities
│   │   │       ├── enums/                  # Enumerations
│   │   │       └── repository/             # Data Repositories
│   │   └── resources/
│   │       ├── application.yml             # Configuration
│   │       ├── static/                     # Static Resources
│   │       └── templates/                  # Email Templates
│   └── test/                               # Test Classes
├── docker-compose.yml                      # Development Stack
├── Dockerfile                              # Application Container
└── pom.xml                                # Maven Dependencies
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL (if running locally)
- Redis (if running locally)

### 1. Clone the Repository
```bash
git clone https://github.com/tamamhuda/minimart-api.git
cd minimart-api
```

### 2. Environment Setup

#### Environment Variables
Create a `.env` file in the project root with the following variables:

```env
# Application Configuration
APP_NAME=minimart-api
APP_PORT=8080

# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/minimart
POSTGRES_USER=admin
POSTGRES_PASSWORD=password

# JWT Configuration
JWT_ACCESS_SECRET=your_jwt_access_secret_key_here
JWT_ACCESS_EXPIRATION_IN_MINUTES=15
JWT_REFRESH_SECRET=your_jwt_refresh_secret_key_here
JWT_REFRESH_EXPIRATION_IN_DAYS=7

# AWS S3 Configuration
AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key
AWS_S3_REGION=us-east-1
AWS_S3_BUCKET=minimart-uploads

# Xendit Configuration
XENDIT_SECRET_KEY=xnd_development_your_secret_key
XENDIT_VERIFICATION_TOKEN=your_xendit_webhook_token

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_DB=0
REDIS_KEY_PREFIX=minimart:

# Mailtrap Configuration
MAILTRAP_TOKEN=your_mailtrap_api_token
MAILTRAP_SENDER_EMAIL=noreply@minimart.com
MAILTRAP_TEMPLATE_VERIFICATION_EMAIL_ID=your_template_id
MAILTRAP_TEMPLATE_VERIFICATION_URL=http://localhost:8080/api/v1/auth/verify

# TOTP Configuration
TOTP_GLOBAL_SECRET=your_global_totp_secret
TOTP_EXPIRY_IN_MINUTES=5
```

#### Using Docker Compose (Recommended)
```bash
# Start all services (PostgreSQL, Redis, Application)
docker-compose up -d

# View logs
docker-compose logs -f app
```

#### Manual Setup
1. **Start PostgreSQL and Redis**:
```bash
# PostgreSQL
docker run --name postgres \
  -e POSTGRES_DB=minimart \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 -d postgres:15

# Redis
docker run --name redis -p 6379:6379 -d redis:7-alpine
```

2. **Set Environment Variables**: Ensure your `.env` file is properly configured (see above)

3. **Run the Application**:
```bash
mvn spring-boot:run
```

### 3. Access the Application
- **API Base URL**: `http://localhost:8080/api/v1`
- **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`
- **Health Check**: `http://localhost:8080/api/v1/healthz`
- **API Docs**: `http://localhost:8080/api/v1/api-docs`

## 📚 API Documentation

### Interactive Documentation
Visit `http://localhost:8080/api/v1/swagger-ui.html` for the complete interactive API documentation with:
- All available endpoints
- Request/response schemas
- Authentication examples
- Try-it-out functionality

### OpenAPI Specification
- **JSON Format**: `http://localhost:8080/api/v1/api-docs`
- **YAML Format**: `http://localhost:8080/api/v1/api-docs.yaml`

### Key Endpoint Categories

#### Authentication
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `POST /auth/refresh` - Refresh access token
- `POST /auth/logout` - Logout and revoke tokens
- `POST /auth/resend-verification` - Resend verification email

#### User Management
- `GET /users/me` - Get current user profile
- `PUT /users/{user_id}` - Update user profile
- `POST /users/{user_id}/upload` - Upload profile image
- `PUT /users/{user_id}/change-password` - Change password

#### Product Management
- `GET /products` - List products with filtering and pagination
- `POST /products` - Create new product (Admin only)
- `GET /products/{product_id}` - Get product details
- `PUT /products/{product_id}` - Update product (Admin only)
- `DELETE /products/{product_id}` - Delete product (Admin only)
- `POST /products/{product_id}/upload` - Upload product image

#### Cart Management
- `GET /cart` - Get user's cart
- `POST /cart/items` - Add item to cart
- `PUT /cart/items/{item_id}` - Update cart item
- `DELETE /cart/items/{item_id}` - Remove item from cart

#### Order Management
- `POST /orders/checkout` - Create order from cart items
- `GET /orders` - Get user's orders
- `GET /orders/{order_id}` - Get order details
- `POST /orders/{order_id}/payments` - Initiate payment

#### Webhooks
- `POST /webhook/xendit/payments` - Handle Xendit payment webhooks

## 🔐 Authentication

The API uses JWT-based authentication with access and refresh tokens.

### Registration & Login Flow
```bash
# 1. Register a new user
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "securepassword",
    "confirm_password": "securepassword",
    "full_name": "John Doe"
  }'

# 2. Login to get tokens
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "securepassword"
  }'
```

### Using Authentication
Include the JWT token in the Authorization header:
```bash
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Token Refresh
```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "token": "YOUR_REFRESH_TOKEN"
  }'
```

## 💳 Payment Integration

The application integrates with **Xendit** for payment processing and uses **AWS S3** for file storage.

### Payment Flow
1. **Create Order**: Checkout cart items to create an order
2. **Initiate Payment**: Create payment for the order
3. **Payment Processing**: User completes payment via Xendit
4. **Webhook Handling**: System receives payment status updates at `/webhook/xendit/payments`
5. **Order Completion**: Order status updated based on payment

### Configuration
The payment system is configured through environment variables:
```env
XENDIT_SECRET_KEY=xnd_development_your_secret_key
XENDIT_VERIFICATION_TOKEN=your_xendit_webhook_token
```

### Webhook Setup
Configure your Xendit webhook URL to point to:
```
https://yourdomain.com/api/v1/webhook/xendit/payments
```

### File Storage
The application uses AWS S3 for storing uploaded files (profile images, product images):
```env
AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key
AWS_S3_REGION=us-east-1
AWS_S3_BUCKET=minimart-uploads
```

## 🧪 Development

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
```

### Code Quality
```bash
# Format code
mvn spotless:apply

# Check style
mvn checkstyle:check

# Static analysis
mvn spotbugs:check
```

### Database Migration
The application uses JPA with `ddl-auto: none` for production safety. Database schema should be managed through proper migration tools like Flyway or Liquibase.

### Hot Reload
The project includes Spring Boot DevTools for automatic application restart during development.

## 🐳 Deployment

### Docker Production Build
```bash
# Build production image
docker build -t minimart-api:latest .

# Run with production profile
docker run -d \
  --name minimart-api \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=your_prod_db_url \
  -e REDIS_URL=your_prod_redis_url \
  minimart-api:latest
```

### Environment Variables
Key environment variables for production:
```env
# Application
APP_NAME=minimart-api
APP_PORT=8080

# Database
DATABASE_URL=jdbc:postgresql://prod-host:5432/minimart
POSTGRES_USER=minimart_user
POSTGRES_PASSWORD=secure_password

# JWT
JWT_ACCESS_SECRET=your_secure_jwt_access_secret
JWT_ACCESS_EXPIRATION_IN_MINUTES=15
JWT_REFRESH_SECRET=your_secure_jwt_refresh_secret
JWT_REFRESH_EXPIRATION_IN_DAYS=7

# AWS S3
AWS_ACCESS_KEY=your_production_access_key
AWS_SECRET_KEY=your_production_secret_key
AWS_S3_REGION=us-east-1
AWS_S3_BUCKET=minimart-prod-uploads

# Xendit
XENDIT_SECRET_KEY=xnd_production_your_secret_key
XENDIT_VERIFICATION_TOKEN=your_production_webhook_token

# Redis
REDIS_HOST=prod-redis-host
REDIS_PORT=6379
REDIS_DB=0
REDIS_KEY_PREFIX=minimart:prod:

# Mailtrap
MAILTRAP_TOKEN=your_production_token
MAILTRAP_SENDER_EMAIL=noreply@yourdomain.com
MAILTRAP_TEMPLATE_VERIFICATION_EMAIL_ID=your_template_id
MAILTRAP_TEMPLATE_VERIFICATION_URL=https://yourdomain.com/verify

# TOTP
TOTP_GLOBAL_SECRET=your_production_totp_secret
TOTP_EXPIRY_IN_MINUTES=5
```

### Health Monitoring
The application provides health checks at:
- `GET /api/v1/healthz` - Basic health status
- `GET /api/v1/actuator/health` - Detailed health information (if actuator enabled)

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Development Guidelines
- Follow Spring Boot best practices
- Write unit and integration tests
- Use conventional commit messages
- Update documentation for new features
- Ensure all tests pass before submitting PR

### Code Style
The project follows standard Java coding conventions with:
- 4-space indentation
- Line length limit of 120 characters
- Proper JavaDoc for public APIs

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 📞 Contact

**Tamam Huda**
- Email: contact@utadev.sh
- GitHub: [@tamamhuda](https://github.com/tamamhuda)
- Website: [utadev.sh](https://utadev.sh)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Xendit for payment processing capabilities
- All contributors who help improve this project

---

## 🔧 Quick Start Commands

```bash
# Clone and start with Docker
git clone https://github.com/tamamhuda/minimart-api.git
cd minimart-api

# Create .env file with your configuration
cp .env.example .env
# Edit .env with your actual values

docker-compose up -d

# Access Swagger UI
open http://localhost:8080/api/v1/swagger-ui.html

# Test the API
curl http://localhost:8080/api/v1/healthz
```

For detailed setup instructions and advanced configuration, please refer to the sections above.