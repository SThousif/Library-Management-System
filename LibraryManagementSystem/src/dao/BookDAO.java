package dao;

import model.Book;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookDAO - Data Access Object for all book-related DB operations.
 *
 * Methods:
 *   addBook(Book)               - Insert new book
 *   getAllBooks()               - Get all books
 *   getBookById(int)            - Get one book by ID
 *   getAvailableBooks()         - Get books with availableQty > 0
 *   updateBook(Book)            - Update book details
 *   deleteBook(int)             - Delete book by ID
 *   searchBooks(String)         - Search by title or author (LIKE)
 *   getTotalBooks()             - Count of all books (sum of quantities)
 *   getTotalAvailableBooks()    - Sum of available_qty across all books
 */
public class BookDAO {

    // ----------------------------------------------------------------
    // Helper: maps a ResultSet row to a Book object
    // ----------------------------------------------------------------
    private Book mapRow(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setBookId(rs.getInt("book_id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setCategory(rs.getString("category"));
        b.setQuantity(rs.getInt("quantity"));
        b.setAvailableQty(rs.getInt("available_qty"));
        return b;
    }

    // ----------------------------------------------------------------
    // ADD a new book
    // ----------------------------------------------------------------
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, category, quantity, available_qty) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setInt(4, book.getQuantity());
            ps.setInt(5, book.getQuantity()); // initially all copies available
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BookDAO] addBook error: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----------------------------------------------------------------
    // GET all books
    // ----------------------------------------------------------------
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY book_id";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[BookDAO] getAllBooks error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ----------------------------------------------------------------
    // GET a single book by ID
    // ----------------------------------------------------------------
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[BookDAO] getBookById error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    // ----------------------------------------------------------------
    // GET books with at least 1 available copy (for issue dropdown)
    // ----------------------------------------------------------------
    public List<Book> getAvailableBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE available_qty > 0 ORDER BY title";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[BookDAO] getAvailableBooks error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ----------------------------------------------------------------
    // UPDATE book details
    // Adjusts available_qty proportionally when total quantity changes
    // ----------------------------------------------------------------
    public boolean updateBook(Book book) {
        // Get current quantity first
        Book existing = getBookById(book.getBookId());
        if (existing == null) return false;

        int qtyDiff    = book.getQuantity() - existing.getQuantity();
        int newAvail   = existing.getAvailableQty() + qtyDiff;
        if (newAvail < 0) newAvail = 0; // guard against negative

        String sql = "UPDATE books SET title=?, author=?, category=?, " +
                     "quantity=?, available_qty=? WHERE book_id=?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setInt(4, book.getQuantity());
            ps.setInt(5, newAvail);
            ps.setInt(6, book.getBookId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BookDAO] updateBook error: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----------------------------------------------------------------
    // DELETE a book by ID
    // Note: will fail if book has active (ISSUED) records (FK constraint)
    // ----------------------------------------------------------------
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BookDAO] deleteBook error: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ----------------------------------------------------------------
    // SEARCH books by title OR author (case-insensitive LIKE)
    // ----------------------------------------------------------------
    public List<Book> searchBooks(String keyword) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY title";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[BookDAO] searchBooks error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ----------------------------------------------------------------
    // DASHBOARD STATS
    // ----------------------------------------------------------------

    /** Total number of distinct book titles in library */
    public int getTotalBooks() {
        return getScalar("SELECT COUNT(*) FROM books");
    }

    /** Sum of all available copies across all books */
    public int getTotalAvailableBooks() {
        return getScalar("SELECT COALESCE(SUM(available_qty), 0) FROM books");
    }

    /** Sum of all copies (quantity) across all books */
    public int getTotalQuantity() {
        return getScalar("SELECT COALESCE(SUM(quantity), 0) FROM books");
    }

    // Helper to run a COUNT/SUM scalar query
    private int getScalar(String sql) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[BookDAO] getScalar error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    // ----------------------------------------------------------------
    // Internal use: update available_qty directly (used by IssuedBookDAO)
    // ----------------------------------------------------------------
    public boolean updateAvailableQty(Connection conn, int bookId, int delta) throws SQLException {
        String sql = "UPDATE books SET available_qty = available_qty + ? WHERE book_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, delta);
        ps.setInt(2, bookId);
        return ps.executeUpdate() > 0;
    }
}
