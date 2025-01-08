<%@page import="java.sql.ResultSet"%>
<%@page import="db.JDBC"%>
<%@page language="java"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buku yang Dipinjam</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container my-5">
        <h2 class="text-center mb-4">Buku yang Sedang Dipinjam</h2>

        <!-- Form Pencarian Buku yang Dipinjam -->
        <form method="get" action="borrowed-books.jsp" class="mb-4">
            <div class="input-group">
                <input type="text" name="search" class="form-control" placeholder="Cari buku berdasarkan judul..." 
                       value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                <button type="submit" class="btn btn-primary">Cari</button>
            </div>
        </form>

        <!-- Tabel Buku yang Dipinjam -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID Buku</th>
                    <th>Judul</th>
                    <th>Penulis</th>
                    <th>Tanggal Peminjaman</th>
                    <th>Batas Pengembalian</th>
                    <th>Status</th>
                    <th>Aksi</th>
                </tr>
            </thead>
            <tbody>
                <% 
                int userId = (int) session.getAttribute("user_id");
                JDBC db = new JDBC();
                boolean isBorrowedBooksFound = false;

                try {
                    if (db.isConnected) {
                        String searchQuery = 
                            "SELECT l.id_loan, b.id_book, b.title, b.author, l.loan_date, " +
                            "l.return_date, " +
                            "CASE WHEN NOW() > l.return_date THEN 'Terlambat' ELSE 'Dalam Batas Waktu' END AS status " +
                            "FROM loans l " +
                            "JOIN books b ON l.book_id = b.id_book " +
                            "WHERE l.id_user = ? AND l.is_returned = 0";

                        String search = request.getParameter("search");
                        if (search != null && !search.trim().isEmpty()) {
                            searchQuery += " AND b.title LIKE '%" + search + "%'";
                        }

                        ResultSet rs = db.getPreparedData(searchQuery, userId);

                        if (!rs.isBeforeFirst()) {
                            throw new Exception("Tidak ada buku yang sedang dipinjam.");
                        }

                        while (rs.next()) {
                            isBorrowedBooksFound = true;
                %>
                <tr>
                    <td><%= rs.getInt("id_book") %></td>
                    <td><%= rs.getString("title") %></td>
                    <td><%= rs.getString("author") %></td>
                    <td><%= rs.getTimestamp("loan_date") %></td>
                    <td><%= rs.getTimestamp("return_date") %></td>
                    <td>
                        <% if ("Terlambat".equals(rs.getString("status"))) { %>
                            <span class="badge bg-danger">Terlambat</span>
                        <% } else { %>
                            <span class="badge bg-success">Dalam Batas Waktu</span>
                        <% } %>
                    </td>
                    <td>
                        <form action="loanController" method="post">
                            <input type="hidden" name="action" value="returnBook">
                            <input type="hidden" name="loan_id" value="<%= rs.getInt("id_loan") %>">
                            <input type="hidden" name="book_id" value="<%= rs.getInt("id_book") %>">
                            <button type="submit" class="btn btn-warning btn-sm">Kembalikan</button>
                        </form>
                    </td>
                </tr>
                <% 
                        }
                        db.disconnect();
                    }
                } catch (Exception e) {
                %>
                <tr>
                    <td colspan="7" class="text-center text-danger">
                        <%= e.getMessage() %>
                    </td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>

        <hr>
        <a href="member.html" class="btn btn-primary">Kembali ke Halaman Member</a>
        <a href="LogoutServlet" class="btn btn-danger ms-2">Logout</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
