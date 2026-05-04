package servlet;

import dao.BookDAO;
import dao.IssuedBookDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * DashboardServlet - Loads statistics for the admin dashboard.
 *
 * GET /dashboard → fetches stats, forwards to dashboard.jsp
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

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

        // Gather dashboard stats
        int totalBooks     = bookDAO.getTotalBooks();
        int totalQuantity  = bookDAO.getTotalQuantity();
        int issuedCount    = issuedBookDAO.getIssuedCount();
        int availableCount = bookDAO.getTotalAvailableBooks();

        request.setAttribute("totalBooks",     totalBooks);
        request.setAttribute("totalQuantity",  totalQuantity);
        request.setAttribute("issuedCount",    issuedCount);
        request.setAttribute("availableCount", availableCount);

        request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }
}
