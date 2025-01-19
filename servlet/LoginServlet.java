import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        Connection cn;
        PreparedStatement ps;
        ResultSet rs;
        String query;
        
        String user_email = request.getParameter("user_email");
        String user_password = request.getParameter("user_password");
        
        
        String dbURL="jdbc:mysql://localhost:3306/music_db";
        String username="root";
        String password="";
        
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(dbURL, username, password);
            
            query = "SELECT * FROM users WHERE user_email = ? AND user_password = ?";

            ps = cn.prepareStatement(query);
            ps.setString(1, user_email);
            ps.setString(2, user_password);

            rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("username", rs.getString("user_name"));
                session.setAttribute("user_id", rs.getInt("user_id"));
                response.sendRedirect("products.jsp");
            } else {                
                response.sendRedirect("unauthorizedUser.html");
            }
        }
        catch(Exception e)
        {
                System.out.println(e.getMessage());    
        }
        
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
