/**
 * Interface for the Strategy Design Pattern.
 * This defines a "contract" for any borrowing algorithm.
 * Any class that 'implements' this must provide these methods.
 * This is part of our "Business Logic Layer."
 */
public interface BorrowStrategy {

    /**
     * Checks if a user is allowed to borrow a book according to this strategy.
     * @param account The user's account (to check current loans).
     * @param book The book they want to borrow.
     * @return true if the borrow is allowed, false otherwise.
     */
    boolean canBorrow(Account account, Book book);

    /**
     * Calculates the due date for a loan based on this strategy.
     * @return The Date the book should be due.
     */
    java.sql.Date calculateDueDate();

    /**
     * Calculates the fine for an overdue book.
     * @param dueDate The date the book was due.
     * @return The total fine amount.
     */
    double calculateFine(java.sql.Date dueDate);
}


