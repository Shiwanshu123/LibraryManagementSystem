import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The main dashboard for the Librarian.
 * This window opens after a successful librarian login.
 * This is part of our "Presentation Layer."
 */
public class LibrarianDashboard extends JFrame implements ActionListener {

    private String adminName;
    private JButton addBookButton, viewBooksButton, viewUsersButton, logoutButton;

    public LibrarianDashboard(String adminName) {
        this.adminName = adminName;

        // --- Frame Setup ---
        setTitle("Librarian Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- Top Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(24, 29, 49)); // Dark blue
        headerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel welcomeLabel = new JLabel("Welcome, " + adminName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Navigation Sidebar (West) ---
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(250, 0));
        navPanel.setBackground(new Color(248, 249, 250)); // Light gray
        navPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Add some spacing at the top
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        addBookButton = createNavButton("Add a new book");
        navPanel.add(addBookButton);

        viewBooksButton = createNavButton("View all books");
        navPanel.add(viewBooksButton);

        viewUsersButton = createNavButton("View all users");
        navPanel.add(viewUsersButton);

        // This pushes the logout button to the bottom
        navPanel.add(Box.createVerticalGlue());

        logoutButton = createNavButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Red for logout
        logoutButton.setForeground(Color.WHITE);
        // Override hover for logout button
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(180, 40, 50)); // Darker red
            }
            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(220, 53, 69)); // Original red
            }
        });
        navPanel.add(logoutButton);

        // Add spacing at the bottom
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        add(navPanel, BorderLayout.WEST);

        // --- Main Content Area (Center) ---
        // We'll just put a welcome image here for now
        // A more complex app would use this space to show tables
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/library_dashboard.png"));
            if (i1.getImage() == null || i1.getImageLoadStatus() == MediaTracker.ERRORED) {
                throw new Exception("Image not found");
            }
            Image i2 = i1.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel imageLabel = new JLabel(i3);
            mainPanel.add(imageLabel);
        } catch (Exception e) {
            System.err.println("Warning: Could not load 'icons/library_dashboard.png'. Using placeholder text.");
            JLabel placeholder = new JLabel("Welcome to the Dashboard!");
            placeholder.setFont(new Font("Segoe UI", Font.BOLD, 40));
            placeholder.setForeground(Color.GRAY);
            mainPanel.add(placeholder);
        }

        add(mainPanel, BorderLayout.CENTER);


        // Make frame visible
        setVisible(true);
    }

    /**
     * Helper method to create styled navigation buttons
     */
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(248, 249, 250)); // Match sidebar
        button.setForeground(new Color(24, 29, 49)); // Dark blue text
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 30, 15, 30)); // Padding
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);

        // Set max size to ensure it fills the width
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220)); // Light gray hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(248, 249, 250)); // Original color
            }
        });

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBookButton) {
            // Open the AddBook window
            new AddBook();

        } else if (ae.getSource() == viewBooksButton) {
            new ViewBooks();


        } else if (ae.getSource() == viewUsersButton) {
            new ViewUsers();


        } else if (ae.getSource() == logoutButton) {
            // Close this window and open the Login window
            setVisible(false);
            new Login();
        }
    }
}


