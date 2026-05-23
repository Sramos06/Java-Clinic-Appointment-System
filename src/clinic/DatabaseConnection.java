package clinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection Class
 * 
 * OOP Concepts Demonstrated:
 * - Exception Handling: try-catch blocks for database operations
 * - Methods: Organized database functions
 * 
 * IMPORTANT: Change DB_PASSWORD to YOUR MySQL root password!
 */
public class DatabaseConnection {

    // ===== MySQL Configuration =====
    // TODO: Change "YourPasswordHere" to your actual MySQL password!
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/clinic_db";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "password";

    /**
     * Get a new database connection.
     * Remember to call closeConnection() when done!
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL Driver not found!");
            System.out.println("Make sure mysql-connector-j.jar is added to Build Path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to database!");
            System.out.println("Check that MySQL is running and your password is correct.");
            e.printStackTrace();
        }
        return connection;
    }

    /** Safely close a Connection */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    /** Safely close a ResultSet */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing ResultSet: " + e.getMessage());
        }
    }

    /** Safely close a Statement */
    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing Statement: " + e.getMessage());
        }
    }
}
