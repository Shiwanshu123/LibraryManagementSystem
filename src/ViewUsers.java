import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Displays all registered users in a table.
 * Opens when librarian clicks "View all users".
 */
public class ViewUsers extends JFrame {

    private JTable userTable;
    private DefaultTableModel model;

    public ViewUsers() {
        setTitle("All Registered Users");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"User ID", "Name", "Type", "Detail", "Fine Amount"};
        model = new DefaultTableModel(columns, 0);
        userTable = new JTable(model);
        userTable.setRowHeight(25);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        loadUsers();

        setVisible(true);
    }

    private void loadUsers() {
        try {
            LibraryDatabase db = LibraryDatabase.getInstance();
            List<User> users = db.getAllUsers();

            for (User u : users) {
                Object[] row = {
                        u.getId(),
                        u.getName(),
                        u.getUserType(),
                        u.getDetail(),
                        String.format("%.2f", u.getFineAmount())
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage());
        }
    }
}
