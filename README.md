# Task Management System 

A simple task management system built with Spring Boot and in-memory storage.

This system allows users to create, update, delete, and list tasks with flexible filtering options.

---

## 🧱 Features

### Core Functionality

- **Create** a task with title, description, due date, priority, and status
- **Update** any modifiable field of a task
- **Delete** a task by ID
- **List** all tasks with filters:
    - By Status (`PENDING`, `IN_PROGRESS`, `COMPLETED`)
    - By Priority (`LOW`, `MEDIUM`, `HIGH`)
    - By Due Date Range (`fromDate`, `toDate`)

### Additional Features

- In-memory data storage (no persistence required)
- RESTful API interface
- Comprehensive unit tests
- Interactive API documentation via **Swagger UI**

---

## ⚙️ Technologies Used

| Technology                | Purpose                        |
|---------------------------|-------------------------------|
| Java 17+                  | Language                      |
| Spring Boot               | Web framework & DI            |
| Maven                     | Build tool                    |
| JUnit 5 + Mockito         | Unit testing                  |
| Swagger UI (SpringDoc)    | API documentation & testing   |

---

## 📦 Package Structure

<details>
<summary>Click to expand</summary>

```
sprih/
├── README.md
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com.example.sprih/
│   │           ├── SprihApplication.java
│   │           ├── controller/
│   │           │   └── TaskController.java
│   │           ├── service/
│   │           │   └── TaskService.java
│   │           ├── repository/
│   │           │   └── InMemoryTaskRepository.java
│   │           ├── model/
│   │           │   ├── Task.java
│   │           │   ├── Status.java
│   │           │   └── Priority.java
│   │           └── exception/
│   │               ├── TaskNotFoundException.java
│   │               └── InvalidTaskException.java
│   └── test/
│       └── java/
│           └── com.example.sprih/
│               ├── controller/
│               │   └── TaskControllerTest.java
│               ├── service/
│               │   └── TaskServiceTest.java
│               └── SprihApplicationTests.java
└── target/
```
</details>

---

## 🛠️ How to Run

### Prerequisites

- Java 17+
- Maven

### Steps

1. **Clone the project**
     ```bash
     git clone https://github.com/sudhanshu2070/sprih-assign.git
     cd sprih
     ```

2. **Build the project**
     ```bash
     mvn clean install
     ```

3. **Run the application**
     ```bash
     mvn spring-boot:run
     ```

4. **Access the app**
     - API endpoint: [http://localhost:8080/api/tasks](http://localhost:8080/api/inMemory/tasks)

---

## 📘 Swagger UI (Interactive API Docs)

Once the app is running, open:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧪 Unit Tests

All core functionality is covered by unit tests.

**Covered Scenarios:**
- Creating a task with valid/invalid input
- Reading a task by ID (and handling missing tasks)
- Updating a task (and invalid updates)
- Deleting a task (and non-existent ones)
- Filtering by multiple criteria

**To run tests:**
```bash
mvn test
```