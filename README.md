# MiniMart API 🛒

A complete Spring Boot backend for a MiniMart e-commerce application featuring JWT authentication, payment integration with Xendit, and comprehensive RESTful APIs.

## 📋 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
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
- **Database Migration**: Flyway integration for schema management

## 🛠 Tech Stack

### Core Technologies
- **Java 21** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL 17** - Primary database
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
- **Flyway** - Database migration

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
│   │       ├── application.yml             # Base Configuration
│   │       ├── application-dev.yml         # Development Profile
│   │       ├── application-local.yml       # Local Profile
│   │       ├── application-prod.yml        # Production Profile
│   │       ├── db/migration/               # Flyway Migration Scripts
│   │       ├── static/                     # Static Resources
│   │       └── templates/                  # Email Templates
│   └── test/                               # Test Classes
├── docker-compose.yml                      # Database and Redis Services
├── docker-compose.dev.yml                 # Development Application
├── docker-compose.prod.yml                # Production Application
├── Dockerfile                              # Production Container
├── Dockerfile-dev                          # Development Container
├── .env.example                           # Environment Variables Template
└── pom.xml                                # Maven Dependencies
```

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL 17 (if running locally without Docker)
- Redis (if running locally without Docker)

### 1. Clone the Repository
```bash
git clone https://github.com/tamamhuda/minimart-api.git
cd minimart-api
```

### 2. Environment Setup

#### Create Environment Configuration
Copy the example environment file and customize it for your needs:

```bash
# For local development
cp .env.example .env.local

# For development environment
cp .env.example .env.dev

# For production environment
cp .env.example .env.prod
```

#### Environment Variables Reference

The `.env.example` file contains all the necessary environment variables:

```env
# Spring Profile (change based on environment: local, dev, prod)
SPRING_PROFILES_ACTIVE=local

# Application Configuration
APP_PORT=8080
APP_NAME=MiniMart

# Database Configuration
POSTGRES_DB=minimart_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432

# Database URL (adjust host based on environment)
DATABASE_URL=jdbc:postgresql://localhost:5432/minimart_db

# JWT Configuration
JWT_ACCESS_SECRET=R2XJ6wAOx1I7ZAPO10+fe0BNz8FZqfF2iA4rqzE4MYY=
JWT_REFRESH_SECRET=N0TFuEJ0Xj7kBIrbi5akMGHRaPWpfcZFeGQZHHi6m7s=

# JWT Expiration
JWT_ACCESS_EXPIRATION_IN_MINUTES=59
JWT_REFRESH_EXPIRATION_IN_DAYS=7

# AWS S3 Configuration (add your credentials)
AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_S3_REGION=
AWS_S3_BUCKET=

# Xendit Payment Configuration (add your credentials)
XENDIT_SECRET_KEY=
XENDIT_VERIFICATION_TOKEN=
XENDIT_SUCCESS_URL=http://localhost:8080/api/v1/webhook/payment
XENDIT_FAILURE_URL=http://localhost:8080/api/v1/webhook/payment

# Redis Configuration (adjust host based on environment)
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_DB=0
REDIS_KEY_PREFIX=minimart
REDIS_ADDRESS=redis://127.0.0.1:6379

# Mailtrap Email Configuration (add your credentials)
MAILTRAP_TOKEN=
MAILTRAP_SENDER_EMAIL=
MAILTRAP_TEMPLATE_VERIFICATION_EMAIL_ID=
MAILTRAP_TEMPLATE_VERIFICATION_URL=

# TOTP Configuration
TOTP_GLOBAL_SECRET=IazVijkSfZG/B41nZ3xIrYUO1CGxdjF+o5i8TyQXz4s=
TOTP_EXPIRY_IN_MINUTES=5
```

#### Environment-Specific Customizations

**For Local Development (`.env.local`):**
- Keep `SPRING_PROFILES_ACTIVE=local`
- Use `DATABASE_URL=jdbc:postgresql://localhost:5432/minimart_db`
- Use `REDIS_HOST=127.0.0.1` and `REDIS_ADDRESS=redis://127.0.0.1:6379`
- Set shorter JWT expiration for testing

**For Development Environment (`.env.dev`):**
- Change `SPRING_PROFILES_ACTIVE=dev`
- Use `DATABASE_URL=jdbc:postgresql://minimart_pg:5432/minimart_db` (Docker service name)
- Use `REDIS_HOST=minimart_redis` and `REDIS_ADDRESS=redis://minimart_redis:6379`
- Add development-specific AWS S3 bucket
- Use Xendit development keys

**For Production Environment (`.env.prod`):**
- Change `SPRING_PROFILES_ACTIVE=prod`
- Use strong, unique JWT secrets (minimum 64 characters)
- Use production database credentials
- Add production AWS S3 and Xendit credentials
- Use production email template URLs
- Ensure all sensitive values are properly secured

