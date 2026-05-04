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
    <title>Dashboard | Library Management System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="wrapper">

    <%@ include file="nav.jsp" %>

    <div class="main-content">
        <!-- Page Header -->
        <div class="page-header">
            <div>
                <h1>📊 Dashboard</h1>
                <p>Welcome back, <strong>Syed Thousif</strong>! Here's your library overview.</p>
            </div>
        </div>

        <div class="content-area">

            <!-- Stat Cards -->
            <div class="stats-grid">

                <div class="stat-card">
                    <div class="stat-icon blue">📚</div>
                    <div class="stat-info">
                        <div class="stat-value"><%= request.getAttribute("totalBooks") %></div>
                        <div class="stat-label">Total Book Titles</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon purple">📦</div>
                    <div class="stat-info">
                        <div class="stat-value"><%= request.getAttribute("totalQuantity") %></div>
                        <div class="stat-label">Total Copies</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon orange">📤</div>
                    <div class="stat-info">
                        <div class="stat-value"><%= request.getAttribute("issuedCount") %></div>
                        <div class="stat-label">Currently Issued</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon green">✅</div>
                    <div class="stat-info">
                        <div class="stat-value"><%= request.getAttribute("availableCount") %></div>
                        <div class="stat-label">Available Copies</div>
                    </div>
                </div>

            </div>

            <!-- Quick Actions -->
            <div class="quick-actions">
                <h2>Quick Actions</h2>
                <div class="action-grid">

                    <a href="<%= request.getContextPath() %>/viewBooks" class="action-card">
                        <span class="action-icon">📖</span>
                        <span class="action-label">View All Books</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/addBook" class="action-card">
                        <span class="action-icon">➕</span>
                        <span class="action-label">Add New Book</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/issueBook" class="action-card">
                        <span class="action-icon">📤</span>
                        <span class="action-label">Issue Book</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/returnBook" class="action-card">
                        <span class="action-icon">📥</span>
                        <span class="action-label">Return Book</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/searchBook" class="action-card">
                        <span class="action-icon">🔍</span>
                        <span class="action-label">Search Books</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/logout" class="action-card">
                        <span class="action-icon">🚪</span>
                        <span class="action-label">Logout</span>
                    </a>

                </div>
            </div>

        </div><!-- /content-area -->
    </div><!-- /main-content -->
</div><!-- /wrapper -->
</body>
</html>
