<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.Book" %>
<%
    if (session.getAttribute("admin") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp"); return;
    }
    List<Book> availableBooks = (List<Book>) request.getAttribute("availableBooks");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Issue Book | Library Management System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="wrapper">

    <%@ include file="nav.jsp" %>

    <div class="main-content">
        <div class="page-header">
            <div>
                <h1>📤 Issue Book</h1>
                <p>Issue a book to a student. Only books with available copies are shown.</p>
            </div>
        </div>

        <div class="content-area">

            <!-- Flash Messages -->
            <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">✅ <%= request.getAttribute("success") %></div>
            <% } %>
            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">⚠️ <%= request.getAttribute("error") %></div>
            <% } %>

            <% if (availableBooks == null || availableBooks.isEmpty()) { %>
            <div class="alert alert-warning">
                ⚠️ <strong>No books available for issue right now.</strong>
                All copies are currently issued. Please wait for returns or
                <a href="<%= ctx %>/addBook" style="color:inherit; font-weight:700;">add new books</a>.
            </div>
            <% } else { %>

            <div class="card" style="max-width: 680px;">
                <div class="card-header">
                    <h2>📋 Issue Details</h2>
                    <span class="badge badge-success"><%= availableBooks.size() %> books available</span>
                </div>
                <div class="card-body">
                    <form action="<%= ctx %>/issueBook" method="post" id="issueForm">

                        <div class="form-group">
                            <label class="form-label" for="studentId">Student ID / Roll No. <span class="req">*</span></label>
                            <input type="text" id="studentId" name="studentId" class="form-control"
                                   placeholder="e.g. CS21B001" required maxlength="50">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="studentName">Student Full Name <span class="req">*</span></label>
                            <input type="text" id="studentName" name="studentName" class="form-control"
                                   placeholder="e.g. Rahul Sharma" required maxlength="100">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="bookId">Select Book <span class="req">*</span></label>
                            <select id="bookId" name="bookId" class="form-control" required>
                                <option value="">-- Choose a book --</option>
                                <% for (Book b : availableBooks) { %>
                                <option value="<%= b.getBookId() %>">
                                    <%= b.getTitle() %> — by <%= b.getAuthor() %> [<%= b.getAvailableQty() %> available]
                                </option>
                                <% } %>
                            </select>
                        </div>

                        <div class="form-grid">
                            <div class="form-group">
                                <label class="form-label" for="issueDate">Issue Date <span class="req">*</span></label>
                                <input type="date" id="issueDate" name="issueDate" class="form-control"
                                       value="<%= request.getAttribute("today") %>" required
                                       max="<%= request.getAttribute("today") %>">
                            </div>
                            <div class="form-group">
                                <label class="form-label" for="dueDate">Return Date <span class="req">*</span></label>
                                <input type="date" id="dueDate" name="dueDate" class="form-control" 
                                       required min="<%= request.getAttribute("today") %>">
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary" id="issueBtn" style="margin-top:8px;">
                            📤 Issue Book
                        </button>

                    </form>
                </div>
            </div>
            <% } %>

        </div>
    </div>
</div>

<script>
    document.getElementById && document.getElementById('issueForm') &&
    document.getElementById('issueForm').addEventListener('submit', function() {
        var btn = document.getElementById('issueBtn');
        if (btn) { btn.textContent = '⏳ Processing...'; btn.disabled = true; }
    });
</script>
</body>
</html>
