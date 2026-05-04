package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection - Utility class for MySQL JDBC connections.
 *
 * IMPORTANT: Update DB_PASSWORD to match your MySQL password before running.
 */
public class DatabaseConnection {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL =
        "jdbc:mysql://localhost:3306/library_management" +
        "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER     = "root";
    private static final String PASSWORD = "0786"; // <-- Change this to your MySQL password

    /**
     * Opens and returns a new database connection.
     * The caller MUST close this connection after use.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver not found. " +
                "Place mysql-connector-java.jar in WEB-INF/lib/", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Safely closes a connection (ignores null, swallows exceptions).
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) {
                System.err.println("[DB] Close error: " + e.getMessage());
            }
        }
    }
}
