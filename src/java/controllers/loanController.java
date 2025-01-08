/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;
import db.JDBC;
import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Neo
 */
@WebServlet(name = "loanController", urlPatterns = {"/loanController"})
public class loanController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("borrowBook".equals(action)) {
            borrowBook(request, response);
        } else if ("returnBook".equals(action)) {
            returnBook(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action: " + action);
        }
    }
    
    protected void borrowBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("book_id"));
        int userId = (int) request.getSession().getAttribute("user_id");

        // Mendapatkan waktu peminjaman sekarang dengan zona waktu
        Timestamp loanTimestamp = new Timestamp(System.currentTimeMillis());

        // Menghitung batas waktu pengembalian (misalnya, 3 hari dari sekarang)
        long threeDaysInMillis = 3L * 24 * 60 * 60 * 1000;
        Timestamp returnTimestamp = new Timestamp(loanTimestamp.getTime() + threeDaysInMillis);

        try (Connection conn = new JDBC().getConnection()) {
            // Memulai transaksi
            conn.setAutoCommit(false);

            // Menambahkan data peminjaman ke tabel loans
            String insertLoanQuery = "INSERT INTO loans (loan_date, return_date, id_user, book_id, is_returned) VALUES (?, ?, ?, ?, 0)";
            try (PreparedStatement stmt = conn.prepareStatement(insertLoanQuery)) {
                stmt.setTimestamp(1, loanTimestamp); // Waktu peminjaman
                stmt.setTimestamp(2, returnTimestamp); // Batas waktu pengembalian
                stmt.setInt(3, userId);
                stmt.setInt(4, bookId);
                stmt.executeUpdate();
            }

            // Mengupdate status buku menjadi tidak tersedia
            String updateBookQuery = "UPDATE books SET is_available = false WHERE id_book = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateBookQuery)) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
            }

            // Commit transaksi
            conn.commit();
            response.sendRedirect("borrow-books.jsp?message=book_borrowed");
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = new JDBC().getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            response.sendRedirect("borrow-books.jsp?error=failed_to_borrow_book");
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("viewAvailableBooks".equals(action)) {
            viewAvailableBooks(response);
        }
    }

    private void viewAvailableBooks(HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = new JDBC().getConnection()) {
            String query = "SELECT * FROM books WHERE is_available = true";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                StringBuilder jsonResponse = new StringBuilder();
                jsonResponse.append("[");
                while (rs.next()) {
                    jsonResponse.append("{")
                        .append("\"id_book\":").append(rs.getInt("id_book")).append(",")
                        .append("\"title\":\"").append(rs.getString("title")).append("\",")
                        .append("\"author\":\"").append(rs.getString("author")).append("\",")
                        .append("\"publisher\":\"").append(rs.getString("publisher")).append("\",")
                        .append("\"publication_year\":").append(rs.getInt("publication_year")).append(",")
                        .append("\"is_available\":").append(rs.getBoolean("is_available"))
                        .append("},");
                }
                if (jsonResponse.charAt(jsonResponse.length() - 1) == ',') {
                    jsonResponse.deleteCharAt(jsonResponse.length() - 1); // Menghapus koma terakhir
                }
                jsonResponse.append("]");
                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching available books.");
        }
    }
    
    protected void returnBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int loanId = Integer.parseInt(request.getParameter("loan_id"));
        int bookId = Integer.parseInt(request.getParameter("book_id"));

        try (Connection conn = new JDBC().getConnection()) {
            // Memulai transaksi
            conn.setAutoCommit(false);

            // Perbarui status peminjaman di tabel loans
            String updateLoanQuery = "UPDATE loans SET is_returned = 1 WHERE id_loan = ?";
            try (PreparedStatement loanStmt = conn.prepareStatement(updateLoanQuery)) {
                loanStmt.setInt(1, loanId);
                loanStmt.executeUpdate();
            }

            // Perbarui status ketersediaan buku di tabel books
            String updateBookQuery = "UPDATE books SET is_available = true WHERE id_book = ?";
            try (PreparedStatement bookStmt = conn.prepareStatement(updateBookQuery)) {
                bookStmt.setInt(1, bookId);
                bookStmt.executeUpdate();
            }

            // Commit transaksi
            conn.commit();
            response.sendRedirect("borrowed-books.jsp?message=book_returned");
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = new JDBC().getConnection()) {
                conn.rollback(); // Rollback jika terjadi error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            response.sendRedirect("borrowed-books.jsp?error=failed_to_return_book");
        }
    }
}
