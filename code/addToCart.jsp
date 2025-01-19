<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String productId = request.getParameter("product_id");
    String quantity = request.getParameter("quantity");
    
    if (productId != null && quantity != null) {
        List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        
        Map<String, String> product = new HashMap<>();
        product.put("product_id", productId);
        product.put("quantity", quantity);
        
        cart.add(product);
    }
    
    response.sendRedirect("products.jsp");
%>
