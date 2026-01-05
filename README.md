# Loan Management System

This is a simple backend Loan Management System developed using Java Servlets and JDBC.  
The purpose of this project is to understand how backend applications work without using frameworks like Spring Boot or JPA.

In this project, I have implemented customer management, loan management, and a basic login functionality.

Customer details such as name, email, mobile number, address, and KYC status are stored in the customer table.  
Loan details such as loan type, amount, interest rate, tenure, and status are stored in the loan table.  
Each loan is linked to a customer using customer_id.

Login details are stored in a separate table called user_credential.  
This table contains username, password, and customer_id.  
Authentication data is kept separate from business data for better clarity.

When a user logs in using username and password, the details are validated from the user_credential table.  
If the login is successful, a token is generated and stored in the auth_token table.  
This token can be used later to secure APIs.

The project follows a simple layered structure:
Servlets handle HTTP requests,
Service classes contain business logic,
DAO classes handle database operations using JDBC,
Data is stored in a MySQL database.

The application supports basic CRUD operations.

Customer operations include:
Create customer, fetch all customers, fetch customer by ID, update customer, and delete customer.

Loan operations include:
Create loan, fetch all loans, fetch loan by ID, update loan, and delete loan.

The APIs exposed in this project are:

Customer APIs:
POST /customer  
GET /customer  
GET /customer?id=1  
PUT /customer  
DELETE /customer?id=1  

Loan APIs:
POST /loan  
GET /loan  
GET /loan?id=1  
PUT /loan  
DELETE /loan?id=1  

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

{
  "token": "generated-auth-token"
}



