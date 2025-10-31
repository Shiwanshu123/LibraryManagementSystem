import java.sql.Date;

/**
 * Represents a Book. This is a "POJO" (Plain Old Java Object).
 * Its main job is to hold data.
 * This is part of our "Business Logic Layer."
 */
public class Book {

    // --- Properties ---
    private String isbn;
    private String title;
    private String author;
    private String publication;
    private boolean isAvailable;
    private Date dueDate;
    private String borrowerId;

    // --- Constructor ---
    public Book(String isbn, String title, String author, String publication) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.isAvailable = true; // New books are available by default
    }

    // --- Getters and Setters ---
    // These allow other classes to safely access and change the properties.

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    // --- toString() Method ---
    // This gives us a nice, readable string when we print a Book object.
    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Checked Out (Due: " + dueDate + ")";
        return String.format("ISBN: %s | Title: %s | Author: %s | Status: %s",
                isbn, title, author, status);
    }
}

