import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**

 A dashboard for a logged-in User (Student or Staff).

 This is part of our "Presentation Layer."
 */
public class UserDashboard extends JFrame implements ActionListener {

    // --- Data ---
    private User currentUser;
    private LibraryDatabase db;

    // --- GUI Components ---
    private JButton checkOutButton, returnButton, viewMyBooksButton, payFineButton;
    private JTextArea infoArea;

    public UserDashboard(User user) {
        this.currentUser = user;
        this.db = LibraryDatabase.getInstance();

        setTitle("User Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.setBackground(new Color(24, 29, 49));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getUserType() + ")");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Button Panel (Left) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10)); // 6 rows, 1 col
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        checkOutButton = new JButton("Check Out a Book");
        checkOutButton.addActionListener(this);
        buttonPanel.add(checkOutButton);

        returnButton = new JButton("Return a Book");
        returnButton.addActionListener(this);
        buttonPanel.add(returnButton);

        viewMyBooksButton = new JButton("View My Books");
        viewMyBooksButton.addActionListener(this);
        buttonPanel.add(viewMyBooksButton);

        payFineButton = new JButton("Pay Fines");
        payFineButton.addActionListener(this);
        buttonPanel.add(payFineButton);

        add(buttonPanel, BorderLayout.WEST);

        // --- Info Area (Center) ---
        infoArea = new JTextArea("Welcome! Click a button to get started.");
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        infoArea.setEditable(false);
        infoArea.setMargin(new Insets(20, 20, 20, 20));
        JScrollPane scrollPane = new JScrollPane(infoArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewMyBooksButton) {
// Refresh the user's account info
            currentUser.refreshAccount();

            // Build a string with the info
            StringBuilder info = new StringBuilder();
            info.append("--- Your Account Details ---\n");
            info.append("Name: ").append(currentUser.getName()).append("\n");
            info.append("ID: ").append(currentUser.getId()).append("\n");
            info.append("Total Fines Due: $").append(String.format("%.2f", currentUser.getFineAmount())).append("\n\n");
            info.append("--- Your Borrowed Books ---\n");

            if (currentUser.getAccount().getBorrowedBooks().isEmpty()) {
                info.append("You have no books checked out.\n");
            } else {
                for (Book book : currentUser.getAccount().getBorrowedBooks()) {
                    info.append(book.toString()).append("\n"); // Uses the Book's toString()
                }
            }
            infoArea.setText(info.toString());

        } else if (e.getSource() == checkOutButton) {
            String isbn = JOptionPane.showInputDialog(this, "Enter the ISBN of the book to check out:");
            if (isbn != null && !isbn.isEmpty()) {
                String result = currentUser.borrowBook(isbn);
                infoArea.setText(result); // Show the result of the borrow attempt
                // Refresh account info in the text area
                viewMyBooksButton.doClick();
            }
        } else if (e.getSource() == returnButton) {
            String isbn = JOptionPane.showInputDialog(this, "Enter the ISBN of the book to return:");
            if (isbn != null && !isbn.isEmpty()) {
                String result = currentUser.returnBook(isbn);
                infoArea.setText(result); // Show the result of the return attempt
                // Refresh account info in the text area
                viewMyBooksButton.doClick();
            }
        } else if (e.getSource() == payFineButton) {
            currentUser.refreshAccount();
            double fine = currentUser.getFineAmount();
            if (fine <= 0) {
                JOptionPane.showMessageDialog(this, "You have no outstanding fines.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this,
                    "Your total fine is $" + String.format("%.2f", fine) + ". Do you want to pay now?",
                    "Pay Fine", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                currentUser.payFine();
                infoArea.setText("Fines paid successfully. Your new balance is $0.00");
                viewMyBooksButton.doClick(); // Refresh
            }
        }


    }
}
