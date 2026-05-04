package dao;

import model.Book;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDAO - Data Access Object for admin authentication.
 */
public class AdminDAO {

    /**
     * Validates admin credentials against the admins table.
     *
     * @param username entered username
     * @param password entered password
     * @return true if credentials match, false otherwise
     */
    public boolean validateAdmin(String username, String password) {
        String sql = "SELECT id FROM admins WHERE username = ? AND password = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if at least one row found
        } catch (SQLException e) {
            System.err.println("[AdminDAO] validateAdmin error: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
}
