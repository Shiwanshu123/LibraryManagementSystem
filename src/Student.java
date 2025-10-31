/**
 * A "Concrete" class that extends User.
 * This represents a Student user.
 * This is part of our "Business Logic Layer."
 */
public class Student extends User {

    // --- Properties ---
    private String studentClass; // e.g., "Class: 10A"

    // --- Constructor ---
    public Student(String id, String name, String password, String studentClass, double fineAmount) {
        // 1. Call the 'super' constructor (the one in User.java)
        super(id, name, password, fineAmount);

        // 2. Set the student-specific property
        this.studentClass = studentClass;

        // 3. **Set the Strategy!** This is key.
        // A Student gets a StudentBorrowStrategy.
        this.borrowStrategy = new StudentBorrowStrategy();
    }

    // --- Implemented Abstract Methods ---
    // These are the "blanks" from User.java that we must fill in.

    @Override
    public String getUserType() {
        return "student";
    }

    @Override
    public String getDetail() {
        return "Class: " + this.studentClass;
    }

    // --- Getters and Setters ---
    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}


