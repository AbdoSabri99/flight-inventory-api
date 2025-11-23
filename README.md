# Flight Inventory API

A **Spring Boot REST API** for managing airline flights, with JWT authentication, role-based access control, and well-formatted JSON responses for security errors.

---

## 📋 Table of Contents

- [Getting Started](#getting-started)
- [User Roles and Test Users](#user-roles-and-test-users)
- [API Endpoints](#api-endpoints)
- [Error Responses](#error-responses)
- [Security](#security)
- [Testing](#testing)

---

## 🚀 Getting Started

### Clone the repository

```bash
git clone https://github.com/AbdoSabri99/flight-inventory-api.git
cd flight-inventory-api
```

### Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

---

## 👥 User Roles and Test Users

| Username | Password   | Role  |
|----------|------------|-------|
| admin    | admin123   | ADMIN |
| user     | user123    | USER  |

- **Admin** can create and delete flights
- **User** can only view flights

> Passwords are encoded using `BCryptPasswordEncoder`.

---

## 🛠️ API Endpoints

### Authentication

#### Login

```http
POST /api/auth/login
```

**Request Body:**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**

```json
{
  "accessToken": "jwt-token"
}
```

---

### Flights API

> **Note:** All endpoints require the JWT `Authorization` header:  
> `Authorization: Bearer <accessToken>`

#### Get all flights

```http
GET /api/flights
```

- **Roles:** USER, ADMIN
- **Response:** List of flights

#### Add a flight

```http
POST /api/flights
```

- **Roles:** ADMIN only

**Request Body:**

```json
{
  "flightNumber": "AF123",
  "origin": "Paris",
  "destination": "New York",
  "departureTime": "2025-12-01T10:00:00",
  "arrivalTime": "2025-12-01T14:00:00"
}
```

#### Delete a flight

```http
DELETE /api/flights/{id}
```

- **Roles:** ADMIN only

---

## ⚠️ Error Responses

### 401 Unauthorized (JWT missing/invalid)

```json
{
  "error": "Unauthorized",
  "message": "JWT token has expired",
  "timestamp": "2025-11-23T14:50:00",
  "path": "/api/flights"
}
```

### 403 Forbidden (Role not allowed)

```json
{
  "error": "Forbidden",
  "message": "You do not have permission to perform this action.",
  "timestamp": "2025-11-23T14:51:00",
  "path": "/api/flights"
}
```

### Controller exceptions (400/500)

```json
{
  "error": "BAD_REQUEST",
  "message": "Invalid input value",
  "timestamp": "2025-11-23T14:55:00"
}
```

---

## 🔒 Security

- JWT validation handled in `JwtAuthFilter`
- Authentication errors handled in `JwtAuthenticationEntryPoint` (401 JSON)
- Authorization errors handled in `JwtAccessDeniedHandler` (403 JSON)
- Method-level security via `@PreAuthorize` for roles

---

## 🧪 Testing

1. **Login** as admin/user via `/api/auth/login`
2. **Use returned JWT** in Postman `Authorization` header
3. **Call flights API endpoints** depending on role
4. **Verify 401/403 JSON responses** when JWT is missing/invalid or user role is insufficient

---
**Made with ❤️ using Spring Boot**
