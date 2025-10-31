import java.sql.Date;
import java.util.List;

/**

 Abstract class representing a User.

 This is the "blueprint" for Student and Staff.

 This is part of our "Business Logic Layer."

 FINAL VERSION: Includes borrow, return, refresh, and payFine methods.
 */
public abstract class User {

    // --- Properties ---
    protected String id;
    protected String name;
    protected String password;
    protected double fineAmount;
    protected Account account;
    protected BorrowStrategy borrowStrategy; // The "Strategy"
    protected LibraryDatabase db;

    // --- Constructor ---
    public User(String id, String name, String password, double fineAmount) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.fineAmount = fineAmount;
        this.db = LibraryDatabase.getInstance();
        this.account = db.getAccount(this.id); // Get initial account details
    }

// --- Core Methods ---

    /**

     Refreshes the user's account (borrowed books and fine) from the database.
     */
    public void refreshAccount() {
        this.account = db.getAccount(this.id);
        User dbUser = db.validateUser(this.id, this.password); // A bit of a hack to get latest fine
        if (dbUser != null) {
            this.fineAmount = dbUser.getFineAmount();
        }
    }

    /**

     Main logic for a user to borrow a book.

     @param isbn The ISBN of the book to borrow.

     @return A status message (String).
     */
    public String borrowBook(String isbn) {
        Book book = db.getBookByIsbn(isbn);

// 1. Check if the strategy allows borrowing
        if (!borrowStrategy.canBorrow(this.account, book)) {
            if (book == null) {
                return "Error: Book with ISBN " + isbn + " not found.";
            }
            if (!book.isAvailable()) {
                return "Error: Book is already checked out.";
            }
            return "Error: You have reached your borrow limit.";
        }

// 2. If allowed, update the book
        book.setAvailable(false);
        book.setBorrowerId(this.id);
        book.setDueDate(borrowStrategy.calculateDueDate());

// 3. Save to database
        if (db.updateBook(book)) {
            refreshAccount(); // Update our local account
            return "Success! Book '" + book.getTitle() + "' checked out.";
        } else {
            return "Error: Database error while checking out book.";
        }
    }

    /**

     Main logic for a user to return a book.

     @param isbn The ISBN of the book to return.

     @return A status message (String).
     */
    public String returnBook(String isbn) {
        Book book = db.getBookByIsbn(isbn);

// 1. Check if book exists and if this user actually borrowed it
        if (book == null) {
            return "Error: Book with ISBN " + isbn + " not found.";
        }
        if (book.isAvailable() || book.getBorrowerId() == null || !book.getBorrowerId().equals(this.id)) {
            return "Error: You did not borrow this book.";
        }

// 2. Calculate fine (if any)
        double fine = borrowStrategy.calculateFine(book.getDueDate());
        if (fine > 0) {
            this.fineAmount += fine;
            db.updateUserFine(this.id, this.fineAmount);
// We still let them return it, but they now have a fine
        }

// 3. Update the book
        book.setAvailable(true);
        book.setBorrowerId(null);
        book.setDueDate(null);

// 4. Save to database
        if (db.updateBook(book)) {
            refreshAccount(); // Update our local account
            if (fine > 0) {
                return "Book returned. A fine of $" + String.format("%.2f", fine) + " has been added to your account.";
            } else {
                return "Success! Book '" + book.getTitle() + "' returned on time.";
            }
        } else {
            return "Error: Database error while returning book.";
        }
    }

    /**

     Pays the user's outstanding fine balance.
     */
    public void payFine() {
        this.fineAmount = 0.0;
        db.updateUserFine(this.id, 0.0);
    }

    // --- Getters and Setters ---
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public Account getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // --- Abstract Methods (to be filled in by Student/Staff) ---
    public abstract String getUserType();
    public abstract String getDetail();
}