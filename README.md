# SkillSwap – A Microservices-Based Skill Exchange Platform

SkillSwap is a scalable microservices-based platform that allows users to exchange skills using a secure credit-based system. The platform is built with Spring Boot, secured with JWT authentication via an API Gateway, and designed using modern cloud-native microservices architecture.

---

## 🧠 Core Concept

Users can:
- Register and authenticate securely
- Create and manage their skills
- Earn and spend credits
- Exchange skills with other users
- Manage their profiles and transactions

The system is designed to be **modular, scalable, and production-ready** using industry best practices.

---

## 🏗️ Architecture Overview

The project follows a **distributed microservices architecture**:

- **Config Server** – Centralized configuration management
- **Eureka Server** – Service discovery
- **API Gateway** – Single entry point, JWT validation, request routing
- **User Service** – Authentication, registration, user profiles
- **Skill Service** – User skills management
- **Credits Service** – Wallet & credit management
- **(Upcoming)** Exchange Service – Skill exchange & requests
- **(Upcoming)** Review & Rating Service
- **(Upcoming)** Notification & Chat Services

All services communicate through REST APIs and are registered with Eureka.

---

## 🔐 Security Design

- JWT-based authentication
- Authentication handled centrally by the **API Gateway**
- Gateway injects:
  - `X-Authenticated-User`
- Internal microservices do **not** expose authentication logic

---

## 🐳 Infrastructure

- Each microservice has its **own PostgreSQL database**
- Databases are managed using **Docker**
- Configuration is stored in a **local Git-backed Config Server**
- Services automatically load their configuration at startup

---

## ⚙️ Technologies Used

- Java 17
- Spring Boot 3.3.x
- Spring Cloud (Gateway, Eureka, Config Server)
- Spring Security + JWT
- PostgreSQL 15
- Docker & Docker Compose
- Maven
- RESTful APIs

---

## 🚀 Microservices & Ports

| Service | Port |
|--------|------|
| Config Server | 8888 |
| Eureka Server | 8761 |
| API Gateway | 8080 |
| User Service | 8081 |
| Skill Service | 8082 |
| Credits Service | 8083 |

---

## 📦 Implemented Features

### ✅ User Service
- User registration & login
- Password encryption (BCrypt)
- JWT token generation
- User profile management

### ✅ Skill Service
- Skill creation, update, deletion
- Retrieve skills by user
- Search and listing of skills

### ✅ Credits Service
- Wallet creation per user
- Credit top-up
- Credit transfer between users
- Credit charge mechanism
- Transaction history

### ✅ API Gateway
- Centralized authentication
- JWT validation
- Route management
- Header injection for internal services

---

## 🧪 How to Run the Project

1. Start Docker:
```bash
docker-compose up -d
```
2. Start services in order:
- Config Server
- Eureka Server
- API Gateway
- User Service
- Skill Service
- Credits Service
- Check Eureka:
3. Check Eureka: http://localhost:8761
4. Access APIs only through Gateway: http://localhost:8080
# 👨‍💻 Author
Developed by: [Ilhem Cherif]
Role: Full Stack Engineer
Focus: Microservices, Spring Boot, Cloud Architecture