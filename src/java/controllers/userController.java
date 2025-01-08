package controllers;

import db.JDBC;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "userController", urlPatterns = {"/userController"})
public class userController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        JDBC db = new JDBC();
        
        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = "member";  // Set default role as 'member'
            String phoneNumber = request.getParameter("phone_number");
            
            String query = "INSERT INTO users (name, email, password, role, phone_number) VALUES ('" 
                           + name + "', '" + email + "', '" + password + "', '" + role + "', '" + phoneNumber + "')";
            db.runQuery(query);
        } else if ("edit".equals(action)) {
            int id_user = Integer.parseInt(request.getParameter("id_user"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phoneNumber = request.getParameter("phone_number");
            
            String query = "UPDATE users SET name = '" + name + "', email = '" + email + "', password = '" + password
                         + "', phone_number = '" + phoneNumber + "' WHERE id_user = " + id_user;
            db.runQuery(query);
        } else if ("delete".equals(action)) {
            int id_user = Integer.parseInt(request.getParameter("id_user"));
            String query = "DELETE FROM users WHERE id_user = " + id_user;
            db.runQuery(query);
        }
        
        db.disconnect();
        response.sendRedirect("view-users.jsp");
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
