# 📦 Microservices System – Chat Application

## 📖 Project Overview
This project is a chat application built using a **microservices architecture**. Each service is responsible for a specific domain (e.g., authentication, users, messaging), and communication between services is handled via REST APIs and Feign Client.

---

## 🛠️ Technologies

- Java 17+
- Spring Boot
- Spring Security + JWT
- Spring Cloud OpenFeign
- Maven
- PostgreSQL
- Docker (optional)
- Kafka

---

## 🚀 How to Run the Project

### 1. Clone the repository
```
git clone <repo-url>
cd <project-folder>
```

### 2. Configure the database
In `application.yml` or `application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/db_name
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 3. Run services
Each microservice runs independently:
```
cd auth-service
mvn spring-boot:run

cd user-service
mvn spring-boot:run

cd chat-service
mvn spring-boot:run
```

Or run them via your IDE (e.g., IntelliJ) by starting the `Application` class.

---

## 🔐 Authentication (JWT)

1. Get token:
```
POST /auth/login
```
Request body:
```
{
  "username": "user",
  "password": "password"
}
```

2. Use token in header:
```
Authorization: Bearer <token>
```

---

## 🔗 API Endpoints

### 📌 Auth Service
- POST /v1/auth/register – register user
- POST /v1/auth/token – generate JWT token
- GET /v1/auth/.well-known/jwks.json – public key for JWT verification

### 👤 User Service
- GET /v1/users/all – get all users
- GET /v1/users/{id} – get user by ID
- GET /v1/users/search?query= – search users
- GET /v1/users/me – get current user
- PATCH /v1/users/profile – update user profile
- POST /v1/users – create user

### 💬 Chat Service
- POST /v1/messages/ – send message
- GET /v1/messages/conversations – get all conversations
- GET /v1/messages/conversations/get/{userId}/ – get conversation with user
- PUT /v1/messages/conversations/read/{userId} – mark as read
- DELETE /v1/messages/{id} – delete message

### 💬 Chat

- SEND /app/chat – send message
- SUBSCRIBE /topic/messages – receive messages (broadcast)

---

## 🔄 Inter-Service Communication

Microservices communicate using **Feign Client**.

Example:
```java
@FeignClient(name = "chat-service", url = "${services.chat-service.url}")
public interface ChatClient {

    @GetMapping("/v1/messages/{id}")
    ChatResponseDto getChat(@PathVariable("id") Long id);
}
```

### How it works:
- One service (e.g., User Service) calls another (Chat Service)
- Feign automatically performs the HTTP request
- JWT token can be propagated using an interceptor

---

## 📁 Project Structure

```
project-root/
│
├── auth-service/
│   ├── config/
│   ├── controller/
│   ├── entity/
│   ├── security/
│   └── repository/
│
├── user-service/
│   ├── config/
│   ├── controller/
│   ├── entity/    
│   ├── repository/ 
│   ├── service/      
│   └── mapper/
│
├── chat-service/
│   ├── config/
│   ├── controller/
│   ├── entity/
│   ├── kafka/     
│   ├── repository/ 
│   ├── service/   
│   ├── websocket/     
│   └── mapper/
│
└── common/ (optional)
    └── dto/
```

---

## ⚙️ Feign + JWT Configuration

To propagate JWT tokens between services:

```java
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + getToken());
        };
    }
}
```

---

## 📌 Additional Notes

- Each service can have its own database
- API Gateway can be added
- Service Discovery (e.g., Eureka) can be integrated

---
