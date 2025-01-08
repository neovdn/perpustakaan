package controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import db.BooksDAO;

@WebServlet(name = "BooksServlet", urlPatterns = {"/BooksServlet"})
public class BooksServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String publicationYearStr = request.getParameter("publicationYear");
        String totalBooksStr = request.getParameter("totalBooks");
        String isAvailableStr = request.getParameter("isAvailable");

        if (title == null || author == null || publisher == null ||
            publicationYearStr == null || totalBooksStr == null) {
            response.sendRedirect("manage-books.jsp?error=missing");
            return;
        }

        try {
            int publicationYear = Integer.parseInt(publicationYearStr);
            int totalBooks = Integer.parseInt(totalBooksStr);
            boolean isAvailable = (isAvailableStr != null) ? Boolean.parseBoolean(isAvailableStr) : true;

            BooksDAO booksDAO = new BooksDAO();
            boolean isAdded = booksDAO.addBook(title, author, publisher, publicationYear, totalBooks, isAvailable);

            if (isAdded) {
                response.sendRedirect("manage-books.jsp?success=added");
            } else {
                response.sendRedirect("manage-books.jsp?error=exists");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("manage-books.jsp?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("manage-books.jsp?error=server");
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BooksDAO booksDAO = new BooksDAO();
        List<String> books = booksDAO.getAllBooks();

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Books List</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container mt-5'>");
            out.println("<h3>Books List</h3>");
            out.println("<table class='table table-bordered'>");
            out.println("<thead><tr><th>ID</th><th>Title</th><th>Author</th><th>Publisher</th><th>Year</th><th>Total</th><th>Available</th></tr></thead>");
            out.println("<tbody>");

            for (String book : books) {
                out.println(book);
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("<a href='manage-books.jsp' class='btn btn-primary'>Back to Manage Books</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
