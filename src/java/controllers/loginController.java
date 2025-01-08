/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.http.HttpSession;
import db.JDBC;
/**
 *
 * @author Neo
 */
@WebServlet(name = "loginController", urlPatterns = {"/loginController"})

public class loginController extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        JDBC db = new JDBC();
        if (db.isConnected) {
            try {
                var conn = db.getConnection(); // Mendapatkan koneksi
                String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", rs.getInt("id_user"));
                    session.setAttribute("name", rs.getString("name"));
                    session.setAttribute("email", rs.getString("email"));
                    session.setAttribute("role", rs.getString("role"));

                    String role = rs.getString("role");  // Ambil role dari database

                    // Redirect berdasarkan role
                    if ("admin".equals(role)) {
                        response.sendRedirect("admin.html"); // Redirect ke admin jika role admin
                    } else if ("member".equals(role)) {
                        response.sendRedirect("member.html"); // Redirect ke member jika role member
                    } else {
                        response.sendRedirect("index.jsp?error=invalid_role"); // Error jika role tidak valid
                    }
                } else {
                    response.sendRedirect("index.jsp?error=invalid_credentials"); // Error jika kredensial tidak valid
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.disconnect();
            }
        }
    }
}