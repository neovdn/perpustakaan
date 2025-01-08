package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsersDAO extends DatabaseConnection {
    public boolean registerUser(String name, String email, String password, String phoneNumber, String role) throws Exception {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        String insertQuery = "INSERT INTO users (name, email, password, phone_number, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Cek apakah email sudah terdaftar
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Email sudah terdaftar
            }

            // Daftarkan pengguna baru
            insertStmt.setString(1, name);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);
            insertStmt.setString(4, phoneNumber);
            insertStmt.setString(5, role);
            insertStmt.executeUpdate();
            return true;
        }
    }
    
    public List<String> getAllUsersByRole(String role) {
        String query = "SELECT * FROM users WHERE role = ?";
        List<String> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public String validateLogin(String email, String password) {
        String query = "SELECT role FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // Mengembalikan peran (admin/member)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Jika tidak ada user yang cocok
    }
    
    public List<String> getAllUsers() {
        String query = "SELECT name, email, role FROM users";
        List<String> users = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");

                users.add("<tr><td>" + name + "</td><td>" + email + "</td><td>" + role + "</td></tr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
