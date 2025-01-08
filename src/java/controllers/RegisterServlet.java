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
import db.UsersDAO;

/**
 *
 * @author Neo
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String role = request.getParameter("role");

        if (name == null || email == null || password == null || phoneNumber == null || role == null) {
            response.sendRedirect("register.html?error=missing");
            return;
        }

        UsersDAO usersDAO = new UsersDAO();
        try {
            boolean isRegistered = usersDAO.registerUser(name, email, password, phoneNumber, role);
            if (isRegistered) {
                response.sendRedirect("index.html?success=registered");
            } else {
                response.sendRedirect("register.html?error=exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.html?error=server");
        }
    }
}
