/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import db.JDBC;

/**
 *
 * @author Neo
 */
@WebServlet(name = "registerController", urlPatterns = {"/registerController"})
public class registerController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phone_number");
        String role = request.getParameter("role");
        
        JDBC db = new JDBC();
        if (db.isConnected) {
            try {
                var conn = db.getConnection(); // Mendapatkan koneksi
                String query = "INSERT INTO users (name, email, password, phone_number, role) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, phoneNumber);
                stmt.setString(5, role);
                
                int result = stmt.executeUpdate();
                if (result > 0) {
                    response.sendRedirect("index.jsp?message=register_success");
                } else {
                    response.sendRedirect("register.jsp?error=registration_failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.disconnect();
            }
        }
    }
}
