<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (session.getAttribute("admin") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp"); return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Book | Library Management System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="wrapper">

    <%@ include file="nav.jsp" %>

    <div class="main-content">
        <div class="page-header">
            <div>
                <h1>➕ Add New Book</h1>
                <p>Fill in the details to add a new book to the library catalog.</p>
            </div>
            <a href="<%= request.getContextPath() %>/viewBooks" class="btn btn-secondary">← Back to Books</a>
        </div>

        <div class="content-area">

            <!-- Messages -->
            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">⚠️ <%= request.getAttribute("error") %></div>
            <% } %>

            <div class="card" style="max-width: 680px;">
                <div class="card-header">
                    <h2>📚 Book Information</h2>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/addBook" method="post" id="addBookForm">

                        <div class="form-group">
                            <label class="form-label" for="title">Book Title <span class="req">*</span></label>
                            <input type="text" id="title" name="title" class="form-control"
                                   placeholder="e.g. Introduction to Algorithms" required maxlength="200"
                                   value="<%= request.getParameter("title") != null ? request.getParameter("title") : "" %>">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="author">Author Name <span class="req">*</span></label>
                            <input type="text" id="author" name="author" class="form-control"
                                   placeholder="e.g. Thomas H. Cormen" required maxlength="100"
                                   value="<%= request.getParameter("author") != null ? request.getParameter("author") : "" %>">
                        </div>

                        <div class="form-grid">
                            <div class="form-group">
                                <label class="form-label" for="category">Category <span class="req">*</span></label>
                                <select id="category" name="category" class="form-control" required>
                                    <option value="">-- Select Category --</option>
                                    <% String[] cats = {"Computer Science","Software Engineering","Database",
                                       "Operating Systems","Networking","Mathematics","Artificial Intelligence",
                                       "Electronics","Electrical","Mechanical","Civil","Physics","Chemistry",
                                       "Management","Other"};
                                       String selCat = request.getParameter("category");
                                       for (String cat : cats) { %>
                                    <option value="<%= cat %>" <%= cat.equals(selCat) ? "selected" : "" %>><%= cat %></option>
                                    <% } %>
                                </select>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="quantity">Total Copies <span class="req">*</span></label>
                                <input type="number" id="quantity" name="quantity" class="form-control"
                                       placeholder="e.g. 3" required min="1" max="999"
                                       value="<%= request.getParameter("quantity") != null ? request.getParameter("quantity") : "1" %>">
                            </div>
                        </div>

                        <div style="display:flex; gap:12px; margin-top:8px;">
                            <button type="submit" class="btn btn-primary" id="submitBtn">
                                📥 Add Book
                            </button>
                            <button type="reset" class="btn btn-secondary">🔄 Reset</button>
                            <a href="<%= request.getContextPath() %>/viewBooks" class="btn btn-secondary">Cancel</a>
                        </div>

                    </form>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
    document.getElementById('addBookForm').addEventListener('submit', function() {
        var btn = document.getElementById('submitBtn');
        btn.textContent = '⏳ Adding...';
        btn.disabled = true;
    });
</script>
</body>
</html>
