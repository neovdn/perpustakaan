<%@page import="java.sql.*"%>
<%@page import="db.JDBC"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Penalties</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container my-5">
        <h2 class="text-center mb-4">Daftar Denda</h2>

        <!-- Tabel Penalties -->
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>Judul Buku</th>
                    <th>Tanggal Peminjaman</th>
                    <th>Tanggal Pengembalian</th>
                    <th>Jumlah Denda</th>
                    <th>Status Denda</th>
                    <th>Aksi</th>
                </tr>
            </thead>
            <tbody>
                <%
                    int userId = (int) session.getAttribute("user_id");

                    // Simulasi batas waktu 3 hari (gunakan parameter tambahan untuk memajukan waktu)
                    // Tambahkan parameter simulasi_time_hours ke URL, contoh: penalties.jsp?simulate_time_hours=72
                    String simulateTimeParam = request.getParameter("simulate_time_hours");
                    int simulateTimeHours = (simulateTimeParam != null) ? Integer.parseInt(simulateTimeParam) : 0;

                    try (Connection conn = new JDBC().getConnection()) {
                        String query = 
                            "SELECT p.penalty_id, b.title, l.loan_date, l.return_date, " +
                            "p.penalty_amount, p.penalty_status, " +
                            "CASE WHEN TIMESTAMPADD(HOUR, ?, l.return_date) < NOW() THEN true ELSE false END AS is_late " +
                            "FROM penalties p " +
                            "JOIN loans l ON p.id_loan = l.id_loan " +
                            "JOIN books b ON l.book_id = b.id_book " +
                            "WHERE l.id_user = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setInt(1, simulateTimeHours); // Menambahkan waktu simulasi
                            stmt.setInt(2, userId);
                            ResultSet rs = stmt.executeQuery();
                            while (rs.next()) {
                                int penaltyId = rs.getInt("penalty_id");
                                String title = rs.getString("title");
                                Date loanDate = rs.getDate("loan_date");
                                Date returnDate = rs.getDate("return_date");
                                double penaltyAmount = rs.getDouble("penalty_amount");
                                boolean penaltyStatus = rs.getBoolean("penalty_status");
                                boolean isLate = rs.getBoolean("is_late");
                %>
                <tr>
                    <td><%= title %></td>
                    <td><%= loanDate %></td>
                    <td><%= returnDate %></td>
                    <td>Rp<%= penaltyAmount %></td>
                    <td>
                        <% if (penaltyStatus) { %>
                            <span class="badge bg-success">Paid</span>
                        <% } else if (isLate) { %>
                            <span class="badge bg-danger">Pending</span>
                        <% } else { %>
                            <span class="badge bg-warning">Dalam Tenggat</span>
                        <% } %>
                    </td>
                    <td>
                        <% if (!penaltyStatus && isLate) { %>
                            <form method="post" action="payPenaltyController">
                                <input type="hidden" name="penalty_id" value="<%= penaltyId %>">
                                <button type="submit" class="btn btn-primary btn-sm">Bayar</button>
                            </form>
                        <% } else { %>
                            <span>-</span>
                        <% } %>
                    </td>
                </tr>
                <%
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                %>
            </tbody>
        </table>

        <hr>
        <div class="d-flex justify-content-between">
            <a href="member.html" class="btn btn-primary">Kembali ke Halaman Member</a>
            <a href="LogoutServlet" class="btn btn-danger">Logout</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
