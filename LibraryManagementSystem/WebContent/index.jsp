<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // If already logged in, go to dashboard; otherwise to login page
    HttpSession existingSession = request.getSession(false);
    if (existingSession != null && existingSession.getAttribute("admin") != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
    } else {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
%>
