import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * The "Concrete Strategy" for Students.
 * Implements the "rule book" for student borrowing.
 * Notice the 'implements BorrowStrategy' part.
 */
public class StudentBorrowStrategy implements BorrowStrategy {

    private static final int MAX_BOOKS_ALLOWED = 5;
    private static final int BORROW_DURATION_DAYS = 14; // 2 weeks
    private static final double FINE_PER_DAY = 0.50; // 50 cents per day

    @Override
    public boolean canBorrow(Account account, Book book) {
        if (!book.isAvailable()) {
            System.out.println("Error: Book is already checked out.");
            return false;
        }
        if (account.getBorrowedBooks().size() >= MAX_BOOKS_ALLOWED) {
            System.out.println("Error: Student has reached the maximum limit of " + MAX_BOOKS_ALLOWED + " books.");
            return false;
        }
        // You could add fine checks here, e.g., if (account.getFineAmount() > 0) return false;
        return true;
    }

    @Override
    public Date calculateDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, BORROW_DURATION_DAYS);
        return new Date(cal.getTimeInMillis());
    }

    @Override
    public double calculateFine(Date dueDate) {
        long diffInMillis = System.currentTimeMillis() - dueDate.getTime();
        if (diffInMillis <= 0) {
            return 0.0; // Not overdue
        }
        long daysOverdue = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        return daysOverdue * FINE_PER_DAY;
    }
}


