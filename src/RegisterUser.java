import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**

 A new window for registering a new User (Student or Staff).

 This is part of our "Presentation Layer."
 */
public class RegisterUser extends JFrame implements ActionListener {

    // --- GUI Components ---
    private JTextField idField, nameField, detailField;
    private JPasswordField passwordField;
    private JRadioButton studentRadio, staffRadio;
    private JButton registerButton, cancelButton;

    // --- Logic/Data ---
    private LibraryDatabase db;

    public RegisterUser() {
// Get the database instance
        db = LibraryDatabase.getInstance();

        setTitle("Register New User");
        // Use DISPOSE_ON_CLOSE so it only closes this window, not the whole app
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;

        // Title
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Create New User Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(24, 29, 49));
        formPanel.add(titleLabel, gbc);

        // User Type (Radio)
        gbc.gridy++;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        radioPanel.setBackground(Color.WHITE);
        studentRadio = new JRadioButton("Student", true);
        studentRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentRadio.setBackground(Color.WHITE);
        staffRadio = new JRadioButton("Staff");
        staffRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        staffRadio.setBackground(Color.WHITE);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(studentRadio);
        radioGroup.add(staffRadio);
        radioPanel.add(studentRadio);
        radioPanel.add(staffRadio);
        formPanel.add(radioPanel, gbc);

        // ID Label
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("User ID:"), gbc);

        // ID Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);

        // Name Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Full Name:"), gbc);

        // Name Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        // Password Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        // Detail Label (Class or Department)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Class/Dept:"), gbc);

        // Detail Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        detailField = new JTextField(15);
        formPanel.add(detailField, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        formPanel.add(buttonPanel, gbc);

        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == registerButton) {
// --- Register the user ---
            String id = idField.getText();
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String detail = detailField.getText();

            if (id.isEmpty() || name.isEmpty() || password.isEmpty() || detail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser;
            if (studentRadio.isSelected()) {
                // Create a Student
                newUser = new Student(id, name, password, detail, 0.0); // 0.0 fine
            } else {
                // Create a Staff
                newUser = new Staff(id, name, password, detail, 0.0); // 0.0 fine
            }

            // Use the "engine" to register the user
            if (db.registerUser(newUser)) {
                JOptionPane.showMessageDialog(this, "User registered successfully! You can now log in.");
                dispose(); // Close this window
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. User ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (ae.getSource() == cancelButton) {
            dispose(); // Close this window
        }


    }
}
