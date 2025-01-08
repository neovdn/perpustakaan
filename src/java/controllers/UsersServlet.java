/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import db.UsersDAO;

/**
 *
 * @author Neo
 */
@WebServlet(name = "UsersServlet", urlPatterns = {"/UsersServlet"})
public class UsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsersDAO usersDAO = new UsersDAO();
        List<String> users = usersDAO.getAllUsers();

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Registered Users</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container mt-5'>");
            out.println("<h3>Registered Users</h3>");
            out.println("<table class='table table-bordered'>");
            out.println("<thead><tr><th>Name</th><th>Email</th><th>Role</th></tr></thead>");
            out.println("<tbody>");

            for (String user : users) {
                out.println(user);
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("<a href='register.html' class='btn btn-primary'>Back to Register</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
