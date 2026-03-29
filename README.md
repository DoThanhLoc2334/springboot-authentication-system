# 🔐 Spring Boot Authentication System

## 📌 Overview

This project is a full-stack authentication and product management system built with:

* **Backend:** Spring Boot (Java)
* **Frontend:** React (Vite)
* **Authentication:** JWT (JSON Web Token)
* **Database:** (configure in `application.properties`)

The system provides secure authentication, role-based access control, and product/category management.

---

## 🧱 Project Structure

```
springboot-authentication-system/
│
├── backend/                 # Spring Boot backend
│   ├── src/main/java/com/dtl/springboot_auth_system
│   │   ├── config/          # Security configuration
│   │   ├── controller/      # REST APIs
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── exception/       # Global exception handling
│   │   ├── model/           # Entities
│   │   ├── repository/      # JPA repositories
│   │   ├── security/        # JWT & security logic
│   │   ├── service/         # Business logic
│   │   └── seeder/          # Initial data seeding
│   ├── resources/
│   │   ├── application.properties
│   │   └── data.sql
│   └── pom.xml
│
├── frontend/               # React frontend (Vite)
│   ├── src/
│   │   ├── api/            # API calls
│   │   ├── components/     # Shared components
│   │   ├── pages/          # App pages
│   │   ├── App.jsx
│   │   └── main.jsx
│   └── package.json
│
└── README.md
```

---

## 🚀 Features

### 🔑 Authentication

* User registration
* User login with JWT
* Secure API with token
* Role-based authorization (USER, ADMIN)

### 📦 Product Management

* Create, read products
* Category management
* Protected routes for admin

### 🛡 Security

* JWT Authentication Filter
* Custom UserDetailsService
* Spring Security configuration
* Global exception handling

---

## ⚙️ Backend Setup (Spring Boot)

### 1. Navigate to backend

```bash
cd backend
```

### 2. Run project

```bash
./mvnw spring-boot:run
```

> Nếu Windows:

```bash
mvnw.cmd spring-boot:run
```

### 3. Database config

Edit file:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/soundwave
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```

---

## 💻 Frontend Setup (React + Vite)

### 1. Navigate to frontend

```bash
cd frontend
```

### 2. Install dependencies

```bash
npm install
```

### 3. Run app

```bash
npm run dev
```

App will run at:

```
http://localhost:5173
```

---

## 🔗 API Endpoints (Example)

### Auth

* `POST /api/auth/register`
* `POST /api/auth/login`

### Products

* `GET /api/products`
* `POST /api/products` (Admin only)

### Categories

* `GET /api/categories`

---

## 🔐 JWT Flow

1. User login
2. Server returns JWT token
3. Frontend stores token (localStorage)
4. Token attached in request header:

```
Authorization: Bearer <token>
```

---

## 🧪 Testing

Run backend tests:

```bash
./mvnw test
```

---

## 📦 Build

### Backend:

```bash
./mvnw clean install
```

### Frontend:

```bash
npm run build
```

---

## 📌 Notes

* Make sure MySQL is running before starting backend
* Configure CORS if frontend cannot connect
* Token expires based on JWT config

---

## 👨‍💻 Author

* Developed as a learning project for Spring Boot + React + JWT Authentication

---

## ⭐ Future Improvements

* Refresh Token
* Pagination & filtering
* Upload image for products
* Docker deployment
* UI/UX improvements

---
