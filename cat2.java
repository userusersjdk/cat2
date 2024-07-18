import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DisplayData extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public DisplayData() {
        setTitle("Display Data");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Mobile");
        tableModel.addColumn("Gender");
        tableModel.addColumn("DOB");
        tableModel.addColumn("Address");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        loadData();

        setVisible(true);
    }

    private void loadData() {
        try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement()) {
            String query = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("mobile"),
                        rs.getString("gender"),
                        rs.getString("dob"),
                        rs.getString("address")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DisplayData();
    }
}


public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/registrationdb"; // Change as per your database
        String user = "root"; // Change as per your database
        String password = ""; // Change as per your database
        return DriverManager.getConnection(url, user, password);
    }

    public static void insertData(String name, String mobile, String gender, String dob, String address) {
        String query = "INSERT INTO users (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, mobile);
            ps.setString(3, gender);
            ps.setString(4, dob);
            ps.setString(5, address);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


public class RegistrationForm extends JFrame {
    // Components of the Form
    private Container container;
    private JLabel title, nameLabel, mobileLabel, genderLabel, dobLabel, addressLabel;
    private JTextField nameField, mobileField, dobField;
    private JRadioButton male, female;
    private ButtonGroup genderGroup;
    private JTextArea addressArea;
    private JCheckBox terms;
    private JButton submit, reset;
    private JTextArea output;
    private JTable dataTable;
    
    public RegistrationForm() {
        setTitle("Registration Form");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        container = getContentPane();
        container.setLayout(null);
        
        title = new JLabel("Registration Form");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(300, 30);
        container.add(title);

        // Add more components as per your form design
        nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setSize(100, 20);
        nameLabel.setLocation(100, 100);
        container.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 15));
        nameField.setSize(190, 20);
        nameField.setLocation(200, 100);
        container.add(nameField);

        // Repeat for other components like mobile, gender, DOB, address, terms, etc.

        submit = new JButton("Submit");
        submit.setFont(new Font("Arial", Font.PLAIN, 15));
        submit.setSize(100, 20);
        submit.setLocation(150, 450);
        container.add(submit);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(270, 450);
        container.add(reset);

        // Output Area
        output = new JTextArea();
        output.setFont(new Font("Arial", Font.PLAIN, 15));
        output.setSize(300, 400);
        output.setLocation(500, 100);
        output.setLineWrap(true);
        output.setEditable(false);
        container.add(output);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (terms.isSelected()) {
                    // Add data to the database and display in output area
                    // TODO: Database connection and data insertion
                    output.setText("Name: " + nameField.getText() + "\nMobile: " + mobileField.getText() + "\n");
                } else {
                    output.setText("Please accept the terms & conditions.");
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                mobileField.setText("");
                // Reset other fields
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}