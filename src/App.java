import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel login = new JLabel("Enter Login:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(login, constraints);

        JTextField user = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(user, constraints);

        JLabel pass = new JLabel("Enter Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(pass, constraints);

        JPasswordField passField = new JPasswordField(10);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(passField, constraints);

        JButton submit = new JButton("Submit");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(submit, constraints);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = user.getText();
                char[] passwordChars = passField.getPassword();
                String password = new String(passwordChars);

                if (authenticate(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful");
                    showWelcomeFrame(username, frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password");
                }
            }
        });

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean authenticate(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/employeemanage";
        String user = "root";
        String pass = "password";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT * FROM user WHERE username=? AND password=?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void showWelcomeFrame(String username, JFrame loginFrame) {
        JFrame welcomeFrame = new JFrame("Welcome, " + username + "!");
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel greetingLabel = new JLabel("Hello, " + username + "!");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(greetingLabel, constraints);

        JButton manageEmployeesButton = new JButton("Manage Employees");
        manageEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add action for managing employees

                showManageEmployee(username);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(manageEmployeesButton, constraints);

        JButton managePayrollButton = new JButton("Manage Payroll");
        managePayrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPayrollEmployee(username);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(managePayrollButton, constraints);

        JButton manageattendanceBtn = new JButton("Manage Attendance");
        manageattendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add action for managing payroll
                String empID = JOptionPane.showInputDialog(welcomeFrame, "Enter Employee ID:");
                if (empID != null && !empID.isEmpty()) {
                    // Check if employee with the given ID exists
                    String url = "jdbc:mysql://localhost:3306/employeemanage";
                    String user = "root";
                    String pass = "password";
                    String searchQuery = "SELECT * FROM employee WHERE emp_id = ?";
                    try (Connection conn = DriverManager.getConnection(url, user, pass);
                            PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
                        stmt.setString(1, empID);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Employee found, show update dialog
                                String name = rs.getString("name");
                                String role = rs.getString("role");
                                String salary = rs.getString("days_present");

                                // Create a new modal dialog for updating the employee
                                JDialog updateEmployeeDialog = new JDialog(welcomeFrame, "Add leaves to Employee",
                                        true);
                                updateEmployeeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                updateEmployeeDialog.setSize(400, 300);

                                // Create a panel for the form
                                JPanel updateEmployeePanel = new JPanel(new GridBagLayout());
                                GridBagConstraints updateEmployeeConstraints = new GridBagConstraints();
                                updateEmployeeConstraints.insets = new Insets(10, 10, 10, 10);

                                JLabel nameLabel = new JLabel("Name:");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 0;
                                updateEmployeePanel.add(nameLabel, updateEmployeeConstraints);

                                JTextField nameField = new JTextField(name, 20);
                                nameField.setEditable(false); // Make it non-editable
                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 0;
                                updateEmployeePanel.add(nameField, updateEmployeeConstraints);

                                JLabel roleLabel = new JLabel("Role:");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 1;
                                updateEmployeePanel.add(roleLabel, updateEmployeeConstraints);

                                JTextField roleField = new JTextField(role, 20);
                                nameField.setEditable(false); // Make it non-editable
                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 1;
                                updateEmployeePanel.add(roleField, updateEmployeeConstraints);

                                JLabel salaryLabel = new JLabel("Attendance :");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 2;
                                updateEmployeePanel.add(salaryLabel, updateEmployeeConstraints);

                                JTextField salaryField = new JTextField(salary, 20);
                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 2;
                                updateEmployeePanel.add(salaryField, updateEmployeeConstraints);

                                JButton saveButton = new JButton("Save");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 3;
                                updateEmployeeConstraints.gridwidth = 2;
                                updateEmployeeConstraints.anchor = GridBagConstraints.CENTER;
                                updateEmployeePanel.add(saveButton, updateEmployeeConstraints);

                                // Add ActionListener to save button to update employee details in the database
                                saveButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String newEmployeeSalary = salaryField.getText();

                                        // Update employee details in the database
                                        String updateQuery = "UPDATE employee SET days_present = ? WHERE emp_id = ?";
                                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                            updateStmt.setString(1, newEmployeeSalary);
                                            updateStmt.setString(2, empID);
                                            updateStmt.executeUpdate();

                                            // Refresh the table data after updating employee
                                            JOptionPane.showMessageDialog(updateEmployeeDialog,
                                                    "Attendance Added Successfully");

                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                            JOptionPane.showMessageDialog(updateEmployeeDialog,
                                                    "Error updating employee: " + ex.getMessage());
                                        }

                                        updateEmployeeDialog.dispose(); // Close the dialog after saving
                                    }
                                });

                                updateEmployeeDialog.add(updateEmployeePanel);
                                updateEmployeeDialog.setLocationRelativeTo(welcomeFrame);
                                updateEmployeeDialog.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(welcomeFrame, "No employee found with ID: " + empID);
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(welcomeFrame, "Error updating employee: " + ex.getMessage());
                    }
                }
            }

        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(manageattendanceBtn, constraints);

        // Add more buttons for other features as needed

        welcomeFrame.add(panel);
        welcomeFrame.pack();
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);

        loginFrame.dispose(); // Close the login frame
    }

    private static void showManageEmployee(String username) {
        JFrame welcomeFrame = new JFrame("Welcome, " + username + "!");
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel greetingLabel = new JLabel("Hello, " + username + "!");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4; // Span across 4 columns
        panel.add(greetingLabel, constraints);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton newempBtn = new JButton("Add New Employee");
        buttonPanel.add(newempBtn);
        JButton delempBtn = new JButton("Delete Employee");
        buttonPanel.add(delempBtn);
        JButton upempBtn = new JButton("Update Employee");
        buttonPanel.add(upempBtn);
        JButton searchempBtn = new JButton("Search Employee");
        buttonPanel.add(searchempBtn);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4; // Span across 4 columns
        panel.add(buttonPanel, constraints);

        // Fetch all employees from the database
        String url = "jdbc:mysql://localhost:3306/employeemanage";
        String user = "root";
        String pass = "password";
        String query = "SELECT * FROM employee";
        final JTable[] table = { new JTable() }; // Create an array to hold the JTable reference
        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            // Create a table to display the employee data
            table[0] = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table[0]);
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.gridwidth = 4; // Span across 4 columns
            panel.add(scrollPane, constraints);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(welcomeFrame, "Error fetching employees: " + e.getMessage());
        }

        // Add action listener for the search button
        searchempBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog searchEmployeeDialog = new JDialog(welcomeFrame, "Search Employee", true);
                searchEmployeeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                searchEmployeeDialog.setSize(400, 300);

                JPanel searchEmployeePanel = new JPanel(new GridBagLayout());
                GridBagConstraints searchEmployeeConstraints = new GridBagConstraints();
                searchEmployeeConstraints.insets = new Insets(10, 10, 10, 10);

                JLabel nameLabel = new JLabel("Enter Name Pattern:");
                searchEmployeeConstraints.gridx = 0;
                searchEmployeeConstraints.gridy = 0;
                searchEmployeePanel.add(nameLabel, searchEmployeeConstraints);

                JTextField nameField = new JTextField(20);
                nameField.setPreferredSize(new Dimension(200, nameField.getPreferredSize().height));
                searchEmployeeConstraints.gridx = 0;
                searchEmployeeConstraints.gridy = 1;
                searchEmployeePanel.add(nameField, searchEmployeeConstraints);

                JButton searchButton = new JButton("Search");
                searchEmployeeConstraints.gridx = 0;
                searchEmployeeConstraints.gridy = 2;
                searchEmployeeConstraints.gridwidth = 2;
                searchEmployeeConstraints.anchor = GridBagConstraints.CENTER;
                searchEmployeePanel.add(searchButton, searchEmployeeConstraints);

                // Add ActionListener to search button to search for employees based on the name
                // pattern
                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String searchName = nameField.getText();
                        // Perform search and update the table model
                        String searchQuery = "SELECT * FROM employee WHERE name LIKE ?";
                        try (Connection conn = DriverManager.getConnection(url, user, pass);
                                PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
                            stmt.setString(1, "%" + searchName + "%");
                            try (ResultSet rs = stmt.executeQuery()) {
                                table[0].setModel(buildTableModel(rs)); // Update the table model
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(searchEmployeeDialog,
                                    "Error searching employees: " + ex.getMessage());
                        }

                        if (table[0].getRowCount() == 0) {
                            JOptionPane.showMessageDialog(searchEmployeeDialog,
                                    "No employee found with the name: " + searchName);
                        }
                        if (table[0].getRowCount() > 0) {
                            searchEmployeeDialog.dispose();
                        }
                    }
                });
                searchEmployeeDialog.add(searchEmployeePanel);
                searchEmployeeDialog.setLocationRelativeTo(welcomeFrame);
                searchEmployeeDialog.setVisible(true);
            }
        });

        upempBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empID = JOptionPane.showInputDialog(welcomeFrame, "Enter Employee ID:");
                if (empID != null && !empID.isEmpty()) {
                    // Check if employee with the given ID exists
                    String searchQuery = "SELECT * FROM employee WHERE emp_id = ?";
                    try (Connection conn = DriverManager.getConnection(url, user, pass);
                            PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
                        stmt.setString(1, empID);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Employee found, show update dialog
                                String name = rs.getString("name");
                                String role = rs.getString("role");
                                String salary = rs.getString("salary");

                                // Create a new modal dialog for updating the employee
                                JDialog updateEmployeeDialog = new JDialog(welcomeFrame, "Update Employee", true);
                                updateEmployeeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                updateEmployeeDialog.setSize(400, 300);

                                // Create a panel for the form
                                JPanel updateEmployeePanel = new JPanel(new GridBagLayout());
                                GridBagConstraints updateEmployeeConstraints = new GridBagConstraints();
                                updateEmployeeConstraints.insets = new Insets(10, 10, 10, 10);

                                JLabel nameLabel = new JLabel("Name:");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 0;
                                updateEmployeePanel.add(nameLabel, updateEmployeeConstraints);

                                JTextField nameField = new JTextField(name, 20);
                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 0;
                                updateEmployeePanel.add(nameField, updateEmployeeConstraints);

                                JLabel roleLabel = new JLabel("Role:");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 1;
                                updateEmployeePanel.add(roleLabel, updateEmployeeConstraints);

                                JTextField roleField = new JTextField(role, 20);
                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 1;
                                updateEmployeePanel.add(roleField, updateEmployeeConstraints);

                                JLabel salaryLabel = new JLabel("Salary:");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 2;
                                updateEmployeePanel.add(salaryLabel, updateEmployeeConstraints);

                                JTextField salaryField = new JTextField(salary, 20);
                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 2;
                                updateEmployeePanel.add(salaryField, updateEmployeeConstraints);

                                JButton saveButton = new JButton("Save");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 3;
                                updateEmployeeConstraints.gridwidth = 2;
                                updateEmployeeConstraints.anchor = GridBagConstraints.CENTER;
                                updateEmployeePanel.add(saveButton, updateEmployeeConstraints);

                                // Add ActionListener to save button to update employee details in the database
                                saveButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String newEmployeeName = nameField.getText();
                                        String newEmployeeRole = roleField.getText();
                                        String newEmployeeSalary = salaryField.getText();

                                        // Update employee details in the database
                                        String updateQuery = "UPDATE employee SET name = ?, role = ?, salary = ? WHERE emp_id = ?";
                                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                            updateStmt.setString(1, newEmployeeName);
                                            updateStmt.setString(2, newEmployeeRole);
                                            updateStmt.setString(3, newEmployeeSalary);
                                            updateStmt.setString(4, empID);
                                            updateStmt.executeUpdate();

                                            // Refresh the table data after updating employee
                                            try (PreparedStatement refreshStmt = conn.prepareStatement(query);
                                                    ResultSet refreshRs = refreshStmt.executeQuery()) {
                                                table[0].setModel(buildTableModel(refreshRs)); // Update the table model
                                            }
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                            JOptionPane.showMessageDialog(updateEmployeeDialog,
                                                    "Error updating employee: " + ex.getMessage());
                                        }

                                        updateEmployeeDialog.dispose(); // Close the dialog after saving
                                    }
                                });

                                updateEmployeeDialog.add(updateEmployeePanel);
                                updateEmployeeDialog.setLocationRelativeTo(welcomeFrame);
                                updateEmployeeDialog.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(welcomeFrame, "No employee found with ID: " + empID);
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(welcomeFrame, "Error updating employee: " + ex.getMessage());
                    }
                }
            }
        });

        newempBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new modal dialog for adding a new employee
                JDialog addEmployeeDialog = new JDialog(welcomeFrame, "Add New Employee", true);
                addEmployeeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                addEmployeeDialog.setSize(400, 300);

                // Create a panel for the form
                JPanel addEmployeePanel = new JPanel(new GridBagLayout());
                GridBagConstraints addEmployeeConstraints = new GridBagConstraints();
                addEmployeeConstraints.insets = new Insets(10, 10, 10, 10);

                JLabel nameLabel = new JLabel("Name:");
                addEmployeeConstraints.gridx = 0;
                addEmployeeConstraints.gridy = 0;
                addEmployeePanel.add(nameLabel, addEmployeeConstraints);

                JTextField nameField = new JTextField(20);
                addEmployeeConstraints.gridx = 1;
                addEmployeeConstraints.gridy = 0;
                addEmployeePanel.add(nameField, addEmployeeConstraints);

                JLabel roleLabel = new JLabel("Role:");
                addEmployeeConstraints.gridx = 0;
                addEmployeeConstraints.gridy = 1;
                addEmployeePanel.add(roleLabel, addEmployeeConstraints);

                JTextField roleField = new JTextField(20);
                addEmployeeConstraints.gridx = 1;
                addEmployeeConstraints.gridy = 1;
                addEmployeePanel.add(roleField, addEmployeeConstraints);

                JLabel salaryLabel = new JLabel("Salary:");
                addEmployeeConstraints.gridx = 0;
                addEmployeeConstraints.gridy = 2;
                addEmployeePanel.add(salaryLabel, addEmployeeConstraints);

                JTextField salaryField = new JTextField(20);
                addEmployeeConstraints.gridx = 1;
                addEmployeeConstraints.gridy = 2;
                addEmployeePanel.add(salaryField, addEmployeeConstraints);

                JButton saveButton = new JButton("Save");
                addEmployeeConstraints.gridx = 0;
                addEmployeeConstraints.gridy = 3;
                addEmployeeConstraints.gridwidth = 2;
                addEmployeeConstraints.anchor = GridBagConstraints.CENTER;
                addEmployeePanel.add(saveButton, addEmployeeConstraints);

                // Add ActionListener to save button to insert new employee into the database
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newEmployeeName = nameField.getText();
                        String newEmployeeRole = roleField.getText();
                        String newEmployeeSalary = salaryField.getText();

                        // Insert new employee into the database
                        String insertQuery = "INSERT INTO employee (name, role, days_present, salary) VALUES (?, ?, ?, ?)";
                        try (Connection conn = DriverManager.getConnection(url, user, pass);
                                PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, newEmployeeName);
                            insertStmt.setString(2, newEmployeeRole);
                            insertStmt.setString(3, "0");

                            insertStmt.setString(4, newEmployeeSalary);
                            insertStmt.executeUpdate();

                            // Refresh the table data after adding a new employee
                            try (PreparedStatement stmt = conn.prepareStatement(query);
                                    ResultSet rs = stmt.executeQuery()) {
                                table[0].setModel(buildTableModel(rs)); // Update the table model
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(addEmployeeDialog,
                                    "Error adding new employee: " + ex.getMessage());
                        }

                        addEmployeeDialog.dispose(); // Close the dialog after saving
                    }
                });

                addEmployeeDialog.add(addEmployeePanel);
                addEmployeeDialog.setLocationRelativeTo(welcomeFrame);
                addEmployeeDialog.setVisible(true);
            }
        });

        delempBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new modal dialog for adding a new employee
                JDialog delEmployeeDialog = new JDialog(welcomeFrame, "Delete Employee", true);
                delEmployeeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                delEmployeeDialog.setSize(400, 300);

                // Create a panel for the form
                JPanel addEmployeePanel = new JPanel(new GridBagLayout());
                GridBagConstraints addEmployeeConstraints = new GridBagConstraints();
                addEmployeeConstraints.insets = new Insets(10, 10, 10, 10);

                JLabel nameLabel = new JLabel("Employee id to be deleted:");
                addEmployeeConstraints.gridx = 0;
                addEmployeeConstraints.gridy = 0;
                addEmployeePanel.add(nameLabel, addEmployeeConstraints);

                JTextField nameField = new JTextField(30);
                addEmployeeConstraints.gridx = 0;
                addEmployeeConstraints.gridy = 1;
                addEmployeePanel.add(nameField, addEmployeeConstraints);
                JButton saveButton = new JButton("Delete the employee");
                addEmployeeConstraints.gridx = 0;
                addEmployeeConstraints.gridy = 3;
                addEmployeeConstraints.gridwidth = 2;
                addEmployeeConstraints.anchor = GridBagConstraints.CENTER;
                addEmployeePanel.add(saveButton, addEmployeeConstraints);

                // Add ActionListener to save button to insert new employee into the database
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newEmployeeName = nameField.getText();
                        // Check if the employee ID exists
                        String checkQuery = "SELECT * FROM employee WHERE emp_id = ?";
                        try (Connection conn = DriverManager.getConnection(url, user, pass);
                                PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                            checkStmt.setString(1, newEmployeeName);
                            try (ResultSet rs = checkStmt.executeQuery()) {
                                if (!rs.next()) {
                                    JOptionPane.showMessageDialog(delEmployeeDialog,
                                            "No employee with ID " + newEmployeeName + " found!");
                                    return;
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(delEmployeeDialog,
                                    "Error checking employee ID: " + ex.getMessage());
                            return;
                        }

                        // Confirmation dialog before deleting
                        int result = JOptionPane.showConfirmDialog(delEmployeeDialog,
                                "Are you sure you want to delete employee with ID " + newEmployeeName + "?",
                                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            // Delete the employee
                            String deleteQuery = "DELETE FROM employee WHERE emp_id = ?";
                            try (Connection conn = DriverManager.getConnection(url, user, pass);
                                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                                deleteStmt.setString(1, newEmployeeName);
                                deleteStmt.executeUpdate();

                                // Refresh the table data after deleting the employee
                                try (PreparedStatement stmt = conn.prepareStatement(query);
                                        ResultSet rs = stmt.executeQuery()) {
                                    table[0].setModel(buildTableModel(rs)); // Update the table model
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(delEmployeeDialog,
                                        "Error deleting employee: " + ex.getMessage());
                            }
                        }

                        delEmployeeDialog.dispose(); // Close the dialog after saving
                    }
                });

                delEmployeeDialog.add(addEmployeePanel);
                delEmployeeDialog.setLocationRelativeTo(welcomeFrame);
                delEmployeeDialog.setVisible(true);
            }
        });

        welcomeFrame.add(panel);
        welcomeFrame.pack();
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }

    private static void showPayrollEmployee(String username) {
        JFrame welcomeFrame = new JFrame("Welcome, " + username + "!");
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel greetingLabel = new JLabel("Hello, " + username + "!");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4; // Span across 4 columns
        panel.add(greetingLabel, constraints);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton newempBtn = new JButton("Disburse Salary");
        buttonPanel.add(newempBtn);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4; // Span across 4 columns
        panel.add(buttonPanel, constraints);

        String url = "jdbc:mysql://localhost:3306/employeemanage";
        String user = "root";
        String pass = "password";
        String query = "SELECT * FROM employee";
        final JTable[] table = { new JTable() }; // Create an array to hold the JTable reference
        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            // Create a table to display the employee data
            table[0] = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table[0]);
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.gridwidth = 4; // Span across 4 columns
            panel.add(scrollPane, constraints);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(welcomeFrame, "Error fetching employees: " + e.getMessage());
        }

        newempBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empID = JOptionPane.showInputDialog(welcomeFrame, "Enter Employee ID:");
                if (empID != null && !empID.isEmpty()) {
                    // Check if employee with the given ID exists
                    String searchQuery = "SELECT * FROM employee WHERE emp_id = ?";
                    try (Connection conn = DriverManager.getConnection(url, user, pass);
                            PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
                        stmt.setString(1, empID);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                // Employee found, show update dialog
                                String name = rs.getString("name");
                                String role = rs.getString("role");
                                String salary = rs.getString("salary_due");

                                // Create a new modal dialog for updating the employee
                                JDialog updateEmployeeDialog = new JDialog(welcomeFrame, "Update Employee", true);
                                updateEmployeeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                updateEmployeeDialog.setSize(400, 300);

                                // Create a panel for the form
                                JPanel updateEmployeePanel = new JPanel(new GridBagLayout());
                                GridBagConstraints updateEmployeeConstraints = new GridBagConstraints();
                                updateEmployeeConstraints.insets = new Insets(10, 10, 10, 10);

                                JLabel nameLabel = new JLabel("Name:");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 0;
                                updateEmployeePanel.add(nameLabel, updateEmployeeConstraints);

                                JTextField nameField = new JTextField(name, 20);
                                nameField.setEditable(false); // Make it non-editable

                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 0;
                                updateEmployeePanel.add(nameField, updateEmployeeConstraints);

                                JLabel roleLabel = new JLabel("Role:");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 1;
                                updateEmployeePanel.add(roleLabel, updateEmployeeConstraints);

                                JTextField roleField = new JTextField(role, 20);
                                nameField.setEditable(false); // Make it non-editable

                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 1;
                                updateEmployeePanel.add(roleField, updateEmployeeConstraints);

                                JLabel salaryLabel = new JLabel("Salary due:");
                                nameField.setEditable(false); // Make it non-editable

                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 2;
                                updateEmployeePanel.add(salaryLabel, updateEmployeeConstraints);

                                JTextField salaryField = new JTextField(salary, 20);
                                nameField.setEditable(false); // Make it non-editable

                                updateEmployeeConstraints.gridx = 1;
                                updateEmployeeConstraints.gridy = 2;
                                updateEmployeePanel.add(salaryField, updateEmployeeConstraints);

                                JButton saveButton = new JButton("Disburse");
                                updateEmployeeConstraints.gridx = 0;
                                updateEmployeeConstraints.gridy = 3;
                                updateEmployeeConstraints.gridwidth = 2;
                                updateEmployeeConstraints.anchor = GridBagConstraints.CENTER;
                                updateEmployeePanel.add(saveButton, updateEmployeeConstraints);

                                // Add ActionListener to save button to update employee details in the database
                                saveButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String newEmployeeName = nameField.getText();
                                        String newEmployeeRole = roleField.getText();
                                        String newEmployeeSalary = salaryField.getText();

                                        // Update employee details in the database
                                        String updateQuery = "UPDATE employee SET days_present = 0 WHERE emp_id = ?";
                                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                            updateStmt.setString(1, empID);
                                            updateStmt.executeUpdate();

                                            // Refresh the table data after updating employee
                                            try (PreparedStatement refreshStmt = conn.prepareStatement(query);
                                                    ResultSet refreshRs = refreshStmt.executeQuery()) {
                                                table[0].setModel(buildTableModel(refreshRs)); // Update the table model
                                            }
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                            JOptionPane.showMessageDialog(updateEmployeeDialog,
                                                    "Error updating employee: " + ex.getMessage());
                                        }

                                        updateEmployeeDialog.dispose(); // Close the dialog after saving
                                    }
                                });

                                updateEmployeeDialog.add(updateEmployeePanel);
                                updateEmployeeDialog.setLocationRelativeTo(welcomeFrame);
                                updateEmployeeDialog.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(welcomeFrame, "No employee found with ID: " + empID);
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(welcomeFrame, "Error updating employee: " + ex.getMessage());
                    }
                }
            }
        });

        welcomeFrame.add(panel);
        welcomeFrame.pack();
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                vector.add(rs.getObject(i));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

}
