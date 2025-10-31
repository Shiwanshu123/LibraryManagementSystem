import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for handling the database connection.
 * This is the ONLY class that knows the database URL, user, and password.
 * This is part of our "Data Access Layer."
 */
public class DatabaseConnector {

    // --- Database Credentials ---
    // These are the only lines you should ever have to change.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "library_db";
    private static final String USER = "root";
    // *** IMPORTANT: Change this to your MySQL password ***
    private static final String PASS = "Shiwanshu@123";
    // The full connection string
    private static final String FULL_DB_URL = DB_URL + DB_NAME;
    // The driver class name
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";


    /**
     * Attempts to establish a connection to the database.
     * @return A Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. Register the JDBC driver
            Class.forName(JDBC_DRIVER);

            // 2. Open a connection
            System.out.println("Connecting to database: " + DB_NAME + "...");
            conn = DriverManager.getConnection(FULL_DB_URL, USER, PASS);
            System.out.println("Connection successful.");

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Did you add the .jar file to the project?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed.");
            System.err.println("Make sure MySQL is running and the database '" + DB_NAME + "' exists.");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * This method creates all the necessary tables if they don't already exist.
     * This is the "missing" method that 'LibraryManagementSystem' needs.
     */
    public static void createTables() {
        // SQL statements to create tables.
        // "IF NOT EXISTS" makes this safe to run even if the tables are already there.

        // AUTO_INCREMENT is used for primary keys in a more advanced setup
        // Here, we'll rely on user-provided IDs

        String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
                + "isbn VARCHAR(13) PRIMARY KEY,"
                + "title VARCHAR(255) NOT NULL,"
                + "author VARCHAR(255) NOT NULL,"
                + "publication VARCHAR(255),"
                + "is_available BOOLEAN DEFAULT TRUE,"
                + "due_date DATE,"
                + "borrower_id VARCHAR(50)" // This will link to the users table
                + ");";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id VARCHAR(50) PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "password VARCHAR(100) NOT NULL," // Should be hashed in a real app
                + "user_type VARCHAR(20) NOT NULL,"  // "student" or "staff"
                + "detail VARCHAR(255),"             // "Class: 5" or "Dept: CSE"
                + "fine_amount DOUBLE DEFAULT 0.0"
                + ");";

        String createLibrariansTable = "CREATE TABLE IF NOT EXISTS librarians ("
                + "id VARCHAR(50) PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "password VARCHAR(100) NOT NULL" // Should be hashed
                + ");";

        // We use a "try-with-resources" block to automatically close the connection
        try (Connection conn = DriverManager.getConnection(FULL_DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            System.out.println("Checking/Creating database tables...");
            stmt.execute(createBooksTable);
            stmt.execute(createUsersTable);
            stmt.execute(createLibrariansTable);
            System.out.println("Database tables checked/created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * A main method for testing the connection and creating tables.
     * You can run this file directly to set up the database.
     */
    public static void main(String[] args) {
        // 1. First, try to connect to the server (without the db name) to create the database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            System.out.println("Attempting to initialize database '" + DB_NAME + "'...");
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database created or already exists.");

        } catch (SQLException e) {
            System.err.println("Could not connect to MySQL server or create database.");
            e.printStackTrace();
            return; // Exit if we can't do this
        }

        // 2. Now that we know the DB exists, create the tables
        createTables();
    }
}

