<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.Book" %>
<%
    if (session.getAttribute("admin") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp"); return;
    }
    List<Book> results = (List<Book>) request.getAttribute("searchResults");
    String keyword = (String) request.getAttribute("keyword");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Books | Library Management System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="wrapper">

    <%@ include file="nav.jsp" %>

    <div class="main-content">
        <div class="page-header">
            <div>
                <h1>🔍 Search Books</h1>
                <p>Search the catalog by book title or author name.</p>
            </div>
        </div>

        <div class="content-area">

            <!-- Search Form -->
            <div class="card" style="margin-bottom: 24px;">
                <div class="card-body">
                    <form action="<%= ctx %>/searchBook" method="get" id="searchForm">
                        <div class="search-bar">
                            <input type="text" id="keyword" name="keyword" class="form-control"
                                   placeholder="Search by title or author name..."
                                   value="<%= keyword != null ? keyword : "" %>"
                                   autofocus required>
                            <button type="submit" class="btn btn-primary">🔍 Search</button>
                            <% if (keyword != null) { %>
                            <a href="<%= ctx %>/searchBook" class="btn btn-secondary">✕ Clear</a>
                            <% } %>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Results -->
            <% if (keyword != null) { %>
            <div class="card">
                <div class="card-header">
                    <h2>
                        📋 Results for "<%= keyword %>"
                        <% if (results != null) { %>
                        <span class="badge <%= results.isEmpty() ? "badge-danger" : "badge-success" %>"
                              style="margin-left:8px;">
                            <%= results.isEmpty() ? "No results" : results.size() + " found" %>
                        </span>
                        <% } %>
                    </h2>
                </div>

                <div class="table-wrapper">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Title</th>
                                <th>Author</th>
                                <th>Category</th>
                                <th>Total Copies</th>
                                <th>Available</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (results == null || results.isEmpty()) { %>
                            <tr>
                                <td colspan="8" class="table-empty">
                                    📭 No books found matching "<%= keyword %>".
                                    Try a different search term.
                                </td>
                            </tr>
                        <% } else {
                               int sno = 1;
                               for (Book b : results) {
                                   String availBadge = b.getAvailableQty() == 0 ? "avail-zero" :
                                                       b.getAvailableQty() <= 2 ? "avail-low" : "avail-high";
                                   String statusText = b.getAvailableQty() == 0 ? "Out of Stock" :
                                                       b.getAvailableQty() <= 2 ? "Low Stock" : "Available";
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
                                        <% if (b.getAvailableQty() > 0) { %>
                                        <a href="<%= ctx %>/issueBook" class="btn btn-primary btn-sm">📤 Issue</a>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                        <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } else { %>
            <!-- Initial state — show tips -->
            <div class="card">
                <div class="card-body text-center" style="padding: 48px;">
                    <div style="font-size:48px; margin-bottom:16px;">🔍</div>
                    <h3 style="font-size:16px; font-weight:600; margin-bottom:8px;">Search the Book Catalog</h3>
                    <p class="text-muted" style="font-size:14px;">
                        Enter a book title (e.g. "Algorithms") or author name (e.g. "Cormen")
                        to find matching books.
                    </p>
                </div>
            </div>
            <% } %>

        </div>
    </div>
</div>
</body>
</html>
