package servlet;

import dao.IssuedBookDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

/**
 * ReturnBookServlet - Handles book return.
 *
 * GET /returnBook → shows all currently issued books (for return)
 * POST /returnBook → processes return, increments available_qty
 */
@WebServlet("/returnBook")
public class ReturnBookServlet extends HttpServlet {

    private final IssuedBookDAO issuedBookDAO = new IssuedBookDAO();

    /** Load active issues and show the return page */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            doRedirect(request, response);
            return;
        }

        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if (success != null)
            request.setAttribute("success", success);
        if (error != null)
            request.setAttribute("error", error);

        request.setAttribute("issuedRecords", issuedBookDAO.getActiveIssues());
        request.setAttribute("today", LocalDate.now().toString());
        request.getRequestDispatcher("/jsp/returnBook.jsp").forward(request, response);
    }

    /** Process book return */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            doRedirect(request, response);
            return;
        }

        String issueIdStr = request.getParameter("issueId");
        String returnDate = request.getParameter("returnDate");

        if (issueIdStr == null || issueIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/returnBook?error=Invalid+issue+ID.");
            return;
        }
        if (returnDate == null || returnDate.trim().isEmpty()) {
            returnDate = LocalDate.now().toString(); // Default to today
        }

        int issueId;
        try {
            issueId = Integer.parseInt(issueIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/returnBook?error=Invalid+issue+ID.");
            return;
        }

        if (issuedBookDAO.returnBook(issueId, returnDate)) {
            response.sendRedirect(request.getContextPath() +
                    "/returnBook?success=Book+returned+successfully!");
        } else {
            response.sendRedirect(request.getContextPath() +
                    "/returnBook?error=Failed+to+process+return.+Book+may+already+be+returned.");
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("admin") != null;
    }

    private void doRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
