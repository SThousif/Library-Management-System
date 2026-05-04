package servlet;

import dao.BookDAO;
import dao.IssuedBookDAO;
import model.IssuedBook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

/**
 * IssueBookServlet - Issues a book to a student.
 *
 * GET /issueBook → loads available books list, shows issue form
 * POST /issueBook → validates, records issue, decrements qty (in transaction)
 */
@WebServlet("/issueBook")
public class IssueBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();
    private final IssuedBookDAO issuedBookDAO = new IssuedBookDAO();

    /** Show the issue book form with available books */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            doRedirect(request, response);
            return;
        }

        // Pass flash messages
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if (success != null)
            request.setAttribute("success", success);
        if (error != null)
            request.setAttribute("error", error);

        // Default issue date = today
        request.setAttribute("today", LocalDate.now().toString());
        request.setAttribute("availableBooks", bookDAO.getAvailableBooks());
        request.getRequestDispatcher("/jsp/issueBook.jsp").forward(request, response);
    }

    /** Process issue form */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            doRedirect(request, response);
            return;
        }

        String studentId = request.getParameter("studentId");
        String studentName = request.getParameter("studentName");
        String bookIdStr = request.getParameter("bookId");
        String issueDate = request.getParameter("issueDate");
        String dueDate = request.getParameter("dueDate");

        // Validate all fields
        if (isEmpty(studentId) || isEmpty(studentName) || isEmpty(bookIdStr) || isEmpty(issueDate)
                || isEmpty(dueDate)) {
            request.setAttribute("error", "All fields are required.");
            request.setAttribute("availableBooks", bookDAO.getAvailableBooks());
            request.setAttribute("today", LocalDate.now().toString());
            request.getRequestDispatcher("/jsp/issueBook.jsp").forward(request, response);
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bookIdStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid book selected.");
            request.setAttribute("availableBooks", bookDAO.getAvailableBooks());
            request.setAttribute("today", LocalDate.now().toString());
            request.getRequestDispatcher("/jsp/issueBook.jsp").forward(request, response);
            return;
        }

        // Build IssuedBook record
        IssuedBook ib = new IssuedBook();
        ib.setStudentId(studentId.trim());
        ib.setStudentName(studentName.trim());
        ib.setBookId(bookId);
        ib.setIssueDate(issueDate);
        ib.setReturnDate(dueDate); // Using return_date column as Due Date during issue
        ib.setStatus("ISSUED");

        if (issuedBookDAO.issueBook(ib)) {
            response.sendRedirect(request.getContextPath() +
                    "/issueBook?success=Book+issued+successfully!");
        } else {
            response.sendRedirect(request.getContextPath() +
                    "/issueBook?error=Book+is+no+longer+available.+Please+choose+another.");
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("admin") != null;
    }

    private void doRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
