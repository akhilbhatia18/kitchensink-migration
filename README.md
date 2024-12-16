# kitchensink-migration

---

# **KitchenSink Application**

The **KitchenSink** project is a **Spring Boot** application that showcases various functionalities, including user registration, data validation, exception handling, and database interactions. It serves as an example of how to develop a robust and scalable web application using modern Spring Boot practices.

This application was initially a JBoss Jakarta EE application but has been **migrated to Spring Boot** for better maintainability, modern features, and broader developer adoption. The migration process included transforming the application to use Spring's ecosystem, replacing the Jakarta EE components, and configuring the project to use a **MongoDB database**.

---

## **Key Features**
- **Registration**: Users can register with validated fields like name, email, and phone number.
- **Validation**: Implements input validations using `javax.validation` annotations (e.g., `@NotNull`, `@Email`, `@Size`).
- **Database Interactions**:
    - Migrated to **MongoDB** as the primary database for runtime.
- **Exception Handling**: Exception handler ensures user-friendly error responses.
- **Testing**:
    - Unit tests created with **JUnit** and **Mockito**.
    - Integrated test cases for services and controllers.

---

## **Project Structure**
The project follows a typical Spring Boot directory structure:
```
src
├── main
│   ├── java
│   │   └── com.quickstarts.kitchensink
│   │       ├── configuration        # Application configurations (e.g., security, MongoDB)
│   │       ├── controller    # REST API controllers
│   │       ├── model         # Data models (entities/documents)
│   │       ├── repository    # Spring Data repositories for database operations
│   │       └── service       # Business logic and services
│   ├── resources
│       ├── application.properties   # Configuration file
├── test
    └── java/com/quickstarts/kitchensink
        └── Unit tests
```

---

## **Technologies Used**
- **Spring Boot**: Core framework for application setup and configuration.
- **Spring Data JPA Mongo**: Used for database operations with the Mongo database.
- **MongoDB**: Primary database after migration from H2.
- **Spring MVC**: Web layer for handling RESTful APIs.
- **JUnit** and **Mockito**: Testing framework for unit and integration tests.
- **Lombok**: Reduces boilerplate code (e.g., getters/setters, logging).
- **Maven**: Build and dependency management tool.

---

## **Setup Instructions**

### **1. Prerequisites**
- **Java 21**: Ensure Java 21 is installed.
- **MongoDB**: Install and start MongoDB on your local machine or use a hosted instance.
- **Maven**: Ensure Maven is installed for building the project.

### **2. Clone the Repository**
```bash
git clone https://github.com/<your-username>/kitchensink-migration.git
cd kitchensink-migration
```

### **3. Configure the Application for Mongo**
```bash
   docker-compose up
```

### **4. Build and Run the Application**
1. Build the project using Maven:
   ```bash
   mvn clean install
   ```
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8085`.

### **5. Access the APIs**
- **Registration Endpoint**:
  ```bash
  POST http://localhost:8085/api/members
  Content-Type: application/json
  Body:
  {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "phoneNumber": "1234567890"
  }
  ```
- **Validation**: If the input is invalid, appropriate error messages will be returned.

---

## **Testing**
Run all tests with:
```bash
mvn test
```

### **Unit Tests**
Tests the services and components using **Mockito** for mocking dependencies.
---

## **Future Enhancements**
- Implement OAuth2 authentication for advanced security.
- Add Docker support for containerized deployment.
- Extend test cases to cover additional scenarios.

---

## **Contributing**
1. Fork the repository.
2. Create a new branch (`feature/your-feature`).
3. Commit your changes and create a pull request.

---