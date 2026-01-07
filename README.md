# Loan Management System

A simple backend **Loan Management System** built using **Java Servlets and JDBC**.  
In this project, I have implemented customer management, loan management, and a basic login functionality

---

## Tech Stack
- Java (Servlets)
- JDBC
- MySQL
- Maven
- Apache Tomcat

---

## Project Structure
- **Servlets** – Handle HTTP requests
- **Services** – Business logic
- **DAOs** – Database operations using JDBC
- **MySQL** – Data storage

---

## Features

### Customer
- Create customer
- Get all customers
- Get customer by ID
- Update customer
- Delete customer

### Loan
- Create loan
- Get all loans
- Get loan by ID
- Update loan
- Delete loan

### Authentication
- Login using username and password
- Token generation after successful login

---

## API Endpoints

### Customer APIs
- POST `/customer`
- GET `/customer`
- GET `/customer?id=1`
- PUT `/customer`
- DELETE `/customer?id=1`

### Loan APIs
- POST `/loan`
- GET `/loan`
- GET `/loan?id=1`
- PUT `/loan`
- DELETE `/loan?id=1`

### Login API
- POST `/api/login`
- During login,a token is generated and stored in the database with the coressponding customer_id.for every request,the system fetch the customer_id by querying the auth_token table using the token

---

## Sample Requests

Sample customer request JSON:
```json
{
  "customerCode": "CUST001",
  "name": "Sathesh",
  "email": "sathesh@gmail.com",
  "mobile": "9876543210",
  "address": "Chennai",
  "kycStatus": "VERIFIED"
}

Sample loan request JSON:
```json
{
  "customerId": 1,
  "loanType": "PERSONAL_LOAN",
  "principalAmount": 500000,
  "interestRate": 10.5,
  "tenureMonths": 24
}
Sample login request JSON:
```json
{
  "username": "sathesh",
  "password": "Admin@123"
}
Sample login response JSON
```json
{
  "token": "generated-auth-token"
}




