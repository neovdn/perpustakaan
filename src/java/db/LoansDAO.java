package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoansDAO {

    public boolean addLoan(String idLoan, String bookId, String memberId, Date loanDate, Date returnDate, boolean isReturned) {
        String query = "INSERT INTO loans (id_loan, book_id, member_id, loan_date, return_date, is_returned) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idLoan);
            stmt.setString(2, bookId);
            stmt.setString(3, memberId);
            stmt.setDate(4, loanDate);
            stmt.setDate(5, returnDate);
            stmt.setBoolean(6, isReturned);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLoanReturnStatus(String idLoan, boolean isReturned) {
        String query = "UPDATE loans SET is_returned = ? WHERE id_loan = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, isReturned);
            stmt.setString(2, idLoan);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getLoansByMemberId(String memberId) {
        String query = "SELECT * FROM loans WHERE member_id = ?";
        List<String> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                loans.add("Loan ID: " + rs.getString("id_loan") + ", Book ID: " + rs.getString("book_id") + ", Return Date: " + rs.getDate("return_date") + ", Returned: " + rs.getBoolean("is_returned"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public boolean deleteLoan(String idLoan) {
        String query = "DELETE FROM loans WHERE id_loan = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idLoan);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
