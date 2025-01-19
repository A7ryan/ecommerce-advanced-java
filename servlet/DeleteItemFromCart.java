import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class DeleteItemFromCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productId = request.getParameter("product_id");
        String quantityStr = request.getParameter("quantity");
        String totalQuantityStr = request.getParameter("total_quantity");
        
        
        if(productId == null || productId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is null");
            return;
        } else if(quantityStr == null || quantityStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity is null");
            return;
        } else if(totalQuantityStr == null || totalQuantityStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Total QUanirity is null");
            return;            
        }

        int totalQtyInInt = Integer.parseInt(totalQuantityStr);
        int quantity = Integer.parseInt(quantityStr);

        // Calculate the updated quantity
        int updatedQuantity = totalQtyInInt - quantity;

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        // Check if the user is logged in
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Database connection details
        String dbURL = "jdbc:mysql://localhost:3306/music_db";
        String dbUsername = "root";
        String dbPassword = "";

        String query;

        try (Connection cn = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {

            if (updatedQuantity <= 0) {
                // If the updated quantity is zero or less, delete the item from the cart
                query = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
                try (PreparedStatement ps = cn.prepareStatement(query)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, Integer.parseInt(productId));
                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Item removed from cart.");
                    } else {
                        System.out.println("Item not found in cart.");
                    }
                }
            } else {
                // If the updated quantity is positive, update the item quantity
                query = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
                try (PreparedStatement ps = cn.prepareStatement(query)) {
                    ps.setInt(1, updatedQuantity);
                    ps.setInt(2, userId);
                    ps.setInt(3, Integer.parseInt(productId));
                    int rows = ps.executeUpdate();

                    if (rows > 0) {
//                        System.out.println("Updated " + quantity + " quantity in cart.");
                    } else {
                        System.out.println("Item not found in cart.");
                    }
                }
            }

            // Redirect back to the cart page after the operation
            response.sendRedirect("DisplayCartServlet");

        } catch (SQLException e) {
            log("Error while updating or removing item from cart", e);
            response.sendRedirect("error.html"); // Redirect to a specific error page
        }
    }
}
