<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login | Library Management System</title>
    <meta name="description" content="Library Management System - Admin Login">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="login-page">
    <div class="login-card">

        <!-- Logo & Title -->
        <div class="login-logo">
            <div class="icon-wrap">📚</div>
            <h1>Library Management</h1>
            <p>Sign in to your administrator account</p>
        </div>

        <!-- Error Message -->
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error">
            ⚠️ <%= request.getAttribute("error") %>
        </div>
        <% } %>

        <!-- Login Form -->
        <form action="<%= request.getContextPath() %>/login" method="post" id="loginForm">

            <div class="form-group">
                <label class="form-label" for="username">
                    Username <span class="req">*</span>
                </label>
                <input type="text" id="username" name="username" class="form-control"
                       placeholder="Enter admin username" required autofocus
                       value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>">
            </div>

            <div class="form-group" style="margin-bottom: 24px;">
                <label class="form-label" for="password">
                    Password <span class="req">*</span>
                </label>
                <input type="password" id="password" name="password" class="form-control"
                       placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn btn-primary btn-full btn-lg" id="loginBtn">
                🔐 Sign In
            </button>
        </form>

        <div class="login-footer">
            <p>Library Management System</p>
        </div>
    </div>
</div>

<script>
    // Simple loading state on form submit
    document.getElementById('loginForm').addEventListener('submit', function() {
        var btn = document.getElementById('loginBtn');
        btn.textContent = '⏳ Signing in...';
        btn.disabled = true;
    });
</script>
</body>
</html>
