<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Purchase Product</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        .header {
            background-color: #343a40;
            color: white;
            padding: 10px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header a, .header button {
            color: white;
            text-decoration: none;
            margin-left: 10px;
            border: none;
            background: none;
            cursor: pointer;
            font-size: 1rem;
        }
        .header a.logout {
            color: #dc3545;
        }
        .header a:hover, .header button:hover {
            text-decoration: underline;
        }
        .content {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .product {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
        }
        .product p {
            margin: 0;
            font-size: 1.2rem;
            font-weight: bold;
        }
        .product form {
            display: flex;
            align-items: center;
        }
        .product input[type="number"] {
            width: 60px;
            padding: 5px;
            margin-right: 10px;
            font-size: 1rem;
        }
        .product input[type="submit"] {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 4px;
            font-size: 1rem;
            cursor: pointer;
        }
        .product input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .footer {
            text-align: center;
            padding: 10px;
            background-color: #343a40;
            color: white;
            margin-top: 50px;
        }
    </style>
</head>
<body>
    <!-- Header Section -->
    <div class="header">
        <c:if test="${not empty sessionScope.username}">
            <div>
                Welcome, <strong>${sessionScope.username}</strong>
            </div>
            <div>
                <form action="DisplayCartServlet" method="post" style="display: inline;">
                    <button type="submit">Cart</button>
                </form>
                <a href="orders.jsp">Orders</a>
                <a href="logout.jsp" class="logout">Logout</a>
            </div>
        </c:if>
        <c:if test="${empty sessionScope.username}">
            <a href="login.html">Login</a>
        </c:if>
    </div>

    <!-- Content Section -->
    <div class="content">
        <!-- Guitar Product -->
        <div class="product">
            <p>Guitar</p>
            <form action="AddToCartServlet" method="post">
                <input type="hidden" name="product_id" value="1">
                <input type="number" name="quantity" value="1" min="1">
                <input type="submit" value="Add to Cart">
            </form>
        </div>

        <!-- Piano Product -->
        <div class="product">
            <p>Piano</p>
            <form action="AddToCartServlet" method="post">
                <input type="hidden" name="product_id" value="2">
                <input type="number" name="quantity" value="1" min="1">
                <input type="submit" value="Add to Cart">
            </form>
        </div>
    </div>

    <!-- Footer Section -->
    <div class="footer">
        &copy; 2025 Marvel Music Store
    </div>
</body>
</html>
