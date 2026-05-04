package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * LogoutServlet - Invalidates the admin session and redirects to login.
 *
 * GET /logout → destroys session, redirects to /login.jsp
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destroy session
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
