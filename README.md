# Java-Clinic-Appointment-System
Practice project for connecting Java to MySQL. Features include booking, viewing, and managing clinic appointments.

# Clinic Management System (JDBC Practice)

## What is this?

Just practicing how to connect Java to MySQL. Made a clinic management system because I needed *something* to store in a database.

**Main goal:** Learn JDBC (Java Database Connectivity)  
**Side goal:** Pass my OOP class  
**Actual result:** Spaghetti code that works ¯\_(ツ)_/¯

## How to run

1. Install MySQL
2. Run the SQL file in `database/` folder
3. Change password in `DatabaseConnection.java` to your MySQL password
4. Add MySQL Connector JAR to build path
5. Run it

Login: `admin` / `admin123`

## What matters here

Check `DatabaseConnection.java` - that's where the JDBC stuff happens:
- Loading the MySQL driver
- Creating connections
- Running SQL queries
- Converting results to Java objects

Everything else is just OOP stuff to make it look like a real project.

## Tech
- Java
- MySQL
- JDBC
- Lots of copy-pasting

## Status
Works on my machine ✅

## Warning
- No security (passwords in plain text)
- No validation (will crash on bad input)
- No tests (YOLO)
- Vibe coded (don't judge)

Made for learning, not for production!

---

**If you're here to learn JDBC:** Focus on `DatabaseConnection.java` and how it talks to MySQL.  
**If you're here to copy for your project:** Good luck explaining the code 😂
