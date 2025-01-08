package db;

import java.sql.*;

/**
 * Kelas JDBC untuk mengelola koneksi database.
 */
public class JDBC {
    public Connection con;
    public Statement stmt;
    public ResultSet rs;
    public boolean isConnected;
    public String message;

    // Constructor untuk membuka koneksi
    public JDBC() {
        try {
            // Memuat driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuka koneksi ke database
            String url = "jdbc:mysql://localhost:3306/perpustakaan"; // Ganti dengan nama database Anda
            String user = "root";  // Username MySQL Anda
            String password = "";  // Password MySQL Anda

            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            isConnected = true;
            message = "Koneksi berhasil.";
            System.out.println(message);
        } catch (ClassNotFoundException e) {
            isConnected = false;
            message = "Driver tidak ditemukan: " + e.getMessage();
            System.err.println(message);
        } catch (SQLException e) {
            isConnected = false;
            message = "Koneksi database gagal: " + e.getMessage();
            System.err.println(message);
        }
    }

    // Method untuk menjalankan query INSERT, UPDATE, atau DELETE
    public void runQuery(String query) {
        try {
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Query berhasil dijalankan. Baris yang terpengaruh: " + rowsAffected);
        } catch (SQLException e) {
            System.err.println("Kesalahan eksekusi query: " + e.getMessage());
        }
    }

    // Method untuk mengambil data dari database (SELECT)
    public ResultSet getData(String query) {
        try {
            rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.err.println("Kesalahan eksekusi query: " + e.getMessage());
            return null;
        }
    }
    
    public Connection getConnection() {
        return con;
    }

    // Method untuk menutup koneksi ke database
    public void disconnect() {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
            message = "Koneksi berhasil ditutup.";
            System.out.println(message);
        } catch (SQLException e) {
            message = "Kesalahan saat menutup koneksi: " + e.getMessage();
            System.err.println(message);
        }
    }
    
    public ResultSet getPreparedData(String query, Object... params) throws SQLException {
        if (con == null || con.isClosed()) {
            throw new SQLException("Database connection is not available.");
        }

        PreparedStatement preparedStatement = con.prepareStatement(query);

        // Mengatur parameter pada PreparedStatement
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }

        return preparedStatement.executeQuery();
    }
}
