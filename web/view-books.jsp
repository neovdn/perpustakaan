<%@page import="java.sql.ResultSet"%>
<%@page import="db.JDBC"%>
<%@page language="java"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar Buku</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container my-5">
        <h2 class="text-center mb-4">Daftar Buku</h2>

        <!-- Form Pencarian Buku -->
        <form method="get" action="view-books.jsp" class="mb-4">
            <div class="input-group">
                <input type="text" name="search" class="form-control" placeholder="Cari buku berdasarkan judul..." 
                       value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                <button type="submit" class="btn btn-primary">Cari</button>
            </div>
        </form>

        <!-- Tabel Daftar Buku -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Judul</th>
                    <th>Penulis</th>
                    <th>Penerbit</th>
                    <th>Tahun Terbit</th>
                    <th>Total Buku</th>
                    <th>Terbuka</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% 
                // Koneksi ke database dan menampilkan data buku
                JDBC db = new JDBC();
                if (db.isConnected) {
                    String searchQuery = "SELECT * FROM books";
                    String search = request.getParameter("search");
                    if (search != null && !search.trim().isEmpty()) {
                        searchQuery += " WHERE title LIKE '%" + search + "%'";
                    }
                    ResultSet rs = db.getData(searchQuery);
                    while (rs.next()) {
                %>
                <tr>
                    <td><%= rs.getInt("id_book") %></td>
                    <td><%= rs.getString("title") %></td>
                    <td><%= rs.getString("author") %></td>
                    <td><%= rs.getString("publisher") %></td>
                    <td><%= rs.getInt("publication_year") %></td>
                    <td><%= rs.getInt("total_books") %></td>
                    <td><%= rs.getBoolean("is_available") ? "Ya" : "Tidak" %></td>
                    <td>
                        <!-- Button untuk menghapus buku -->
                        <form method="post" action="bookController?action=delete">
                            <input type="hidden" name="id_book" value="<%= rs.getInt("id_book") %>">
                            <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                        </form>
                        <!-- Button untuk mengupdate buku -->
                        <button type="button" class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#updateBookModal"
                                data-id="<%= rs.getInt("id_book") %>" 
                                data-title="<%= rs.getString("title") %>" 
                                data-author="<%= rs.getString("author") %>" 
                                data-publisher="<%= rs.getString("publisher") %>" 
                                data-publication-year="<%= rs.getInt("publication_year") %>"
                                data-total-books="<%= rs.getInt("total_books") %>"
                                data-is-available="<%= rs.getBoolean("is_available") %>">Update</button>
                    </td>
                </tr>
                <% 
                    }
                    db.disconnect();
                }
                %>
            </tbody>
        </table>
        
        <!-- Form untuk menambahkan buku baru -->
        <h4 class="mb-3">Tambah Buku Baru</h4>
        <form method="post" action="bookController?action=add">
            <div class="mb-3">
                <label for="title" class="form-label">Judul Buku</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>
            <div class="mb-3">
                <label for="author" class="form-label">Penulis</label>
                <input type="text" class="form-control" id="author" name="author" required>
            </div>
            <div class="mb-3">
                <label for="publisher" class="form-label">Penerbit</label>
                <input type="text" class="form-control" id="publisher" name="publisher" required>
            </div>
            <div class="mb-3">
                <label for="publication_year" class="form-label">Tahun Terbit</label>
                <input type="number" class="form-control" id="publication_year" name="publication_year" required>
            </div>
            <div class="mb-3">
                <label for="total_books" class="form-label">Total Buku</label>
                <input type="number" class="form-control" id="total_books" name="total_books" required>
            </div>
            <div class="mb-3">
                <label for="is_available" class="form-label">Tersedia?</label>
                <select class="form-select" id="is_available" name="is_available" required>
                    <option value="true">Ya</option>
                    <option value="false">Tidak</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Tambah Buku</button>
        </form>
        
        <hr>
        <a href="admin.html" class="btn btn-primary">Kembali ke Halaman Admin</a>
        <hr>
        <a href="LogoutServlet" class="btn btn-danger">Logout</a>
    </div>
    <!-- Modal Update Buku -->
    <div class="modal fade" id="updateBookModal" tabindex="-1" aria-labelledby="updateBookModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="updateBookModalLabel">Update Buku</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="bookController?action=update">
                        <input type="hidden" name="id_book" id="update-id">
                        <div class="mb-3">
                            <label for="update-title" class="form-label">Judul Buku</label>
                            <input type="text" class="form-control" id="update-title" name="title" required>
                        </div>
                        <div class="mb-3">
                            <label for="update-author" class="form-label">Penulis</label>
                            <input type="text" class="form-control" id="update-author" name="author" required>
                        </div>
                        <div class="mb-3">
                            <label for="update-publisher" class="form-label">Penerbit</label>
                            <input type="text" class="form-control" id="update-publisher" name="publisher" required>
                        </div>
                        <div class="mb-3">
                            <label for="update-publication-year" class="form-label">Tahun Terbit</label>
                            <input type="number" class="form-control" id="update-publication-year" name="publication_year" required>
                        </div>
                        <div class="mb-3">
                            <label for="update-total-books" class="form-label">Total Buku</label>
                            <input type="number" class="form-control" id="update-total-books" name="total_books" required>
                        </div>
                        <div class="mb-3">
                            <label for="update-is-available" class="form-label">Tersedia?</label>
                            <select class="form-select" id="update-is-available" name="is_available" required>
                                <option value="true">Ya</option>
                                <option value="false">Tidak</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Update Buku</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Mengisi data pada modal update buku
        var updateBookModal = document.getElementById('updateBookModal');
        updateBookModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget; // Tombol yang memicu modal
            var id = button.getAttribute('data-id');
            var title = button.getAttribute('data-title');
            var author = button.getAttribute('data-author');
            var publisher = button.getAttribute('data-publisher');
            var publicationYear = button.getAttribute('data-publication-year');
            var totalBooks = button.getAttribute('data-total-books');
            var isAvailable = button.getAttribute('data-is-available');

            document.getElementById('update-id').value = id;
            document.getElementById('update-title').value = title;
            document.getElementById('update-author').value = author;
            document.getElementById('update-publisher').value = publisher;
            document.getElementById('update-publication-year').value = publicationYear;
            document.getElementById('update-total-books').value = totalBooks;
            document.getElementById('update-is-available').value = isAvailable;
        });
    </script>
</body>
</html>
