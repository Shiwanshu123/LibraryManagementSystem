import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Displays all books in the system.
 * This is part of the "Presentation Layer."
 */
public class ViewBooks extends JFrame {

    private JTable bookTable;
    private DefaultTableModel tableModel;

    public ViewBooks() {
        setTitle("View All Books");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table column headers
        String[] columns = {"ISBN", "Title", "Author", "Publication", "Availability"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooksFromDatabase();
            }
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);

        // Load data
        loadBooksFromDatabase();

        setVisible(true);
    }

    /**
     * Fetches all books from the database and displays them in the table.
     */
    private void loadBooksFromDatabase() {
        try {
            LibraryDatabase db = LibraryDatabase.getInstance();
            List<Book> books = db.getAllBooks();

            // Clear old rows
            tableModel.setRowCount(0);

            if (books == null || books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books found in the library.");
                return;
            }

            // Populate the table
            for (Book book : books) {
                String status = book.isAvailable() ? "Available" : "Checked Out";
                tableModel.addRow(new Object[]{
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublication(),
                        status
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading books from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
