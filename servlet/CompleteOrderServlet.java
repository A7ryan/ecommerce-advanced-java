import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class CompleteOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        String dbURL = "jdbc:mysql://localhost:3306/music_db";
        String dbUsername = "root";
        String dbPassword = "";
        
        try (Connection cn = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
            // Start a transaction
            cn.setAutoCommit(false);

            // Check if cart is empty
            String checkCartQuery = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
            try (PreparedStatement checkStmt = cn.prepareStatement(checkCartQuery)) {
                checkStmt.setInt(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        response.sendRedirect("cart.jsp"); // No items in cart
                        return;
                    }
                }
            }

            // Copy cart items to the order table
            String insertOrderQuery = "INSERT INTO ordered_instruments (user_id, product_id, quantity, total_price, ordered_at) " +
                          "SELECT c.user_id, c.product_id, c.quantity, (c.quantity * p.product_price), NOW() " +
                          "FROM cart c " +
                          "JOIN products p ON c.product_id = p.product_id " +
                          "WHERE c.user_id = ?";

            try (PreparedStatement insertOrderStmt = cn.prepareStatement(insertOrderQuery)) {
                insertOrderStmt.setInt(1, userId);
                int rowsInserted = insertOrderStmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Cart items successfully copied to the orders table.");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to copy cart items to the order.");
                    cn.rollback();
                    return;
                }

                // Empty the user's cart
                String deleteCartQuery = "DELETE FROM cart WHERE user_id = ?";
                try (PreparedStatement deleteCartStmt = cn.prepareStatement(deleteCartQuery)) {
                    deleteCartStmt.setInt(1, userId);
                    int rowsDeleted = deleteCartStmt.executeUpdate();

                    if (rowsDeleted > 0) {
                        System.out.println("Cart successfully emptied.");
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to empty the cart.");
                        cn.rollback();
                        return;
                    }
                }

                // Commit transaction if all operations were successful
                cn.commit();

                // Redirect to orders page or confirmation page
                response.sendRedirect("orders.jsp");

            } catch (SQLException e) {
                cn.rollback();
                log("Error while processing order", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
            }

        } catch (Exception e) {
            log("Database connection error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
