/**
 * Represents a Librarian.
 * This class has methods for administrative tasks like adding/removing books.
 * This is part of our "Business Logic Layer."
 */
public class Librarian {

    // --- Properties ---
    private String id;
    private String name;
    private String password; // In a real app, this should be a secure hash

    // --- Constructor ---
    public Librarian(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    // --- Public Methods (Getters) ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    // --- Librarian-Specific Actions ---

    /**
     * Adds a new book to the library.
     * This method calls the database singleton.
     * @param db The LibraryDatabase instance.
     * @param book The Book object to add.
     */
    public void addBook(LibraryDatabase db, Book book) {
        if (db.addBook(book)) {
            System.out.println("Book added successfully: " + book.getTitle());
        } else {
            System.out.println("Failed to add book.");
        }
    }

    /**
     * Removes a book from the library by its ISBN.
     * This method calls the database singleton.
     * @param db The LibraryDatabase instance.
     * @param isbn The ISBN of the book to remove.
     */
    public void removeBook(LibraryDatabase db, String isbn) {
        if (db.removeBook(isbn)) {
            System.out.println("Book removed successfully (ISBN: " + isbn + ")");
        } else {
            System.out.println("Failed to remove book. Check ISBN.");
        }
    }
}

