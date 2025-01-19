<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .cart-table th, .cart-table td {
            vertical-align: middle;
        }
        .empty-cart {
            text-align: center;
            padding: 50px 0;
            font-size: 1.2rem;
            color: #6c757d;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            font-size: 1rem;
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .logout {
            color: red !important;
        }
        .btn-remove {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 5px 10px;
            font-size: 0.9rem;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-remove:hover {
            background-color: #b02a37;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center mb-4">Your Shopping Cart</h1>
        
        <c:if test="${not empty cartItems}">
            <table class="table table-bordered cart-table">
                <thead class="table-light">
                    <tr>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                        <th>Remove Item</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="i" items="${cartItems}">
                        <tr>
                            <td>${i.productName}</td>
                            <td>${i.quantity}</td>
                            <td>$${i.productPrice}</td>
                            <td>$${i.totalPrice}</td>
                            <td>
                                <form action="DeleteItemFromCart" method="post" class="d-flex align-items-center">
                                    <input type="number" name="quantity" min="1" value="1" max="${i.quantity}" class="form-control me-2" style="width: 80px;">
                                    <input type="hidden" name="product_id" value="${i.productId}">
                                    <input type="hidden" name="total_quantity" value="${i.quantity}">
                                    <button type="submit" class="btn-remove">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="text-end">
                <form action="CompleteOrderServlet" method="post">
                    <button type="submit" class="btn btn-primary">Proceed to Checkout</button>
                </form>
            </div>
        </c:if>

        <c:if test="${empty cartItems}">
            <div class="empty-cart">
                <p>Your cart is empty.</p>
            </div>
        </c:if>

        <div class="text-center">
            <a href="products.jsp" class="back-link">Back to Products</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
