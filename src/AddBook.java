import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A pop-up window (JFrame) for adding a new book to the database.
 * This is part of our "Presentation Layer."
 */
public class AddBook extends JFrame implements ActionListener {

    private JTextField isbnField, titleField, authorField, pubField;
    private JButton addButton, cancelButton;

    public AddBook() {
        // --- Frame Setup ---
        setTitle("Add New Book");
        setSize(450, 400); // Smaller window
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this window
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- Form Panel (Center) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;

        // ISBN Label & Field
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(isbnLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        isbnField = new JTextField(20);
        isbnField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(isbnField, gbc);

        // Title Label & Field
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        titleField = new JTextField(20);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(titleField, gbc);

        // Author Label & Field
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(authorLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        authorField = new JTextField(20);
        authorField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(authorField, gbc);

        // Publication Label & Field
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel pubLabel = new JLabel("Publication:");
        pubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(pubLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        pubField = new JTextField(20);
        pubField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(pubField, gbc);


        // --- Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

// Add Book button
        addButton = new JButton("Add Book");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(24, 29, 49)); // dark blue
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setOpaque(true);
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

// Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(220, 53, 69)); // soft red
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);


        // Make frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addButton) {
            // --- THIS IS THE "INNOVATION" ---
            // This UI file has NO SQL. It just calls the "engine."

            // 1. Get the data from the form
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            String pub = pubField.getText();

            // 2. Simple validation
            if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN, Title, and Author are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Create a Book object (from our "engine" logic)
            Book newBook = new Book(isbn, title, author, pub);

            // 4. Call the "engine" (LibraryDatabase) to save it
            // This is the separation of concerns!
            try {
                LibraryDatabase db = LibraryDatabase.getInstance();
                boolean success = db.addBook(newBook);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Book added successfully!");
                    setVisible(false); // Close this pop-up window
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding book. (Check for duplicate ISBN)", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "A database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == cancelButton) {
            // Just close this pop-up window
            setVisible(false);
        }
    }
}


