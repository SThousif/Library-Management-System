package servlet;

import dao.AdminDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet - Handles admin authentication.
 *
 * GET  /login → displays login.jsp
 * POST /login → validates credentials, creates session, redirects to /dashboard
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();

    /** Show the login page */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, go straight to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("admin") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /** Process login form submission */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Basic input validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Validate against DB
        if (adminDAO.validateAdmin(username.trim(), password.trim())) {
            // Create session on success
            HttpSession session = request.getSession(true);
            // Display name for the admin in the UI
            session.setAttribute("admin", "Syed Thousif");
            session.setMaxInactiveInterval(30 * 60); // 30 minute timeout
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Invalid username or password. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
