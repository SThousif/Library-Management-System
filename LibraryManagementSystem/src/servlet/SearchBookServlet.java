package servlet;

import dao.BookDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * SearchBookServlet - Searches books by title or author keyword.
 *
 * GET /searchBook           → shows empty search page
 * GET /searchBook?keyword=X → shows matching results
 */
@WebServlet("/searchBook")
public class SearchBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Session guard
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String keyword = request.getParameter("keyword");

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Perform search and pass results
            request.setAttribute("searchResults", bookDAO.searchBooks(keyword.trim()));
            request.setAttribute("keyword", keyword.trim());
        }

        request.getRequestDispatcher("/jsp/searchBook.jsp").forward(request, response);
    }

    /** Support POST (from search form) - just delegate to GET */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