### 3. Credential Setup

Before running the application, you need to configure the following services:

#### Required Services
1. **AWS S3**: Create a bucket and obtain access keys
2. **Xendit**: Sign up for payment processing and get API keys
3. **Mailtrap**: Set up email delivery service and get API token

#### Security Notes
- **Never commit actual credentials to version control**
- **Generate strong JWT secrets for production** (use tools like `openssl rand -base64 64`)
- **Use environment-specific secrets** (different for dev/prod)
- **Rotate secrets regularly in production**

### 4. Running the Application

#### Local Development (Maven + Docker Services)
```bash
# Start database and Redis services
docker-compose --env-file .env.local up -d

# Run application with Maven
mvn spring-boot:run -Dspring.profiles.active=local
```

#### Development Environment (Full Docker)
```bash
# Build and start all services
docker-compose --env-file .env.dev -f docker-compose.yml -f docker-compose.dev.yml up --build

# Start in detached mode
docker-compose --env-file .env.dev -f docker-compose.yml -f docker-compose.dev.yml up -d --build
```

#### Production Environment (Full Docker)
```bash
# Build and start production services
docker-compose --env-file .env.prod -f docker-compose.yml -f docker-compose.prod.yml up --build -d

# Monitor logs
docker-compose --env-file .env.prod logs -f app
```

#### Pure Local Development (No Docker)
```bash
# Requires local PostgreSQL and Redis running
mvn spring-boot:run -Dspring.profiles.active=local
```

### 5. Docker Compose Commands Reference

#### Development Environment
```bash
# Start all services with build
docker-compose --env-file .env.dev -f docker-compose.yml -f docker-compose.dev.yml up --build

# Start in detached mode
docker-compose --env-file .env.dev -f docker-compose.yml -f docker-compose.dev.yml up -d

# View application logs
docker-compose --env-file .env.dev logs -f app

# Stop all services
docker-compose --env-file .env.dev -f docker-compose.yml -f docker-compose.dev.yml down

# Rebuild only application
docker-compose --env-file .env.dev -f docker-compose.dev.yml build app
```

#### Production Environment
```bash
# Start production services
docker-compose --env-file .env.prod -f docker-compose.yml -f docker-compose.prod.yml up --build -d

# Monitor logs
docker-compose --env-file .env.prod logs -f app

# Stop services
docker-compose --env-file .env.prod -f docker-compose.yml -f docker-compose.prod.yml down
```

#### Local Services Only
```bash
# Start only database and Redis
docker-compose --env-file .env.local up -d

# Stop services
docker-compose --env-file .env.local down
```

### 6. Access the Application
- **API Base URL**: `http://localhost:8080/api/v1`
- **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html` (local/dev only)
- **Health Check**: `http://localhost:8080/api/v1/healthz`
- **API Docs**: `http://localhost:8080/api/v1/api-docs` (local/dev only)

## ⚙️ Configuration

The application uses Spring Profiles for environment-specific configuration:

### Profiles
- **`local`**: Local development with `create-drop` schema, no Flyway
- **`dev`**: Development with Flyway migrations, relaxed rate limiting
- **`prod`**: Production with strict rate limiting, disabled docs, optimized settings

### Rate Limiting Configuration
The application uses Bucket4j with Redis for distributed rate limiting:

#### Development Settings
- **Auth endpoints**: 20 requests per 60 seconds per IP
- **Verification resend**: 5 requests per 60 seconds per user
- **Checkout**: 50 requests per 60 seconds per user

#### Production Settings
- **Auth endpoints**: 5 requests per 30 seconds per IP
- **Verification resend**: 1 request per 90 seconds per user
- **Checkout**: 10 requests per 30 seconds per user

### Database Migration
Flyway is used for database schema management:

- **Development/Production**: `flyway.enabled=true` with `ddl-auto=validate`
- **Local**: `flyway.enabled=false` with `ddl-auto=create-drop`
- Migration scripts: `src/main/resources/db/migration/`

## 📚 API Documentation

### Interactive Documentation
Visit `http://localhost:8080/api/v1/swagger-ui.html` for the complete interactive API documentation (available in local/dev profiles only).

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
- `POST /auth/verify` - Verify email with TOTP

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

# 2. Verify email (check email for TOTP code)
curl -X POST http://localhost:8080/api/v1/auth/verify \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "totp": "123456"
  }'

