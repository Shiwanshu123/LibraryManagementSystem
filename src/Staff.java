/**
 * A "Concrete" class that extends User.
 * This represents a Staff user.
 * This is part of our "Business Logic Layer."
 */
public class Staff extends User {

    // --- Properties ---
    private String department; // e.g., "Dept: Computer Science"

    // --- Constructor ---
    public Staff(String id, String name, String password, String department, double fineAmount) {
        // 1. Call the 'super' constructor (the one in User.java)
        super(id, name, password, fineAmount);

        // 2. Set the staff-specific property
        this.department = department;

        // 3. **Set the Strategy!** This is key.
        // A Staff member gets a StaffBorrowStrategy.
        this.borrowStrategy = new StaffBorrowStrategy();
    }

    // --- Implemented Abstract Methods ---
    // These are the "blanks" from User.java that we must fill in.

    @Override
    public String getUserType() {
        return "staff";
    }

    @Override
    public String getDetail() {
        return "Dept: " + this.department;
    }

    // --- Getters and Setters ---
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

