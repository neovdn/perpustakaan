package models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private int totalBooks;
    private boolean isAvailable;

    // Constructor tanpa ID (untuk buku baru)
    public Book(String title, String author, String publisher, int publicationYear, int totalBooks, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.totalBooks = totalBooks;
        this.isAvailable = isAvailable;
    }

    // Constructor dengan ID (untuk buku yang sudah ada)
    public Book(int id, String title, String author, String publisher, int publicationYear, int totalBooks, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.totalBooks = totalBooks;
        this.isAvailable = isAvailable;
    }

    // Getters dan Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
