Advanced Library Management System

This is my Java Swing project for a complete Library Management System. It's a two-sided desktop app (for Librarians and Users) that connects to a MySQL database using JDBC.

The main goal of this project wasn't just to make a functional app, but to build it like a professional engineer. I focused on advanced Object-Oriented Design (OOD), a clean 3-tier architecture, and common Design Patterns.

🚀 My Engineering Focus: A 3-Tier Design

I built this on a strict Separation of Concerns model, meaning every part of the app has one job and doesn't interfere with the others.

The GUI Layer (Presentation):

These are all my JFrame windows, like Login.java, LibrarianDashboard.java, and UserDashboard.java.

My most important rule for this layer was: Zero SQL code. The GUI's only job is to show buttons and text fields, and then call the "engine" when one is clicked.

The "Engine" (Data Access Layer):

This is my LibraryDatabase.java file. It's the only class in the entire project that is allowed to write and run SQL queries.

It manages all the INSERT, SELECT, and UPDATE commands safely using PreparedStatements to prevent SQL injection.

The "Brains" (Business Logic Layer):

These are the classes like User.java, Book.java, Account.java, and the BorrowStrategy files.

They sit in the middle and enforce all the library's rules (like "is this user allowed to borrow this book?").

✨ Cool Design Patterns I Used

I implemented two major design patterns that are common in professional software:

1. The Singleton Pattern

I used the Singleton Pattern for my LibraryDatabase.java class.

Why? In simple terms, this makes sure my entire app shares one single connection to the database. Instead of every window opening a new connection (which is slow and wastes resources), everyone just asks the Singleton for the one that's already open. It's much more efficient.

2. The Strategy Pattern

This was my favorite part. I used the Strategy Pattern to handle the different borrowing rules for different types of users.

The Problem: A Student can only borrow 5 books, but a Staff member can borrow 10.

The "Bad" Way: I could have written a big, messy if/else block inside my "Check Out" code.

The "Strategy" Way: I created a BorrowStrategy interface (a "rule book" contract) and then made two separate "rule book" classes: StudentBorrowStrategy and StaffBorrowStrategy.

The Result: When a user logs in, I just hand them the correct "rule book." If my teacher ever asks to add a new "Professor" role with a 20-book limit, I just create one new ProfessorStrategy.java file. I never have to touch the original, working checkout code. This is a great example of the Open/Closed Principle (OCP).

🛠️ How to Run

Database: You'll need a MySQL server. Just create one empty database (schema) named library_db.

CREATE DATABASE library_db;


Connector: Make sure you add the mysql-connector-j.jar file to your project's dependencies in IntelliJ (or your IDE).

Run: Execute the main method in src/Login.java. The app will run, automatically connect to the database, and create all the necessary tables (books, users, librarians) for you.

🔑 Default Credentials

Librarian (Admin):

ID: admin

Password: admin123

User:

There are no default users! You must click the "Register" button on the login screen to create a new Student or Staff user first. This was done to show the full user-registration workflow.
