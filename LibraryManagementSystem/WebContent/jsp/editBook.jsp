<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Book" %>
<%
    if (session.getAttribute("admin") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp"); return;
    }
    Book book = (Book) request.getAttribute("book");
    if (book == null) {
        response.sendRedirect(request.getContextPath() + "/viewBooks?error=Book+not+found"); return;
    }
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Book | Library Management System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="wrapper">

    <%@ include file="nav.jsp" %>

    <div class="main-content">
        <div class="page-header">
            <div>
                <h1>✏️ Edit Book</h1>
                <p>Update the details for: <strong><%= book.getTitle() %></strong></p>
            </div>
            <a href="<%= ctx %>/viewBooks" class="btn btn-secondary">← Back to Books</a>
        </div>

        <div class="content-area">

            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">⚠️ <%= request.getAttribute("error") %></div>
            <% } %>

            <div class="card" style="max-width: 680px;">
                <div class="card-header">
                    <h2>📝 Edit Book Details</h2>
                    <span class="badge badge-secondary">Book ID: <%= book.getBookId() %></span>
                </div>
                <div class="card-body">

                    <!-- Availability info -->
                    <div class="alert alert-info" style="margin-bottom:20px;">
                        ℹ️ <strong>Current stock:</strong>
                        <%= book.getQuantity() %> total copies,
                        <%= book.getAvailableQty() %> available,
                        <%= (book.getQuantity() - book.getAvailableQty()) %> currently issued.
                        <br>Changing quantity will automatically adjust available copies.
                    </div>

                    <form action="<%= ctx %>/updateBook" method="post" id="editForm">
                        <!-- Hidden book ID -->
                        <input type="hidden" name="bookId" value="<%= book.getBookId() %>">

                        <div class="form-group">
                            <label class="form-label" for="title">Book Title <span class="req">*</span></label>
                            <input type="text" id="title" name="title" class="form-control"
                                   value="<%= book.getTitle() %>" required maxlength="200">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="author">Author Name <span class="req">*</span></label>
                            <input type="text" id="author" name="author" class="form-control"
                                   value="<%= book.getAuthor() %>" required maxlength="100">
                        </div>

                        <div class="form-grid">
                            <div class="form-group">
                                <label class="form-label" for="category">Category <span class="req">*</span></label>
                                <select id="category" name="category" class="form-control" required>
                                    <% String[] cats = {"Computer Science","Software Engineering","Database",
                                       "Operating Systems","Networking","Mathematics","Artificial Intelligence",
                                       "Electronics","Electrical","Mechanical","Civil","Physics","Chemistry",
                                       "Management","Other"};
                                       for (String cat : cats) { %>
                                    <option value="<%= cat %>" <%= cat.equals(book.getCategory()) ? "selected" : "" %>><%= cat %></option>
                                    <% } %>
                                </select>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="quantity">Total Copies <span class="req">*</span></label>
                                <input type="number" id="quantity" name="quantity" class="form-control"
                                       value="<%= book.getQuantity() %>" required min="1" max="999">
                            </div>
                        </div>

                        <div style="display:flex; gap:12px; margin-top:8px;">
                            <button type="submit" class="btn btn-primary" id="saveBtn">
                                💾 Save Changes
                            </button>
                            <a href="<%= ctx %>/viewBooks" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('editForm').addEventListener('submit', function() {
        var btn = document.getElementById('saveBtn');
        btn.textContent = '⏳ Saving...';
        btn.disabled = true;
    });
</script>
</body>
</html>
