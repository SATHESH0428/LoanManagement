# Loan Management System

A backend **Loan Management System** built using **Pure Java, Servlets, and JDBC**.  
This project is developed without using **Spring Boot or JPA** to understand core backend concepts clearly.

---

## Project Description

This project focuses on building a simple loan management backend application using low-level Java technologies.  
It helps in understanding how REST APIs, database access, and business logic work internally.

The system allows managing **customers** and their **loans** with basic CRUD operations.

---

## Technologies Used

- Java 17
- Java Servlets
- JDBC
- MySQL
- Maven
- Liquibase
- Jackson
- JUnit 5
- Mockito
- SLF4J & Logback
- Apache Tomcat 9

---

## Project Structure
Controller (Servlet)
|
Service Layer
|
DAO Layer
|
Models
|
MySQL Database


---

## Features

### Customer Management
- Create customer
- Fetch all customers
- Fetch customer by ID
- Update customer details
- Delete customer

### Loan Management
- Create loan
- Fetch all loans
- Fetch loan by ID
- Update loan details
- Delete loan

---

## API Endpoints

### Customer APIs
POST /customer
GET /customer
GET /customer?id=1
PUT /customer
DELETE /customer?id=1

### Loan APIs
POST /loan
GET /loan
GET /loan?id=1
PUT /loan
DELETE /loan?id=1

---

## Sample Request JSON

### Customer
```json
{
  "customerCode": "CUST001",
  "name": "Sathesh",
  "email": "sathesh@gmail.com",
  "mobile": "9876543210",
  "address": "Chennai",
  "kycStatus": "VERIFIED"
}
| Column        | Type      |
| ------------- | --------- |
| id            | BIGINT    |
| customer_code | VARCHAR   |
| name          | VARCHAR   |
| email         | VARCHAR   |
| mobile        | VARCHAR   |
| address       | VARCHAR   |
| kyc_status    | VARCHAR   |
| created_date  | TIMESTAMP |
### Loan
```json

{
  "customerId": 1,
  "loanType": "PERSONAL",
  "principalAmount": 500000,
  "interestRate": 12.5,
  "tenureMonths": 36
}
| Column        | Type      |
| ------------- | --------- |
| id            | BIGINT    |
| customer_code | VARCHAR   |
| name          | VARCHAR   |
| email         | VARCHAR   |
| mobile        | VARCHAR   |
| address       | VARCHAR   |
| kyc_status    | VARCHAR   |
| created_date  | TIMESTAMP |

Database Migration

Liquibase is used for database schema management.

src/main/resources/db/changelog/db.changelog-master.xml

