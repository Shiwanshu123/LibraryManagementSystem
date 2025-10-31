import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * Library Management System - Login Screen
 * With Mac-friendly styling and visible buttons
 */
public class Login extends JFrame implements ActionListener {

    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton, registerButton;
    private JRadioButton userRadio, librarianRadio;
    private LibraryDatabase db;

    public Login() {
        db = LibraryDatabase.getInstance();
        System.out.println("Checking/Creating database tables...");
        DatabaseConnector.createTables();
        setupDefaultAdmin();

        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- LEFT PANEL ---
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(24, 29, 49));
        imagePanel.setPreferredSize(new Dimension(300, 0));
        imagePanel.setLayout(new GridBagLayout());

        JLabel placeholder = new JLabel("LMS");
        placeholder.setFont(new Font("Segoe UI", Font.BOLD, 70));
        placeholder.setForeground(Color.WHITE);
        imagePanel.add(placeholder);
        add(imagePanel, BorderLayout.WEST);

        // --- RIGHT PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(40, 40, 20, 40));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Library Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(24, 29, 49));
        formPanel.add(titleLabel, gbc);

        // Radio buttons
        gbc.gridy++;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        radioPanel.setBackground(Color.WHITE);
        userRadio = new JRadioButton("User", true);
        librarianRadio = new JRadioButton("Librarian");
        userRadio.setBackground(Color.WHITE);
        librarianRadio.setBackground(Color.WHITE);
        ButtonGroup group = new ButtonGroup();
        group.add(userRadio);
        group.add(librarianRadio);
        radioPanel.add(userRadio);
        radioPanel.add(librarianRadio);
        formPanel.add(radioPanel, gbc);

        // User ID
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        idField = new JTextField(15);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        idField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(24, 29, 49)));
        formPanel.add(idField, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(24, 29, 49)));
        formPanel.add(passwordField, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Initialize buttons
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        registerButton = new JButton("Register");

        // Apply styling
        styleTextButton(loginButton);
        styleTextButton(cancelButton);
        styleTextButton(registerButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(registerButton);
        formPanel.add(buttonPanel, gbc);

        formPanel.revalidate();
        formPanel.repaint();
        setVisible(true);
    }

    /** Helper method: Create underline on hover, dark blue text */
    private void styleTextButton(JButton button) {
        Color defaultColor = new Color(0, 0, 139); // Dark Blue
        Color hoverColor = new Color(30, 144, 255); // Lighter Blue

        button.setForeground(defaultColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);

        // Hover underline + color change
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(hoverColor);
                button.setFont(underlineFont(button.getFont(), true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(defaultColor);
                button.setFont(underlineFont(button.getFont(), false));
            }
        });
    }

    /** Utility: adds/removes underline */
    private static Font underlineFont(Font font, boolean underline) {
        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, underline ? TextAttribute.UNDERLINE_ON : -1);
        return font.deriveFont(attributes);
    }

    /** Ensure default admin account exists */
    private void setupDefaultAdmin() {
        Librarian admin = db.findLibrarianById("admin");
        if (admin == null) {
            System.out.println("No default 'admin' librarian found. Creating one...");
            Librarian defaultAdmin = new Librarian("admin", "Admin", "admin123");
            if (db.registerLibrarian(defaultAdmin)) {
                System.out.println("Default admin created. Credentials: admin / admin123");
            } else {
                System.err.println("Error: Could not create default admin.");
            }
        } else {
            System.out.println("Default admin account 'admin' is ready.");
        }
    }

    /** Button click handler */
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == loginButton) {
            String id = idField.getText().trim();
            String pass = new String(passwordField.getPassword());

            try {
                if (librarianRadio.isSelected()) {
                    Librarian admin = db.validateLibrarian(id, pass);
                    if (admin != null) {
                        JOptionPane.showMessageDialog(this, "Welcome, " + admin.getName());
                        new LibrarianDashboard(admin.getName());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Librarian ID or password");
                    }
                } else {
                    User user = db.validateUser(id, pass);
                    if (user != null) {
                        JOptionPane.showMessageDialog(this, "Welcome, " + user.getName());
                        new UserDashboard(user);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid User ID or password");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }

        } else if (src == cancelButton) {
            dispose();

        } else if (src == registerButton) {
            new RegisterUser();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Login();
    }
}
