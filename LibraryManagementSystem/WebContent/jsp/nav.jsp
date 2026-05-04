<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="sidebar">

    <!-- Brand -->
    <div class="sidebar-brand">
        <div class="brand-icon">📚</div>
        <div class="brand-text">
            <h2>Library</h2>
            <span>Management System</span>
        </div>
    </div>

    <!-- Admin Info -->
    <div class="sidebar-admin">
        <div class="admin-avatar">
            S
        </div>
        <div class="admin-info">
            <strong>Syed Thousif</strong>
            <span>Administrator</span>
        </div>
    </div>

    <!-- Navigation Links -->
    <nav class="sidebar-nav">

        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item ${fn:contains(pageContext.request.requestURI, '/dashboard') ? 'active' : ''}">
            <span class="nav-icon">🏠</span> Dashboard
        </a>

        <div class="nav-group-label">Books</div>

        <a href="${pageContext.request.contextPath}/viewBooks" class="nav-item ${ (fn:contains(pageContext.request.requestURI, '/viewBooks') or fn:contains(pageContext.request.requestURI, '/updateBook')) ? 'active' : ''}">
            <span class="nav-icon">📖</span> View All Books
        </a>

        <a href="${pageContext.request.contextPath}/addBook" class="nav-item ${fn:contains(pageContext.request.requestURI, '/addBook') ? 'active' : ''}">
            <span class="nav-icon">➕</span> Add New Book
        </a>

        <a href="${pageContext.request.contextPath}/searchBook" class="nav-item ${fn:contains(pageContext.request.requestURI, '/searchBook') ? 'active' : ''}">
            <span class="nav-icon">🔍</span> Search Books
        </a>

        <div class="nav-group-label">Transactions</div>

        <a href="${pageContext.request.contextPath}/issueBook" class="nav-item ${fn:contains(pageContext.request.requestURI, '/issueBook') ? 'active' : ''}">
            <span class="nav-icon">📤</span> Issue Book
        </a>

        <a href="${pageContext.request.contextPath}/returnBook" class="nav-item ${fn:contains(pageContext.request.requestURI, '/returnBook') ? 'active' : ''}">
            <span class="nav-icon">📥</span> Return Book
        </a>

    </nav>

    <!-- Logout -->
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn" id="logoutBtn">
            <span>🚪</span> Logout
        </a>
    </div>
</div>
