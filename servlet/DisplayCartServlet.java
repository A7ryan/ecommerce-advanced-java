import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayCartServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> cartItems = new ArrayList<>();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        String dbURL = "jdbc:mysql://localhost:3306/music_db";
        String dbUsername = "root";
        String dbPassword = "";

        String query = "SELECT c.cart_id, c.product_id, c.quantity, p.product_name, p.product_price " +
                       "FROM cart c " +
                       "JOIN products p ON c.product_id = p.product_id " +
                       "WHERE c.user_id = ?";

        try (Connection cn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = cn.prepareStatement(query)) {

            Class.forName("com.mysql.cj.jdbc.Driver");
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("productName", rs.getString("product_name"));
                    item.put("quantity", rs.getInt("quantity"));
                    item.put("productPrice", rs.getDouble("product_price"));
                    item.put("totalPrice", rs.getInt("quantity") * rs.getDouble("product_price")); // Calculate total
                    item.put("productId", rs.getString("product_id"));
                    cartItems.add(item);
                }
            }

            // Debugging Output
            System.out.println("Cart Items for User ID " + userId + ":");
            for (Map<String, Object> i : cartItems) {
                System.out.println(i);
            }

            if (cartItems.isEmpty()) {
                request.setAttribute("message", "Your cart is empty.");
            } else {
                request.setAttribute("cartItems", cartItems);
            }
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            log("Error while fetching cart items", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to display cart.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
