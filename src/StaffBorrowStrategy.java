import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * The "Concrete Strategy" for Staff.
 * Implements the "rule book" for staff borrowing.
 */
public class StaffBorrowStrategy implements BorrowStrategy {

    private static final int MAX_BOOKS_ALLOWED = 10;
    private static final int BORROW_DURATION_DAYS = 30; // 1 month
    private static final double FINE_PER_DAY = 0.25; // 25 cents per day (Staff gets a discount!)

    @Override
    public boolean canBorrow(Account account, Book book) {
        if (!book.isAvailable()) {
            System.out.println("Error: Book is already checked out.");
            return false;
        }
        if (account.getBorrowedBooks().size() >= MAX_BOOKS_ALLOWED) {
            System.out.println("Error: Staff has reached the maximum limit of " + MAX_BOOKS_ALLOWED + " books.");
            return false;
        }
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


