package servlet;

import dao.BookDAO;
import dao.IssuedBookDAO;
import model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * UpdateBookServlet - Pre-fills and processes the edit book form.
 *
 * GET  /updateBook?bookId=X → fetches book, forwards to editBook.jsp
 * POST /updateBook           → validates & saves updated book
 */
@WebServlet("/updateBook")
public class UpdateBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    /** Load the book and show the edit form */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) { doRedirect(request, response); return; }

        String bookIdStr = request.getParameter("bookId");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/viewBooks?error=Invalid+book+ID");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr.trim());
            Book book  = bookDAO.getBookById(bookId);
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/viewBooks?error=Book+not+found");
                return;
            }
            request.setAttribute("book", book);
            request.getRequestDispatcher("/jsp/editBook.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/viewBooks?error=Invalid+book+ID");
        }
    }

    /** Save updated book details */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) { doRedirect(request, response); return; }

        String bookIdStr = request.getParameter("bookId");
        String title     = request.getParameter("title");
        String author    = request.getParameter("author");
        String category  = request.getParameter("category");
        String qtyStr    = request.getParameter("quantity");

        // Validate
        if (isEmpty(title) || isEmpty(author) || isEmpty(category) || isEmpty(qtyStr) || isEmpty(bookIdStr)) {
            request.setAttribute("error", "All fields are required.");
            // Re-populate form
            try {
                Book b = bookDAO.getBookById(Integer.parseInt(bookIdStr));
                if (b != null) { b.setTitle(title); b.setAuthor(author); b.setCategory(category); }
                request.setAttribute("book", b);
            } catch (Exception ignored) {}
            request.getRequestDispatcher("/jsp/editBook.jsp").forward(request, response);
            return;
        }

        int bookId, quantity;
        try {
            bookId   = Integer.parseInt(bookIdStr.trim());
            quantity = Integer.parseInt(qtyStr.trim());
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Quantity must be a positive whole number.");
            request.getRequestDispatcher("/jsp/editBook.jsp").forward(request, response);
            return;
        }

        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(title.trim());
        book.setAuthor(author.trim());
        book.setCategory(category.trim());
        book.setQuantity(quantity);

        if (bookDAO.updateBook(book)) {
            response.sendRedirect(request.getContextPath() + "/viewBooks?success=Book+updated+successfully!");
        } else {
            request.setAttribute("error", "Failed to update book. Please try again.");
            request.setAttribute("book", book);
            request.getRequestDispatcher("/jsp/editBook.jsp").forward(request, response);
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("admin") != null;
    }
    private void doRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }
    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
}
