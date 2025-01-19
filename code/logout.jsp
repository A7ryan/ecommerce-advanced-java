<%-- 
    Document   : logout
    Created on : 11-Dec-2024, 2:40:25 pm
    Author     : aryan
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    session.invalidate();
    
    response.sendRedirect("login.html");
%>
