import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        Connection cn;
        PreparedStatement ps;
//        ResultSet rs;
        String query;
        
        String user_name = request.getParameter("user_name");
        String user_email = request.getParameter("user_email");
        String user_phone = request.getParameter("user_phone");
        String user_password = request.getParameter("user_password");
        
        
        String dbURL="jdbc:mysql://localhost:3306/music_db";
        String username="root";
        String password="";
        
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(dbURL, username, password);
            
//            query = "INSERT INTO users VALUES(?,?,?,?,?,?)";
            query = "INSERT INTO users (user_name, user_email, user_phone, user_password) VALUES (?, ?, ?, ?)";
            
            ps = cn.prepareStatement(query);
            
            ps.setString(1,user_name);
            ps.setString(2,user_email);
            ps.setString(3,user_phone);
            ps.setString(4,user_password);
            
            ps.executeUpdate();
            
            response.sendRedirect("login.html");
            
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
