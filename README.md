Job Search Backend Application

This is the backend API for the Job Search application, which handles job listings, applications, user management, and more. The project is built using Spring Boot, MongoDB, and JWT-based authentication.
Table of Contents

    Technologies
    Installation
    Configuration
    Running the Application
    API Documentation
    Running Tests

Technologies

    Java: Version 17 or above
    Spring Boot: 3.x.x
        Spring Security (JWT Authentication)
        Spring Data MongoDB
        Spring Web
    MongoDB: NoSQL database
    JWT: JSON Web Tokens for authentication
    Maven: For dependency management
    JUnit: For testing

Installation
Prerequisites

    Java 17 or higher
    Maven (3.x.x)
    MongoDB (installed locally or use a cloud instance like MongoDB Atlas)

Clone the Repository

bash

git clone https://github.com/your-username/jobsearch-backend.git
cd jobsearch-backend

Install Dependencies

bash

mvn clean install

MongoDB Setup

Make sure you have a running MongoDB instance, either locally or using a cloud service like MongoDB Atlas. The connection settings can be configured in the application.properties or application.yml file.
Configuration

Edit the application.yml file to configure MongoDB and JWT settings:

yaml

# application.yml

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/jobsearch
  security:
    jwt:
      secret: your_jwt_secret_key
      expirationMs: 86400000 # Token validity in milliseconds (24 hours)

# Additional properties...

JWT Settings

    secret: JWT secret key for token signing.
    expirationMs: Expiration time for JWT tokens.

Environment Variables (Optional)

You can also configure these properties using environment variables:

bash

export SPRING_MONGODB_URI=mongodb://localhost:27017/jobsearch
export JWT_SECRET=your_jwt_secret_key

Running the Application

Once all configurations are set, you can run the application using Maven:

bash

mvn spring-boot:run

The application will start on the default port 8080.
Default Endpoints

    Authentication: /api/auth/**
    Job Listings: /api/jobs/**
    Job Applications: /api/applications/**
    Bookmarks: /api/bookmarks/**
    Swagger UI: /swagger-ui/**
    OpenAPI Documentation: /v3/api-docs/**

API Documentation

The application uses Swagger for API documentation. Once the application is running, you can access the Swagger UI at:

bash

http://localhost:8080/swagger-ui/index.html

This UI allows you to interact with the API and test its functionality.
Running Tests

Unit tests and integration tests are included for controllers, services, and repositories. To run the tests, use:

bash

mvn test

Testing Layers

    Controller Tests: Test the REST APIs using MockMvc.
    Service Tests: Test business logic with mock dependencies.
    Repository Tests: Test MongoDB queries.

Example of running tests for a specific class:

bash

mvn -Dtest=ApplicationServiceTest test

Test Tools Used:

    JUnit 5: For unit and integration testing.
    Mockito: For mocking dependencies.
    MockMvc: For testing the controller layer.
