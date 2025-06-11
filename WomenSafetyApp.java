import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class User {
    String name;
    String phone;
    String emergencyContact;

    User(String name, String phone, String emergencyContact) {
        this.name = name;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
    }
}

class Incident {
    String time;
    String location;
    String message;

    Incident(String time, String location, String message) {
        this.time = time;
        this.location = location;
        this.message = message;
    }

    public String toString() {
        return "Time: " + time + ", Location: " + location + ", Message: " + message;
    }
}

public class WomenSafetyApp extends JFrame {
    private User currentUser = null;
    private ArrayList<Incident> incidentLog = new ArrayList<>();

    // GUI components
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Registration components
    private JTextField nameField, phoneField, emergencyContactField;
    private JButton registerButton;

    // Panic button components
    private JTextField locationField;
    private JButton panicButton;

    // Incident log
    private JTextArea incidentArea;
    private JButton viewIncidentsButton, backToMenuButton;

    // Menu buttons
    private JButton toRegisterButton, toPanicButton, toViewIncidentsButton, exitButton;

    public WomenSafetyApp() {
        setTitle("Women Safety Tracker App");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Build screens
        buildMenu();
        buildRegisterScreen();
        buildPanicScreen();
        buildIncidentLogScreen();

        add(mainPanel);

        cardLayout.show(mainPanel, "menu");
        setVisible(true);
    }

    private void buildMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        toRegisterButton = new JButton("1. Register");
        toPanicButton = new JButton("2. Trigger Panic Button");
        toViewIncidentsButton = new JButton("3. View Incident Log");
        exitButton = new JButton("4. Exit");

        menuPanel.add(new JLabel("--- Women Safety Tracker App ---", SwingConstants.CENTER));
        menuPanel.add(toRegisterButton);
        menuPanel.add(toPanicButton);
        menuPanel.add(toViewIncidentsButton);
        menuPanel.add(exitButton);

        toRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));
        toPanicButton.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please register first.");
            } else {
                cardLayout.show(mainPanel, "panic");
            }
        });
        toViewIncidentsButton.addActionListener(e -> {
            updateIncidentLog();
            cardLayout.show(mainPanel, "incidents");
        });
        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Stay safe. Goodbye!");
            System.exit(0);
        });

        mainPanel.add(menuPanel, "menu");
    }

    private void buildRegisterScreen() {
        JPanel registerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        registerPanel.add(new JLabel("Enter your name:"));
        nameField = new JTextField();
        registerPanel.add(nameField);

        registerPanel.add(new JLabel("Enter your phone number:"));
        phoneField = new JTextField();
        registerPanel.add(phoneField);

        registerPanel.add(new JLabel("Enter emergency contact number:"));
        emergencyContactField = new JTextField();
        registerPanel.add(emergencyContactField);

        registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Menu");

        registerPanel.add(registerButton);
        registerPanel.add(backButton);

        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        mainPanel.add(registerPanel, "register");
    }

    private void buildPanicScreen() {
        JPanel panicPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        panicPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        panicPanel.add(new JLabel("⛔ Panic Button Activated!"));
        panicPanel.add(new JLabel("Enter your current location:"));

        locationField = new JTextField();
        panicPanel.add(locationField);

        panicButton = new JButton("Send Alert");
        JButton backButton = new JButton("Back to Menu");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(panicButton);
        buttonPanel.add(backButton);

        panicPanel.add(buttonPanel);

        panicButton.addActionListener(e -> panicButtonAction());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        mainPanel.add(panicPanel, "panic");
    }

    private void buildIncidentLogScreen() {
        JPanel incidentPanel = new JPanel(new BorderLayout(10, 10));
        incidentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        incidentArea = new JTextArea();
        incidentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(incidentArea);

        backToMenuButton = new JButton("Back to Menu");

        incidentPanel.add(new JLabel("--- Incident History ---", SwingConstants.CENTER), BorderLayout.NORTH);
        incidentPanel.add(scrollPane, BorderLayout.CENTER);
        incidentPanel.add(backToMenuButton, BorderLayout.SOUTH);

        backToMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        mainPanel.add(incidentPanel, "incidents");
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String contact = emergencyContactField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        currentUser = new User(name, phone, contact);
        JOptionPane.showMessageDialog(this, "User registered successfully.");
        nameField.setText("");
        phoneField.setText("");
        emergencyContactField.setText("");
        cardLayout.show(mainPanel, "menu");
    }

    private void panicButtonAction() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please register first.");
            cardLayout.show(mainPanel, "menu");
            return;
        }

        String location = locationField.getText().trim();
        if (location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your location.");
            return;
        }

        String alertMessage = "🚨 Emergency! " + currentUser.name + " is in danger at " + location + ". Please help immediately.";
        JOptionPane.showMessageDialog(this, "📩 Sending alert to: " + currentUser.emergencyContact + "\nMessage: " + alertMessage);

        incidentLog.add(new Incident(java.time.LocalTime.now().toString(), location, alertMessage));
        locationField.setText("");
        cardLayout.show(mainPanel, "menu");
    }

    private void updateIncidentLog() {
        if (incidentLog.isEmpty()) {
            incidentArea.setText("No incidents recorded.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Incident inc : incidentLog) {
                sb.append(inc.toString()).append("\n");
            }
            incidentArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WomenSafetyApp::new);
    }
}
