import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's account.
 * This class holds the list of books a user has borrowed.
 * This is part of our "Business Logic Layer."
 */
public class Account {

    // --- Properties ---
    // We will get the fine amount from the User object.
    // This class will just manage the list of borrowed books.

    private List<Book> borrowedBooks;

    // --- Constructor ---
    public Account() {
        this.borrowedBooks = new ArrayList<>();
    }

    // --- Methods ---

    /**
     * Adds a book to this account's list of borrowed books.
     * This is called by LibraryDatabase when loading the account.
     * @param book The book to add.
     */
    public void addBorrowedBook(Book book) {
        this.borrowedBooks.add(book);
    }

    /**
     * Gets the list of borrowed books.
     * @return A List of Book objects.
     */
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    /**
     * Checks if the account has borrowed a specific book.
     * @param book The book to check for.
     * @return true if the book is in the list, false otherwise.
     */
    public boolean hasBorrowed(Book book) {
        // We check by ISBN, as it's the unique identifier
        for (Book b : borrowedBooks) {
            if (b.getIsbn().equals(book.getIsbn())) {
                return true;
            }
        }
        return false;
    }


    // --- NEW METHOD 1 (Fixes the first error) ---
    /**
     * Displays all books currently borrowed by this account.
     * This is called by the User.viewAccount() method.
     */
    public void displayBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("You have no books currently checked out.");
        } else {
            System.out.println("--- Your Borrowed Books ---");
            for (Book book : borrowedBooks) {
                // We can just use the book's built-in toString() method
                System.out.println(book);
            }
            System.out.println("---------------------------");
        }
    }

    // --- NEW METHOD 2 (Fixes the second error) ---
    /**
     * Removes a book from this account's list.
     * This is called by the User.returnBook() method.
     * @param book The book to remove.
     */
    public void removeBorrowedBook(Book book) {
        // We must remove by ISBN, not the object itself
        borrowedBooks.removeIf(b -> b.getIsbn().equals(book.getIsbn()));
    }

} // End of Account class

