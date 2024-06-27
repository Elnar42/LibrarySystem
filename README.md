# Library Management System

## Overview

The Library Management System is a Java application designed to facilitate efficient management of library resources including users, books, transactions, and reports.

## Features

### 1. User Registration and Login

- Implements user registration with unique `userID`.
- Provides authentication and authorization using roles (`Librarian`, `Member`).

### 2. User Management

- Methods to add, delete, and update user accounts.
- Ensures unique `userID` for each user.

### 3. Book Management

- Manages book collection with attributes such as `bookID`, `title`, `author`, etc.
- Implements CRUD operations for books and ensures unique `bookID`.

### 4. Borrowing and Returning Books

- Tracks book availability and borrower information.
- Methods for borrowing and returning books, updating availability.

### 5. Transaction Management

- Records transactions including `transactionID`, `userID`, `bookID`, etc.
- Displays transaction history and ensures unique `transactionID`.

### 6. Searching and Sorting

- Implements search functionality for books and users based on various attributes.
- Sorting functionality for books and users.

### 7. Generating Reports

- Generates reports such as borrowed books, overdue books, user transactions, etc.

### 8. File I/O for Data Persistence

- Saves and loads data (books, users, transactions) using file operations.
- Ensures persistence between program executions using appropriate file formats.

### 9. Exception Handling

- Uses custom exceptions (`BookNotAvailableException`, `UserNotFoundException`) to manage errors.
- Provides meaningful feedback and prevents unexpected crashes.

### 10. Favorites Feature

- Allows users to add and display favorite books.
- Ensures favorites list persistence across sessions.

### 11. Interactive Menu

- Text-based menu system for easy navigation and operation selection.
- Provides clear instructions for user interaction.

### 12. GIT Integration

- Version control using GIT with proper commit history, branching, and merging.

## Additional Features

### Reservation System

- Allows users to reserve books and manages reservation queues.

### Late Fee Calculation

- Calculates and applies late fees for overdue books, with a method to pay fees.

## Deliverables

1. **Source Code**
   - Complete, well-documented Java source code adhering to coding standards.

2. **Documentation**
   - Comprehensive report including project overview, class diagrams, flowcharts, code explanations, and instructions to run the program.

3. **GIT Repository**
   - Link to the GIT repository demonstrating version control practices.

## Evaluation Criteria

- **Functionality (40%)**: Adherence to requirements and effectiveness of implemented features.
- **Code Quality (20%)**: Readability, best practices, and documentation within the code.
- **Exception Handling (10%)**: Proper use of exceptions to manage errors gracefully.
- **Use of OOP Concepts (20%)**: Effective application of object-oriented principles.
- **GIT Usage (10%)**: Proper version control practices with a clear commit history.

## Deadline

- **1 week** to complete the project and provide the deliverables.
