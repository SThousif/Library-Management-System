<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.Book" %>
<%
    if (session.getAttribute("admin") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp"); return;
    }
    List<Book> books = (List<Book>) request.getAttribute("books");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Books | Library Management System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="wrapper">

    <%@ include file="nav.jsp" %>

    <div class="main-content">
        <div class="page-header">
            <div>
                <h1>📖 Book Catalog</h1>
                <p>All books registered in the library system.</p>
            </div>
            <a href="<%= ctx %>/addBook" class="btn btn-primary">➕ Add New Book</a>
        </div>

        <div class="content-area">

            <!-- Flash Messages -->
            <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">✅ <%= request.getAttribute("success") %></div>
            <% } %>
            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">⚠️ <%= request.getAttribute("error") %></div>
            <% } %>

            <div class="card">
                <div class="card-header">
                    <h2>📚 All Books
                        <% if (books != null) { %>
                        <span class="badge badge-info" style="font-size:12px; margin-left:8px;"><%= books.size() %> books</span>
                        <% } %>
                    </h2>
                    <!-- Live search filter -->
                    <input type="text" id="tableSearch" class="form-control"
                           placeholder="🔍 Filter table..." style="width:220px; padding:7px 12px;">
                </div>

                <div class="table-wrapper">
                    <table class="table" id="booksTable">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Title</th>
                                <th>Author</th>
                                <th>Category</th>
                                <th>Total Qty</th>
                                <th>Available</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (books == null || books.isEmpty()) { %>
                            <tr><td colspan="8" class="table-empty">📭 No books found. <a href="<%= ctx %>/addBook">Add the first book →</a></td></tr>
                        <% } else {
                               int sno = 1;
                               for (Book b : books) {
                                   String availBadge = b.getAvailableQty() == 0 ? "avail-zero" :
                                                       b.getAvailableQty() <= 2 ? "avail-low"  : "avail-high";
                                   String statusText = b.getAvailableQty() == 0 ? "Out of Stock" :
                                                       b.getAvailableQty() <= 2 ? "Low Stock"   : "Available";
                        %>
                            <tr>
                                <td><%= sno++ %></td>
                                <td><strong><%= b.getTitle() %></strong></td>
                                <td><%= b.getAuthor() %></td>
                                <td><span class="badge badge-secondary"><%= b.getCategory() %></span></td>
                                <td style="text-align:center;"><%= b.getQuantity() %></td>
                                <td style="text-align:center;"><strong><%= b.getAvailableQty() %></strong></td>
                                <td><span class="badge <%= availBadge %>"><%= statusText %></span></td>
                                <td>
                                    <div class="actions-cell">
                                        <a href="<%= ctx %>/updateBook?bookId=<%= b.getBookId() %>"
                                           class="btn btn-warning btn-sm">✏️ Edit</a>
                                        <a href="<%= ctx %>/deleteBook?bookId=<%= b.getBookId() %>"
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Delete \'<%= b.getTitle().replace("'", "\\'") %>\'? This cannot be undone.')">
                                           🗑️ Delete</a>
                                    </div>
                                </td>
                            </tr>
                        <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
    // Live table filter
    document.getElementById('tableSearch').addEventListener('keyup', function() {
        var filter = this.value.toLowerCase();
        var rows = document.querySelectorAll('#booksTable tbody tr');
        rows.forEach(function(row) {
            row.style.display = row.textContent.toLowerCase().includes(filter) ? '' : 'none';
        });
    });
</script>
</body>
</html>
