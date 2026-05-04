<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.IssuedBook" %>
<%
    if (session.getAttribute("admin") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp"); return;
    }
    List<IssuedBook> issuedRecords = (List<IssuedBook>) request.getAttribute("issuedRecords");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Return Book | Library Management System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="wrapper">

    <%@ include file="nav.jsp" %>

    <div class="main-content">
        <div class="page-header">
            <div>
                <h1>📥 Return Book</h1>
                <p>Process book returns for students. Click the Return button for a record.</p>
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

            <div class="card">
                <div class="card-header">
                    <h2>📋 Currently Issued Books
                        <% if (issuedRecords != null) { %>
                        <span class="badge badge-warning" style="margin-left:8px;"><%= issuedRecords.size() %> pending</span>
                        <% } %>
                    </h2>
                    <input type="text" id="returnSearch" class="form-control"
                           placeholder="🔍 Filter by student / book..." style="width:240px; padding:7px 12px;">
                </div>

                <div class="table-wrapper">
                    <table class="table" id="returnTable">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Issue ID</th>
                                <th>Student ID</th>
                                <th>Student Name</th>
                                <th>Book Title</th>
                                <th>Issue Date</th>
                                <th>Due Date</th>
                                <th style="text-align:center;">ACTION</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (issuedRecords == null || issuedRecords.isEmpty()) { %>
                            <tr>
                                <td colspan="8" class="table-empty">
                                    📭 No books are currently issued. All returned!
                                </td>
                            </tr>
                        <% } else {
                               int sno = 1;
                               for (IssuedBook ib : issuedRecords) { %>
                            <tr>
                                <td><%= sno++ %></td>
                                <td><span class="badge badge-secondary">#<%= ib.getIssueId() %></span></td>
                                <td><strong><%= ib.getStudentId() %></strong></td>
                                <td><%= ib.getStudentName() %></td>
                                <td><%= ib.getBookTitle() != null ? ib.getBookTitle() : "Book #" + ib.getBookId() %></td>
                                <td><%= ib.getIssueDate() %></td>
                                <td><span class="badge badge-warning" style="font-weight:500;"><%= ib.getReturnDate() != null ? ib.getReturnDate() : "—" %></span></td>
                                <td style="text-align:center;">
                                    <form action="<%= ctx %>/returnBook" method="post" 
                                          onsubmit="return confirm('Confirm return of \'<%= (ib.getBookTitle() != null ? ib.getBookTitle().replace("'", "\\'") : "this book") %>\'?')">
                                        <input type="hidden" name="issueId" value="<%= ib.getIssueId() %>">
                                        <button type="submit" class="btn btn-success btn-sm" style="background: linear-gradient(135deg, #06d6a0, #059669); border:none; padding: 7px 20px;">
                                            📥 Return Book
                                        </button>
                                    </form>
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
    document.getElementById('returnSearch').addEventListener('keyup', function() {
        var filter = this.value.toLowerCase();
        var rows = document.querySelectorAll('#returnTable tbody tr');
        rows.forEach(function(row) {
            row.style.display = row.textContent.toLowerCase().includes(filter) ? '' : 'none';
        });
    });
</script>
</body>
</html>
