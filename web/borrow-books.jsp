<%@page import="java.sql.ResultSet"%>
<%@page import="db.JDBC"%>
<%@page import="exceptions.BookNotAvailableException"%>
<%@page language="java"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Peminjaman Buku</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container my-5">
        <h2 class="text-center mb-4">Peminjaman Buku</h2>

        <!-- Form Pencarian Buku -->
        <form method="get" action="borrow-books.jsp" class="mb-4">
            <div class="input-group">
                <input type="text" name="search" class="form-control" placeholder="Cari buku berdasarkan judul..." 
                       value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                <button type="submit" class="btn btn-primary">Cari</button>
            </div>
        </form>

        <!-- Tabel Buku yang Tersedia untuk Dipinjam -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Judul</th>
                    <th>Penulis</th>
                    <th>Penerbit</th>
                    <th>Tahun Terbit</th>
                    <th>Total Buku</th>
                    <th>Tersedia</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% 
                // Koneksi ke database dan menampilkan buku yang tersedia
                JDBC db = new JDBC();
                boolean isBookFound = false;

                try {
                    if (db.isConnected) {
                        String searchQuery = "SELECT * FROM books WHERE is_available = 1";
                        String search = request.getParameter("search");
                        if (search != null && !search.trim().isEmpty()) {
                            searchQuery += " AND title LIKE '%" + search + "%'";
                        }
                        ResultSet rs = db.getData(searchQuery);

                        if (!rs.isBeforeFirst()) {
                            // Jika tidak ada hasil, lempar exception
                            throw new BookNotAvailableException("Buku tidak ditemukan atau tidak tersedia.");
                        }

                        while (rs.next()) {
                            isBookFound = true;
                %>
                <tr>
                    <td><%= rs.getInt("id_book") %></td>
                    <td><%= rs.getString("title") %></td>
                    <td><%= rs.getString("author") %></td>
                    <td><%= rs.getString("publisher") %></td>
                    <td><%= rs.getInt("publication_year") %></td>
                    <td><%= rs.getInt("total_books") %></td>
                    <td>Ya</td>
                    <td>
                        <!-- Button untuk meminjam buku -->
                        <form method="post" action="loanController">
                            <input type="hidden" name="book_id" value="<%= rs.getInt("id_book") %>">
                            <input type="hidden" name="action" value="borrowBook">
                            <button type="submit" class="btn btn-success btn-sm">Pinjam</button>
                        </form>
                    </td>
                </tr>
                <%
                        }
                        db.disconnect();
                    }
                } catch (BookNotAvailableException e) {
                %>
                    <tr>
                        <td colspan="8" class="text-center text-danger">
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
        <hr>
        <a href="LogoutServlet" class="btn btn-danger">Logout</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
