package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO extends DatabaseConnection {

    public boolean addBook(String title, String author, String publisher, int publicationYear, int totalBooks, boolean isAvailable) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM books WHERE title = ?";
        String insertQuery = "INSERT INTO books (title, author, publisher, publication_year, total_books, is_available) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Cek apakah buku dengan judul yang sama sudah ada
            checkStmt.setString(1, title);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Buku dengan judul ini sudah ada
            }

            // Menambahkan buku baru
            insertStmt.setString(1, title);
            insertStmt.setString(2, author);
            insertStmt.setString(3, publisher);
            insertStmt.setInt(4, publicationYear);
            insertStmt.setInt(5, totalBooks);
            insertStmt.setBoolean(6, isAvailable);
            insertStmt.executeUpdate();
            return true;
        }
    }



    public boolean updateBook(int id, String title, String author, String publisher, int publicationYear, int totalBooks, boolean isAvailable) {
        String query = "UPDATE books SET title = ?, author = ?, publisher = ?, publication_year = ?, total_books = ?, is_available = ? WHERE id_book = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setInt(4, publicationYear);
            stmt.setInt(5, totalBooks);
            stmt.setBoolean(6, isAvailable);
            stmt.setInt(7, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(int id) {
        String query = "DELETE FROM books WHERE id_book = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllBooks() {
        String query = "SELECT * FROM books";
        List<String> books = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                int idBook = resultSet.getInt("id_book");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                int publicationYear = resultSet.getInt("publication_year");
                int totalBooks = resultSet.getInt("total_books");
                boolean isAvailable = resultSet.getBoolean("is_available");

                String row = String.format(
                        "<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%d</td><td>%s</td></tr>",
                        idBook, title, author, publisher, publicationYear, totalBooks, isAvailable ? "Yes" : "No"
                );
                books.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<String> searchBooksByTitle(String title) {
        String query = "SELECT * FROM books WHERE title LIKE ?";
        List<String> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idBook = rs.getInt("id_book");
                String bookTitle = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int publicationYear = rs.getInt("publication_year");
                int totalBooks = rs.getInt("total_books");
                boolean isAvailable = rs.getBoolean("is_available");

                String row = String.format(
                        "<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%d</td><td>%s</td></tr>",
                        idBook, bookTitle, author, publisher, publicationYear, totalBooks, isAvailable ? "Yes" : "No"
                );
                books.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean checkAvailability(int id) {
        String query = "SELECT is_available FROM books WHERE id_book = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
