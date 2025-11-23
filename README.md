# Flight Inventory API

A **Spring Boot REST API** for managing airline flights, with JWT authentication, role-based access control, and well-formatted JSON responses for security errors.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Getting Started](#getting-started)
5. [User Roles and Test Users](#user-roles-and-test-users)
6. [API Endpoints](#api-endpoints)
7. [Error Responses](#error-responses)
8. [Security](#security)
9. [Testing](#testing)

---

## Project Overview

The Flight Inventory API allows users to:

- List available flights
- Add, update, or delete flights (admin only)
- Authenticate using JWT tokens
- Receive structured JSON error responses for authentication and authorization errors

---

## Features

- JWT authentication (access tokens)
- Role-based access (`ADMIN` and `USER`)
- Method-level security using `@PreAuthorize`
- Well-formatted JSON error responses for:
  - Invalid/missing JWT → 401 Unauthorized
  - Unauthorized roles → 403 Forbidden
  - Controller exceptions → 400/500
- Spring Boot 3.x, Spring Security, and Spring Data JPA

---

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA / Hibernate
- H2 (in-memory DB for testing) or any relational DB
- Maven

---

## Getting Started

### Prerequisites
- Java 17
- Maven
- IDE (IntelliJ, Eclipse, VS Code)
- Postman or similar tool for API testing

### Clone the repository
```bash
git clone <your-repo-url>
cd flight-inventory-api

Build and run
mvn clean install
mvn spring-boot:run


The application will start on http://localhost:8080 by default.

User Roles and Test Users
Username	Password	Role
admin	admin123	ADMIN
user	user123	USER

Admin can create, delete, and update flights

User can only view flights

Passwords are encoded using BCryptPasswordEncoder.

API Endpoints
Authentication
Login
POST /api/auth/login


Request Body:

{
  "username": "admin",
  "password": "admin123"
}


Response:

{
  "accessToken": "jwt-token",
  "username": "admin",
  "roles": ["ADMIN"]
}

Flights API

All endpoints require the JWT Authorization header:
Authorization: Bearer <accessToken>

Get all flights
GET /api/flights


Roles: USER, ADMIN

Response: List of flights

Add a flight
POST /api/flights


Roles: ADMIN only

Request Body:

{
  "flightNumber": "AF123",
  "origin": "Paris",
  "destination": "New York",
  "departureTime": "2025-12-01T10:00:00",
  "arrivalTime": "2025-12-01T14:00:00"
}

Delete a flight
DELETE /api/flights/{id}


Roles: ADMIN only

Error Responses

401 Unauthorized (JWT missing/invalid)

{
  "error": "Unauthorized",
  "message": "JWT token has expired",
  "timestamp": "2025-11-23T14:50:00",
  "path": "/api/flights"
}


403 Forbidden (Role not allowed)

{
  "error": "Forbidden",
  "message": "You do not have permission to perform this action.",
  "timestamp": "2025-11-23T14:51:00",
  "path": "/api/flights"
}


Controller exceptions (400/500)

{
  "error": "BAD_REQUEST",
  "message": "Invalid input value",
  "timestamp": "2025-11-23T14:55:00"
}

Security

JWT validation handled in JwtAuthFilter

Authentication errors handled in JwtAuthenticationEntryPoint (401 JSON)

Authorization errors handled in JwtAccessDeniedHandler (403 JSON)

Method-level security via @PreAuthorize for roles

Testing

Login as admin/user via /api/auth/login

Use returned JWT in Postman Authorization header

Call flights API endpoints depending on role

Verify 401/403 JSON responses when JWT is missing/invalid or user role is insufficient