# 3. Login to get tokens
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
The payment system is configured through environment variables in your `.env` file:
```env
XENDIT_SECRET_KEY=xnd_development_your_secret_key
XENDIT_VERIFICATION_TOKEN=your_xendit_webhook_token
XENDIT_SUCCESS_URL=http://localhost:8080/api/v1/webhook/payment
XENDIT_FAILURE_URL=http://localhost:8080/api/v1/webhook/payment
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

### Database Operations

All database operations require the `dotenv-cli` npm package to load environment variables from your local `.env` file.

#### Prerequisites
```bash
# Install dotenv-cli globally (one-time setup)
npm install -g dotenv-cli
```

#### Local Development
```bash
# Run Flyway migrations
dotenv -e .env.local -- mvn -Plocal flyway:migrate

# View migration status
dotenv -e .env.local -- mvn -Plocal flyway:info

# Clean database (removes all objects)
dotenv -e .env.local -- mvn -Plocal flyway:clean

# Validate current schema against migrations
dotenv -e .env.local -- mvn -Plocal flyway:validate

# Reset database and re-run all migrations
dotenv -e .env.local -- mvn -Plocal flyway:clean
dotenv -e .env.local -- mvn -Plocal flyway:migrate

# Connect to local database directly
dotenv -e .env.local -- psql $DATABASE_URL

# Reset database schema (development only)
dotenv -e .env.local -- psql $DATABASE_URL -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
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

### Hot Reload
The project includes Spring Boot DevTools for automatic application restart during development (enabled in local/dev profiles).

## 🐳 Deployment

### Docker Production Build
```bash
# Build and start production environment
docker-compose --env-file .env.prod -f docker-compose.yml -f docker-compose.prod.yml up --build -d

# View logs
docker-compose --env-file .env.prod logs -f app
```

### Manual Production Deployment
```bash
# Build application
mvn clean package -DskipTests

# Run with production profile
java -Dlogging.config=./config/logback-spring.xml \
     -jar target/minimart-api.jar \
     --spring.profiles.active=prod
```

### Health Monitoring
The application provides health checks at:
- `GET /api/v1/healthz` - Basic health status
- `GET /api/v1/actuator/health` - Detailed health information (if actuator enabled)

### Security Considerations for Production

1. **JWT Secrets**: Use strong, unique secrets (minimum 64 characters for production)
2. **Database**: Use encrypted connections and strong passwords
3. **Rate Limiting**: Production has stricter limits than development
4. **API Documentation**: Swagger UI and API docs are disabled in production
5. **Error Handling**: Stack traces are not exposed in production
6. **Connection Pooling**: Configured for optimal performance with HikariCP

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
- Test with different profiles (local, dev, prod)

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
- Flyway for database migration management
- All contributors who help improve this project

---

## 🔧 Quick Start Commands

### Development Environment
```bash
# Clone the repository
git clone https://github.com/tamamhuda/minimart-api.git
cd minimart-api

# Create environment file from example
cp .env.example .env.dev
# Edit .env.dev with your actual configuration values

# Start development environment (database + Redis + application)
docker-compose --env-file .env.dev -f docker-compose.yml -f docker-compose.dev.yml up --build

# View logs
docker-compose --env-file .env.dev logs -f app

# Access Swagger UI (development only)
open http://localhost:8080/api/v1/swagger-ui.html
```

### Local Development
```bash
# Create local environment file
cp .env.example .env.local
# Edit .env.local with local configuration

# Option 1: Docker services + Maven application
docker-compose --env-file .env.local up -d
mvn spring-boot:run -Plocal

# Option 2: Pure local development (requires local PostgreSQL/Redis)
mvn spring-boot:run -Plocal
```

### Production Environment
```bash
# Create production environment file
cp .env.example .env.prod
# Configure .env.prod with production values and credentials

# Start production environment
docker-compose --env-file .env.prod -f docker-compose.yml -f docker-compose.prod.yml up --build -d

# Monitor logs
docker-compose --env-file .env.prod logs -f app
```

### Useful Commands
```bash
# Test the API health
curl http://localhost:8080/api/v1/healthz

# View running containers
docker ps

# Clean up Docker resources
docker-compose --env-file .env.dev down
docker system prune -f

# Rebuild application container only
docker-compose --env-file .env.dev -f docker-compose.dev.yml build app

# View environment-specific logs
docker-compose --env-file .env.dev logs -f app
```

### Environment File Setup Checklist

Before running the application, ensure your `.env` files contain:

- ✅ **Spring Profile**: Set `SPRING_PROFILES_ACTIVE` (local/dev/prod)
- ✅ **Database**: Configure PostgreSQL connection details
- ✅ **JWT Secrets**: Generate strong secrets for production
- ✅ **AWS S3**: Add your AWS credentials and bucket name
- ✅ **Xendit**: Configure payment processing credentials
- ✅ **Redis**: Set Redis connection details
- ✅ **Mailtrap**: Configure email service credentials
- ✅ **URLs**: Update webhook and verification URLs for your domain

For detailed setup instructions and advanced configuration, please refer to the sections above.