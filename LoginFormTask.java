import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    public WelcomePage(User user) {
        setTitle("Welcome");
        setSize(350, 180);
        setLayout(new GridLayout(2, 1));

        add(new Label(
            "Welcome, " + user.getUsername(),
            Label.CENTER
        ));

        add(new Label(
            "Role: " + user.getRole(),
            Label.CENTER
        ));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

public class LoginFormTask extends Frame {

    private TextField usernameField;
    private TextField passwordField;
    private Choice roleChoice;
    private Button loginButton;
    private Label statusLabel;

    private int attempts = 0;

    private List<User> users = new ArrayList<>();

    public LoginFormTask() {
        setTitle("Login Form");
        setSize(400, 280);
        setLayout(new GridLayout(5, 2, 8, 8));

        loadUsers();

        add(new Label("Username:"));

        usernameField = new TextField();
        add(usernameField);

        add(new Label("Password:"));

        passwordField = new TextField();
        passwordField.setEchoChar('*');
        add(passwordField);

        add(new Label("Role:"));

        roleChoice = new Choice();
        roleChoice.add("Student");
        roleChoice.add("TA");
        roleChoice.add("Professor");
        add(roleChoice);

        loginButton = new Button("Login");
        Button cancelButton = new Button("Cancel");

        statusLabel = new Label("Enter your login details.");

        add(loginButton);
        add(cancelButton);
        add(statusLabel);
        add(new Label(""));

        loginButton.addActionListener(e -> login());

        cancelButton.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String selectedRole = roleChoice.getSelectedItem();

        // Check empty fields
        if (username.isEmpty() || password.trim().isEmpty()) {
            showMessage("Please enter username and password.");
            return;
        }

        // Find the user
        User user = findUser(username);

        // Validate username, password, and role
        if (user != null
                && user.getPassword().equals(password)
                && user.getRole().equals(selectedRole)) {

            showMessage("Login successful!");

            new WelcomePage(user);
            dispose();

        } else {
            attempts++;

            if (attempts >= 3) {
                loginButton.setEnabled(false);
                statusLabel.setText("Login disabled.");

                showMessage(
                    "Too many failed attempts. Login disabled."
                );

            } else {
                int remaining = 3 - attempts;

                statusLabel.setText(
                    "Attempts left: " + remaining
                );

                showMessage(
                    "Invalid username, password, or role. Attempts left: "
                    + remaining
                );
            }
        }
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);

        dialog.setLayout(new BorderLayout(10, 10));

        dialog.add(
            new Label(message, Label.CENTER),
            BorderLayout.CENTER
        );

        Button ok = new Button("OK");

        ok.addActionListener(e -> dialog.dispose());

        Panel panel = new Panel();
        panel.add(ok);

        dialog.add(panel, BorderLayout.SOUTH);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        dialog.setSize(360, 130);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Hard-coded users
    private void loadUsers() {
        users.add(new User("student1", "1234", "Student"));
        users.add(new User("ta1", "ta123", "TA"));
        users.add(new User("prof1", "prof123", "Professor"));
    }

    // Search for a user
    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask();
    }
}
