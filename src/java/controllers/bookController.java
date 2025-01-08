package controllers;

import db.JDBC;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "bookController", urlPatterns = {"/bookController"})
public class bookController extends HttpServlet {

    // Method untuk memproses permintaan dari user
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                // Buat objek db untuk koneksi ke database
                JDBC db = new JDBC();
                // Cek koneksi database
                if (db.isConnected) {
                    // Menangani aksi yang diterima melalui parameter "action"
                    String action = request.getParameter("action");

                    // Menambah buku baru
                    if ("add".equals(action)) {
                        String title = request.getParameter("title");
                        String author = request.getParameter("author");
                        String publisher = request.getParameter("publisher");
                        int publicationYear = Integer.parseInt(request.getParameter("publication_year"));
                        int totalBooks = Integer.parseInt(request.getParameter("total_books"));
                        boolean isAvailable = Boolean.parseBoolean(request.getParameter("is_available"));

                        String query = "INSERT INTO books (title, author, publisher, publication_year, total_books, is_available) "
                                       + "VALUES ('" + title + "', '" + author + "', '" + publisher + "', " + publicationYear + ", " 
                                       + totalBooks + ", " + isAvailable + ")";
                        db.runQuery(query);

                    // Menghapus buku berdasarkan ID
                    } else if ("delete".equals(action)) {
                        int id = Integer.parseInt(request.getParameter("id_book"));
                        String query = "DELETE FROM books WHERE id_book = " + id;
                        db.runQuery(query);

                    // Mengupdate buku berdasarkan ID
                    } else if ("update".equals(action)) {
                        int id = Integer.parseInt(request.getParameter("id_book"));
                        String title = request.getParameter("title");
                        String author = request.getParameter("author");
                        String publisher = request.getParameter("publisher");
                        int publicationYear = Integer.parseInt(request.getParameter("publication_year"));
                        int totalBooks = Integer.parseInt(request.getParameter("total_books"));
                        boolean isAvailable = Boolean.parseBoolean(request.getParameter("is_available"));

                        String query = "UPDATE books SET title = '" + title + "', author = '" + author + "', "
                                       + "publisher = '" + publisher + "', publication_year = " + publicationYear + ", "
                                       + "total_books = " + totalBooks + ", is_available = " + isAvailable + " WHERE id_book = " + id;
                        db.runQuery(query);
                    }

                    // Setelah operasi selesai, arahkan kembali ke halaman index.jsp
                    response.sendRedirect("view-books.jsp");

                }
            } catch (Exception e) {
                out.println("Terjadi kesalahan saat memproses data.");
                e.printStackTrace(out);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
