<%@page import="java.sql.ResultSet"%>
<%@page import="db.JDBC"%>
<%@page language="java"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar Pengguna</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container my-5">
        <h2 class="text-center mb-4">Daftar Pengguna (Member)</h2>

        <!-- Form Pencarian Pengguna -->
        <form method="get" action="view-users.jsp" class="mb-4">
            <div class="input-group">
                <input type="text" name="search" class="form-control" placeholder="Cari pengguna berdasarkan nama..."
                       value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                <button type="submit" class="btn btn-primary">Cari</button>
            </div
                </form>
                <br>
        <!-- Tabel Daftar Pengguna -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID User</th>
                    <th>Nama</th>
                    <th>Email</th>
                    <th>Nomor Telepon</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% 
                // Koneksi ke database dan menampilkan data pengguna berdasarkan pencarian
                JDBC db = new JDBC();
                if (db.isConnected) {
                    String searchQuery = "SELECT * FROM users WHERE role = 'member'";
                    String search = request.getParameter("search");
                    if (search != null && !search.trim().isEmpty()) {
                        searchQuery += " AND name LIKE '%" + search + "%'";
                    }
                    ResultSet rs = db.getData(searchQuery);
                    while (rs.next()) {
                %>
                <tr>
                    <td><%= rs.getInt("id_user") %></td>
                    <td><%= rs.getString("name") %></td>
                    <td><%= rs.getString("email") %></td>
                    <td><%= rs.getString("phone_number") %></td>
                    <td>
                        <!-- Button untuk menghapus pengguna -->
                        <form method="post" action="userController?action=delete">
                            <input type="hidden" name="id_user" value="<%= rs.getInt("id_user") %>">
                            <button type="submit" class="btn btn-danger btn-sm">Hapus</button>
                        </form>
                        <!-- Button untuk mengupdate pengguna -->
                        <button type="button" class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#updateUserModal"
                                data-id="<%= rs.getInt("id_user") %>" 
                                data-name="<%= rs.getString("name") %>" 
                                data-email="<%= rs.getString("email") %>" 
                                data-phone-number="<%= rs.getString("phone_number") %>">Update</button>
                    </td>
                </tr>
                <% 
                    }
                    db.disconnect();
                }
                %>
            </tbody>
        </table>

        <!-- Form untuk menambahkan pengguna baru -->
        <h4 class="mb-3">Tambah Pengguna Baru</h4>
        <form method="post" action="userController?action=add">
            <div class="mb-3">
                <label for="name" class="form-label">Nama</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <div class="mb-3">
                <label for="phone_number" class="form-label">Nomor Telepon</label>
                <input type="text" class="form-control" id="phone_number" name="phone_number" required>
            </div>
            <button type="submit" class="btn btn-primary">Tambah Pengguna</button>
        </form>
        
        <hr>
        <a href="admin.html" class="btn btn-primary">Kembali ke Halaman Admin</a>
        <hr>
        <a href="LogoutServlet" class="btn btn-danger">Logout</a>
    </div>
    
    <!-- Modal Update Pengguna -->
    <div class="modal fade" id="updateUserModal" tabindex="-1" aria-labelledby="updateUserModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="updateUserModalLabel">Update Pengguna</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="userController?action=update">
                        <input type="hidden" name="id_user" id="update-id">
                        <div class="mb-3">
                            <label for="update-name" class="form-label">Nama</label>
                            <input type="text" class="form-control" id="update-name" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="update-email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="update-email" name="email" required>
                        </div>
                        <div class="mb-3">
                            <label for="update-phone-number" class="form-label">Nomor Telepon</label>
                            <input type="text" class="form-control" id="update-phone-number" name="phone_number" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Update Pengguna</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Mengisi data pada modal update pengguna
        var updateUserModal = document.getElementById('updateUserModal');
        updateUserModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget; // Tombol yang memicu modal
            var id = button.getAttribute('data-id');
            var name = button.getAttribute('data-name');
            var email = button.getAttribute('data-email');
            var phoneNumber = button.getAttribute('data-phone-number');

            document.getElementById('update-id').value = id;
            document.getElementById('update-name').value = name;
            document.getElementById('update-email').value = email;
            document.getElementById('update-phone-number').value = phoneNumber;
        });
    </script>
</body>
</html>
    