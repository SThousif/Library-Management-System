package servlet;

import dao.BookDAO;
import model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * AddBookServlet - Handles adding a new book to the catalog.
 *
 * GET  /addBook → displays the add book form
 * POST /addBook → validates input, inserts book, redirects with message
 */
@WebServlet("/addBook")
public class AddBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    /** Show the add book form */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) { redirect(request, response); return; }
        request.getRequestDispatcher("/jsp/addBook.jsp").forward(request, response);
    }

    /** Process form submission */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) { redirect(request, response); return; }

        String title    = request.getParameter("title");
        String author   = request.getParameter("author");
        String category = request.getParameter("category");
        String qtyStr   = request.getParameter("quantity");

        // Validate inputs
        if (isEmpty(title) || isEmpty(author) || isEmpty(category) || isEmpty(qtyStr)) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/jsp/addBook.jsp").forward(request, response);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyStr.trim());
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Quantity must be a positive number.");
            request.getRequestDispatcher("/jsp/addBook.jsp").forward(request, response);
            return;
        }

        Book book = new Book();
        book.setTitle(title.trim());
        book.setAuthor(author.trim());
        book.setCategory(category.trim());
        book.setQuantity(quantity);

        if (bookDAO.addBook(book)) {
            response.sendRedirect(request.getContextPath() + "/viewBooks?success=Book+added+successfully!");
        } else {
            request.setAttribute("error", "Failed to add book. Please try again.");
            request.getRequestDispatcher("/jsp/addBook.jsp").forward(request, response);
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("admin") != null;
    }
    private void redirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }
    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
}
