<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
            padding: 20px;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background: #fff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .header {
            text-align: right;
            margin-bottom: 20px;
        }
        .header a {
            margin-left: 10px;
            text-decoration: none;
            color: #007bff;
        }
        .header a.logout {
            color: #dc3545;
        }
        .orders-table {
            width: 100%;
            margin: 20px 0;
            border-collapse: collapse;
        }
        .orders-table th, .orders-table td {
            padding: 12px;
            text-align: center;
            border: 1px solid #ddd;
        }
        .orders-table th {
            background-color: #f8f9fa;
        }
        .grand-total {
            font-size: 1.2rem;
            font-weight: bold;
            text-align: right;
            margin-top: 20px;
        }
        .empty-orders {
            text-align: center;
            font-size: 1.2rem;
            color: #6c757d;
            margin-top: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header Section -->
        <div class="header">
            <c:if test="${not empty sessionScope.username}">
                <span>Welcome, <strong>${sessionScope.username}</strong></span>
                <a href="DisplayCartServlet">Cart</a>
                <a href="products.jsp">Products</a>
                <a href="logout.jsp" class="logout">Logout</a>
            </c:if>
            <c:if test="${empty sessionScope.username}">
                <a href="login.html">Login</a>
            </c:if>
        </div>

        <!-- Orders Section -->
        <h1 class="text-center">Your Orders</h1>

        <sql:setDataSource var="dataSource" driver="com.mysql.cj.jdbc.Driver" 
            url="jdbc:mysql://localhost:3306/music_db" user="root" password="" />

        <c:set var="userId" value="${sessionScope.user_id}" />
        <sql:query var="orderData" dataSource="${dataSource}">
            SELECT oi.order_instrument_id, oi.product_id, oi.quantity, oi.total_price, oi.ordered_at, p.product_name
            FROM ordered_instruments oi
            JOIN products p ON oi.product_id = p.product_id
            WHERE oi.user_id = ${userId}
            ORDER BY oi.ordered_at DESC
        </sql:query>

        <c:if test="${not empty orderData.rows}">
            <table class="table table-bordered orders-table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                        <th>Order Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="grandTotal" value="0" />
                    <c:forEach var="order" items="${orderData.rows}">
                        <tr>
                            <td>${order.order_instrument_id}</td>
                            <td>${order.product_name}</td>
                            <td>${order.quantity}</td>
                            <td>$${order.total_price}</td>
                            <td>$${order.quantity * order.total_price}</td>
                            <td>${order.ordered_at}</td>
                        </tr>
                        <c:set var="grandTotal" value="${grandTotal + (order.quantity * order.total_price)}" />
                    </c:forEach>
                </tbody>
            </table>
            <div class="grand-total">
                Grand Total: $<c:out value="${grandTotal}" />
            </div>
        </c:if>

        <c:if test="${empty orderData.rows}">
            <div class="empty-orders">
                <p>You have not made any orders yet.</p>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
