package servlet;

import dao.BookDAO;
import model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * ViewBooksServlet - Retrieves and displays all books.
 *
 * GET /viewBooks → fetches all books, forwards to viewBooks.jsp
 */
@WebServlet("/viewBooks")
public class ViewBooksServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Pass any success/error messages from redirect params
        String success = request.getParameter("success");
        String error   = request.getParameter("error");
        if (success != null) request.setAttribute("success", success);
        if (error   != null) request.setAttribute("error",   error);

        request.setAttribute("books", bookDAO.getAllBooks());
        request.getRequestDispatcher("/jsp/viewBooks.jsp").forward(request, response);
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("admin") != null;
    }
}
