package controllers;

import db.JDBC;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "payPenaltyController", urlPatterns = {"/payPenaltyController"})
public class payPenaltyController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int penaltyId = Integer.parseInt(request.getParameter("penalty_id"));

        try (Connection conn = new JDBC().getConnection()) {
            String updateQuery = "UPDATE penalties SET penalty_status = true WHERE penalty_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, penaltyId);
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    response.sendRedirect("penalties.jsp?message=penalty_paid");
                } else {
                    response.sendRedirect("penalties.jsp?error=penalty_not_found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("penalties.jsp?error=database_error");
        }
    }
}