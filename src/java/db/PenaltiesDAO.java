package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenaltiesDAO {

    public boolean addPenalty(String penaltyId, String loanId, double penaltyAmount, boolean penaltyStatus) {
        String query = "INSERT INTO penalties (penalty_id, loan_id, penalty_amount, penalty_status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, penaltyId);
            stmt.setString(2, loanId);
            stmt.setDouble(3, penaltyAmount);
            stmt.setBoolean(4, penaltyStatus);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePenaltyStatus(String penaltyId, boolean penaltyStatus) {
        String query = "UPDATE penalties SET penalty_status = ? WHERE penalty_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, penaltyStatus);
            stmt.setString(2, penaltyId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getPenaltiesByLoanId(String loanId) {
        String query = "SELECT * FROM penalties WHERE loan_id = ?";
        List<String> penalties = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, loanId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                penalties.add("Penalty ID: " + rs.getString("penalty_id") + ", Amount: " + rs.getDouble("penalty_amount") + ", Status: " + rs.getBoolean("penalty_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return penalties;
    }

    public boolean deletePenalty(String penaltyId) {
        String query = "DELETE FROM penalties WHERE penalty_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, penaltyId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
