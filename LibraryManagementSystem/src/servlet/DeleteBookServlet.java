package servlet;

import dao.BookDAO;
import dao.IssuedBookDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * DeleteBookServlet - Handles book deletion.
 *
 * GET /deleteBook?bookId=X → checks for active issues, deletes if safe, redirects
 */
@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {

    private final BookDAO       bookDAO       = new BookDAO();
    private final IssuedBookDAO issuedBookDAO = new IssuedBookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Session guard
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String bookIdStr = request.getParameter("bookId");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/viewBooks?error=Invalid+book+ID");
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/viewBooks?error=Invalid+book+ID");
            return;
        }

        // Safety check: cannot delete a book that is currently issued
        if (issuedBookDAO.hasActiveIssue(bookId)) {
            response.sendRedirect(request.getContextPath() +
                "/viewBooks?error=Cannot+delete:+book+is+currently+issued+to+a+student.");
            return;
        }

        if (bookDAO.deleteBook(bookId)) {
            response.sendRedirect(request.getContextPath() + "/viewBooks?success=Book+deleted+successfully!");
        } else {
            response.sendRedirect(request.getContextPath() + "/viewBooks?error=Failed+to+delete+book.");
        }
    }
}
