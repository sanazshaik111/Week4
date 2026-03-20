# Spring Boot Demo 2

## Overview
Spring Boot Demo 2 is a simple RESTful web application built using **Spring Boot**.  
The project is designed to demonstrate core backend concepts such as REST APIs, controllers, services, and basic object‑oriented design using Java.

This demo focuses on managing video content (such as Movies and Series) and exposes HTTP endpoints that can be tested using tools like Postman or curl.

---

## Tech Stack
- **Java**
- **Spring Boot**
- **Spring Web (REST APIs)**
- **Gradle**
- **Docker**
- **Postman / curl** (for testing APIs)

---

## Test Endpoints with Postman
Test Endpoints

GET localhost:8080/api/videos
PUT localhost:8080/api/videos/Inception/rent
PUT localhost:8080/api/videos/Inception/return
GET localhost:8080/api/videos/available

POST localhost:8080/api/videos/add/movie

~ to add movie contents ~ 

Key: Content-Type
Value: application/json

Body input:

{
  "title": "example_movie",
  "genre": "Sci-Fi"
}

---

## Docker Commands:
docker-compose down -v   // to stop running active containers
.\gradlew.bat clean bootJar
docker-compose up --build 

---

## Run Tests:
.\gradlew.bat clean test