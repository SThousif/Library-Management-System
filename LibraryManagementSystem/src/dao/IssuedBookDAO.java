package dao;

import model.IssuedBook;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * IssuedBookDAO - Data Access Object for book issue and return transactions.
 *
 * Methods:
 *   issueBook(IssuedBook) - Record issue + decrement available_qty (DB transaction)
 *   returnBook(int, String) - Mark as returned + increment available_qty (DB transaction)
 *   getAllIssuedBooks()    - Get all records with book title (JOIN)
 *   getActiveIssues()     - Get only ISSUED (not yet returned) records
 *   getIssuedCount()      - Count of currently issued books (for dashboard)
 *   hasActiveIssue(int)   - Check if a book has active issues (before delete)
 */
public class IssuedBookDAO {

    // ----------------------------------------------------------------
    // Helper: maps a ResultSet row to an IssuedBook object
    // ----------------------------------------------------------------
    private IssuedBook mapRow(ResultSet rs) throws SQLException {
        IssuedBook ib = new IssuedBook();
        ib.setIssueId(rs.getInt("issue_id"));
        ib.setStudentId(rs.getString("student_id"));
        ib.setStudentName(rs.getString("student_name"));
        ib.setBookId(rs.getInt("book_id"));
        ib.setIssueDate(rs.getString("issue_date"));
        ib.setReturnDate(rs.getString("return_date"));
        ib.setStatus(rs.getString("status"));
        // bookTitle is set only when fetched via JOIN
        try { ib.setBookTitle(rs.getString("title")); } catch (SQLException ignored) {}
        return ib;
    }

    // ----------------------------------------------------------------
    // ISSUE a book to a student
    // Uses a DB transaction:
    //   1. Check available_qty > 0
    //   2. Insert into issued_books
    //   3. Decrement books.available_qty by 1
    // ----------------------------------------------------------------
    public boolean issueBook(IssuedBook ib) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Step 1: Check availability (with lock)
            String checkSql = "SELECT available_qty FROM books WHERE book_id = ? FOR UPDATE";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, ib.getBookId());
            ResultSet rs = checkPs.executeQuery();
            if (!rs.next() || rs.getInt("available_qty") < 1) {
                conn.rollback();
                return false; // Not available
            }

            // Step 2: Insert issue record
            String insertSql = "INSERT INTO issued_books " +
                "(student_id, student_name, book_id, issue_date, return_date, status) " +
                "VALUES (?, ?, ?, ?, ?, 'ISSUED')";
            PreparedStatement insertPs = conn.prepareStatement(insertSql);
            insertPs.setString(1, ib.getStudentId());
            insertPs.setString(2, ib.getStudentName());
            insertPs.setInt(3, ib.getBookId());
            insertPs.setString(4, ib.getIssueDate());
            insertPs.setString(5, ib.getReturnDate()); // Store due date here
            insertPs.executeUpdate();

            // Step 3: Decrement available_qty
            String updateSql = "UPDATE books SET available_qty = available_qty - 1 WHERE book_id = ?";
            PreparedStatement updatePs = conn.prepareStatement(updateSql);
            updatePs.setInt(1, ib.getBookId());
            updatePs.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            System.err.println("[IssuedBookDAO] issueBook error: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) {}
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----------------------------------------------------------------
    // RETURN a book
    // Uses a DB transaction:
    //   1. Get bookId from the issue record
    //   2. Update status = 'RETURNED', set return_date
    //   3. Increment books.available_qty by 1
    // ----------------------------------------------------------------
    public boolean returnBook(int issueId, String returnDate) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Step 1: Get bookId and verify ISSUED status
            String getSql = "SELECT book_id, status FROM issued_books WHERE issue_id = ?";
            PreparedStatement getPs = conn.prepareStatement(getSql);
            getPs.setInt(1, issueId);
            ResultSet rs = getPs.executeQuery();
            if (!rs.next() || !"ISSUED".equals(rs.getString("status"))) {
                conn.rollback();
                return false; // Already returned or not found
            }
            int bookId = rs.getInt("book_id");

            // Step 2: Mark as returned
            String updateIssueSql =
                "UPDATE issued_books SET status='RETURNED', return_date=? WHERE issue_id=?";
            PreparedStatement updateIssuePs = conn.prepareStatement(updateIssueSql);
            updateIssuePs.setString(1, returnDate);
            updateIssuePs.setInt(2, issueId);
            updateIssuePs.executeUpdate();

            // Step 3: Increment available_qty (ensuring it doesn't exceed total quantity)
            String updateBookSql =
                "UPDATE books SET available_qty = LEAST(available_qty + 1, quantity) WHERE book_id = ?";
            PreparedStatement updateBookPs = conn.prepareStatement(updateBookSql);
            updateBookPs.setInt(1, bookId);
            updateBookPs.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            System.err.println("[IssuedBookDAO] returnBook error: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) {}
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----------------------------------------------------------------
    // GET all issued records (with book title via JOIN)
    // ----------------------------------------------------------------
    public List<IssuedBook> getAllIssuedBooks() {
        List<IssuedBook> list = new ArrayList<>();
        String sql = "SELECT ib.*, b.title FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "ORDER BY ib.issue_id DESC";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[IssuedBookDAO] getAllIssuedBooks error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ----------------------------------------------------------------
    // GET only currently ISSUED records (for the return page)
    // ----------------------------------------------------------------
    public List<IssuedBook> getActiveIssues() {
        List<IssuedBook> list = new ArrayList<>();
        String sql = "SELECT ib.*, b.title FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "WHERE ib.status = 'ISSUED' " +
                     "ORDER BY ib.issue_date ASC";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[IssuedBookDAO] getActiveIssues error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ----------------------------------------------------------------
    // DASHBOARD: count of currently issued books
    // ----------------------------------------------------------------
    public int getIssuedCount() {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE status = 'ISSUED'";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[IssuedBookDAO] getIssuedCount error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    // ----------------------------------------------------------------
    // CHECK if book has active issues (used before delete)
    // ----------------------------------------------------------------
    public boolean hasActiveIssue(int bookId) {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE book_id=? AND status='ISSUED'";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[IssuedBookDAO] hasActiveIssue error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
}
