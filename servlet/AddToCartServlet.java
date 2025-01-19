import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class AddToCartServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        String dbURL = "jdbc:mysql://localhost:3306/music_db";
        String dbUsername = "root";
        String dbPassword = "";

        try (Connection cn = DriverManager.getConnection(dbURL, dbUsername, dbPassword)) {
            String checkQuery = "SELECT * FROM cart WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement psCheck = cn.prepareStatement(checkQuery)) {
                psCheck.setInt(1, userId);
                psCheck.setInt(2, productId);

                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        String updateQuery = "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
                        try (PreparedStatement psUpdate = cn.prepareStatement(updateQuery)) {
                            psUpdate.setInt(1, quantity);
                            psUpdate.setInt(2, userId);
                            psUpdate.setInt(3, productId);
                            psUpdate.executeUpdate();
                        }
                    } else {
                        String insertQuery = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement psInsert = cn.prepareStatement(insertQuery)) {
                            psInsert.setInt(1, userId);
                            psInsert.setInt(2, productId);
                            psInsert.setInt(3, quantity);
                            psInsert.executeUpdate();
                        }
                    }
                }
            }

//            response.sendRedirect("products.jsp");
            response.sendRedirect("DisplayCartServlet");

        } catch (Exception e) {
            response.sendRedirect("error.html");
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

//    @Override
//    public String getServletInfo() {
//        return "Add to Cart Servlet";
//    }
}
