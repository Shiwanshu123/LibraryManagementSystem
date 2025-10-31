import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage all database interactions for the library.
 * This is the ONLY class in the system that directly executes SQL queries.
 * This is our "Data Access Layer."
 */
public class LibraryDatabase {

    // --- Singleton Pattern ---
    // 1. A private static variable to hold the one-and-only instance
    private static LibraryDatabase instance;

    // 2. A private variable for the database connection
    private Connection connection;

    // 3. A private constructor. No one else can 'new' this class.
    private LibraryDatabase() {
        this.connection = DatabaseConnector.getConnection();
        if (this.connection == null) {
            System.err.println("FATAL ERROR: LibraryDatabase could not connect.");
            // In a real app, you might exit or throw a runtime exception
        }
    }

    // 4. The public "getter" for the instance. This is how everyone gets it.
    public static synchronized LibraryDatabase getInstance() {
        if (instance == null) {
            instance = new LibraryDatabase();
        }
        return instance;
    }
    // --- End Singleton Pattern ---


    // --- Book Management Methods ---

    /**
     * Adds a new book to the database.
     * Uses a PreparedStatement to prevent SQL Injection.
     * @param book The Book object to add.
     * @return true if successful, false otherwise.
     */
    public boolean addBook(Book book) {
        // The '?' are placeholders. This is a PreparedStatement.
        String sql = "INSERT INTO books (isbn, title, author, publication) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // We set the values for the '?' placeholders.
            // This is SAFE from SQL Injection.
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublication());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Returns true if the insert worked (1 row)

        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for books by title.
     * @param title The title (or partial title) to search for.
     * @return A List of matching Book objects.
     */
    public List<Book> searchBookByTitle(String title) {
        List<Book> books = new ArrayList<>();
        // We use 'LIKE' and '%' to find partial matches
        String sql = "SELECT * FROM books WHERE title LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + title + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publication")
                    );
                    book.setAvailable(rs.getBoolean("is_available"));
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching for book: " + e.getMessage());
        }
        return books;
    }

    /**
     * Gets a single book by its exact ISBN.
     * @param isbn The ISBN to find.
     * @return A Book object, or null if not found.
     */
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, isbn);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publication")
                    );
                    book.setAvailable(rs.getBoolean("is_available"));
                    book.setDueDate(rs.getDate("due_date"));
                    book.setBorrowerId(rs.getString("borrower_id"));
                    return book;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ISBN: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates a book's record, typically for checking out or returning.
     * @param book The Book object with updated information.
     * @return true if successful, false otherwise.
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET is_available = ?, due_date = ?, borrower_id = ? WHERE isbn = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, book.isAvailable());
            pstmt.setDate(2, book.getDueDate());
            pstmt.setString(3, book.getBorrowerId());
            pstmt.setString(4, book.getIsbn());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetches all books in the database.
     * @return A List of all Book objects.
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publication")
                );
                book.setAvailable(rs.getBoolean("is_available"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
        }
        return books;
    }


    // --- User/Librarian Authentication Methods ---

    /**
     * Validates a User's login credentials.
     * @param id The user's ID.
     * @param password The user's password.
     * @return A User object if successful, or null if login fails.
     */
    public User validateUser(String id, String password) {
        String sql = "SELECT * FROM users WHERE id = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userType = rs.getString("user_type");
                    String name = rs.getString("name");
                    String detail = rs.getString("detail");
                    double fine = rs.getDouble("fine_amount");

                    // Use the Strategy Pattern!
                    if ("student".equalsIgnoreCase(userType)) {
                        return new Student(id, name, password, detail, fine);
                    } else if ("staff".equalsIgnoreCase(userType)) {
                        return new Staff(id, name, password, detail, fine);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating user: " + e.getMessage());
        }
        return null; // Login failed
    }

    /**
     * Validates a Librarian's login credentials.
     * @param id The librarian's ID.
     * @param password The librarian's password.
     * @return A Librarian object if successful, or null if login fails.
     */
    public Librarian validateLibrarian(String id, String password) {
        String sql = "SELECT * FROM librarians WHERE id = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Librarian(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating librarian: " + e.getMessage());
        }
        return null; // Login failed
    }

    /**
     * Registers a new User (Student or Staff) in the database.
     * @param user The User object to register.
     * @return true if successful, false otherwise.
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (id, name, password, user_type, detail) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword()); // Note: In a real app, hash this!
            pstmt.setString(4, user.getUserType());
            pstmt.setString(5, user.getDetail());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registers a new Librarian in the database.
     * @param librarian The Librarian object to register.
     * @return true if successful, false otherwise.
     */
    public boolean registerLibrarian(Librarian librarian) {
        String sql = "INSERT INTO librarians (id, name, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, librarian.getId());
            pstmt.setString(2, librarian.getName());
            pstmt.setString(3, librarian.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error registering librarian: " + e.getMessage());
            return false;
        }
    }

    // --- Account/Fine Management Methods ---

    /**
     * Gets a user's account details (borrowed books).
     * @param userId The ID of the user.
     * @return An Account object.
     */
    public Account getAccount(String userId) {
        Account account = new Account();
        String sql = "SELECT * FROM books WHERE borrower_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publication")
                    );
                    book.setAvailable(false);
                    book.setDueDate(rs.getDate("due_date"));
                    book.setBorrowerId(userId);

                    account.addBorrowedBook(book); // Add to the list
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting account details: " + e.getMessage());
        }

        // We will get the fine amount from the user table itself
        // A more complex design might have a separate 'fines' table

        return account;
    }

    /**
     * Updates a user's fine amount in the users table.
     * @param userId The ID of the user.
     * @param newFineAmount The new total fine.
     * @return true if successful, false otherwise.
     */
    public boolean updateUserFine(String userId, double newFineAmount) {
        String sql = "UPDATE users SET fine_amount = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newFineAmount);
            pstmt.setString(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating fine: " + e.getMessage());
            return false;
        }
    }

    // --- NEW METHODS TO ADD ---

    /**
     * (Helper for Admin check) Finds a librarian by ID without password.
     * @param id The librarian's ID.
     * @return A Librarian object or null.
     */
    public Librarian findLibrarianById(String id) {
        String sql = "SELECT * FROM librarians WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Librarian(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding librarian: " + e.getMessage());
        }
        return null;
    }

    /**
     * (Helper for Menu) Prints all books to the console.
     */
    public void displayBooks() {
        List<Book> books = getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        System.out.println("\n--- All Books ---");
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("-----------------");
    }

    /**
     * (Helper for Menu) Prints all users to the console.
     */
    public void displayUsers() {
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- All Users ---");
            while (rs.next()) {
                System.out.printf("ID: %s | Name: %s | Type: %s | Detail: %s | Fine: $%.2f\n",
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("user_type"),
                        rs.getString("detail"),
                        rs.getDouble("fine_amount")
                );
            }
            System.out.println("-----------------");

        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
    }

    /**
     * (Called by Librarian) Removes a book from the database.
     * @param isbn The ISBN of the book to remove.
     * @return true if successful, false otherwise.
     */
    public boolean removeBook(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing book: " + e.getMessage());
            return false;
        }
    }
    /**
     * Fetch all users from the database.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String userType = rs.getString("user_type");
                String detail = rs.getString("detail");
                double fine = rs.getDouble("fine_amount");

                if ("student".equalsIgnoreCase(userType)) {
                    users.add(new Student(id, name, password, detail, fine));
                } else if ("staff".equalsIgnoreCase(userType)) {
                    users.add(new Staff(id, name, password, detail, fine));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

} // This is the final closing brace of the class

