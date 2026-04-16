package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import models.Ticket;
import models.Train;
import models.User;
import services.TicketService;
import services.TrainService;
import services.UserService;

public class RailEaseUI extends JFrame {
    private static final String USERS_FILE = "data/users.txt";
    private static final String TRAINS_FILE = "data/trains.txt";
    private static final String TICKETS_FILE = "data/tickets.txt";
    private static final String RECEIPTS_DIR = "data/receipts";

    private final UserService userService = new UserService(USERS_FILE);
    private final TrainService trainService = new TrainService(TRAINS_FILE);
    private final TicketService ticketService = new TicketService(TICKETS_FILE, RECEIPTS_DIR, trainService);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(cardLayout);

    private User currentUser;

    private JTextField loginUsernameField;
    private JTextField loginPasswordField;
    private JTextField registerUsernameField;
    private JTextField registerPasswordField;

    private JTextField passengerField;
    private JTextField dateField;
    private JComboBox<String> trainSelector;
    private JLabel welcomeLabel;
    private JLabel infoLabel;
    private JTable trainTable;
    private JTable ticketTable;

    public RailEaseUI() {
        setTitle("RailEase Desktop Reservation System");
        setSize(1020, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);

        rootPanel.add(buildAuthPanel(), "auth");
        rootPanel.add(buildDashboardPanel(), "dashboard");
        cardLayout.show(rootPanel, "auth");
    }

    public static void main(String[] args) {
        setSystemLookAndFeel();
        SwingUtilities.invokeLater(() -> new RailEaseUI().setVisible(true));
    }

    private JPanel buildAuthPanel() {
        JPanel outer = new JPanel(new GridLayout(1, 2, 24, 0));
        outer.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        outer.setBackground(new Color(241, 244, 248));

        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBackground(new Color(28, 62, 96));
        heroPanel.setBorder(BorderFactory.createEmptyBorder(40, 32, 40, 32));

        JLabel title = new JLabel("RailEase");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel subtitle = new JLabel("<html>Simple railway reservation project with Java Swing and file handling.</html>");
        subtitle.setForeground(new Color(220, 228, 236));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel points = new JLabel("<html><br>• User login and register<br>• Ticket booking and cancellation<br>• Receipt saved as text file</html>");
        points.setForeground(new Color(208, 220, 232));
        points.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        heroPanel.add(title);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        heroPanel.add(subtitle);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        heroPanel.add(points);

        JPanel formsPanel = new JPanel(new GridLayout(2, 1, 0, 18));
        formsPanel.setOpaque(false);
        formsPanel.add(buildLoginCard());
        formsPanel.add(buildRegisterCard());

        outer.add(heroPanel);
        outer.add(formsPanel);
        return outer;
    }

    private JPanel buildLoginCard() {
        JPanel panel = createCardPanel("Login");

        loginUsernameField = new JTextField();
        loginPasswordField = new JTextField();

        JButton loginButton = createPrimaryButton("Login");
        loginButton.addActionListener(e -> handleLogin());

        panel.add(labelValue("Username", loginUsernameField));
        panel.add(labelValue("Password", loginPasswordField));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(loginButton);
        return panel;
    }

    private JPanel buildRegisterCard() {
        JPanel panel = createCardPanel("New User");

        registerUsernameField = new JTextField();
        registerPasswordField = new JTextField();

        JButton registerButton = createSecondaryButton("Create Account");
        registerButton.addActionListener(e -> handleRegister());

        panel.add(labelValue("Username", registerUsernameField));
        panel.add(labelValue("Password", registerPasswordField));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(registerButton);
        return panel;
    }

    private JPanel buildDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout(16, 16));
        dashboard.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        dashboard.setBackground(new Color(245, 247, 250));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        infoLabel = new JLabel("Simple file-based reservation dashboard");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        infoLabel.setForeground(new Color(88, 102, 117));

        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.addActionListener(e -> logout());

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(welcomeLabel);
        titlePanel.add(infoLabel);

        topBar.add(titlePanel, BorderLayout.WEST);
        topBar.add(logoutButton, BorderLayout.EAST);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);

        JPanel bookingPanel = new JPanel(new BorderLayout(12, 12));
        bookingPanel.setBackground(Color.WHITE);
        bookingPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 232)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        JPanel bookingForm = new JPanel(new GridLayout(3, 2, 10, 10));
        bookingForm.setOpaque(false);
        passengerField = new JTextField();
        dateField = new JTextField();
        trainSelector = new JComboBox<>();

        JButton bookButton = createPrimaryButton("Book Ticket");
        bookButton.addActionListener(e -> bookSelectedTrain());

        bookingForm.add(new JLabel("Passenger Name"));
        bookingForm.add(passengerField);
        bookingForm.add(new JLabel("Journey Date"));
        bookingForm.add(dateField);
        bookingForm.add(new JLabel("Train"));
        bookingForm.add(trainSelector);

        JPanel bookingActions = new JPanel(new GridLayout(1, 1, 10, 0));
        bookingActions.setOpaque(false);
        bookingActions.add(bookButton);

        trainTable = new JTable();
        JScrollPane trainScroll = new JScrollPane(trainTable);

        bookingPanel.add(createSectionTitle("Book Ticket"), BorderLayout.NORTH);
        bookingPanel.add(bookingForm, BorderLayout.CENTER);
        bookingPanel.add(bookingActions, BorderLayout.SOUTH);

        JPanel leftWrapper = new JPanel(new BorderLayout(10, 10));
        leftWrapper.setOpaque(false);
        leftWrapper.add(bookingPanel, BorderLayout.NORTH);
        leftWrapper.add(trainScroll, BorderLayout.CENTER);

        JPanel ticketsPanel = new JPanel(new BorderLayout(12, 12));
        ticketsPanel.setBackground(Color.WHITE);
        ticketsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 232)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        ticketTable = new JTable();
        JScrollPane ticketScroll = new JScrollPane(ticketTable);

        JButton cancelButton = createSecondaryButton("Cancel Selected Ticket");
        cancelButton.addActionListener(e -> cancelSelectedTicket());
        JButton receiptButton = createPrimaryButton("Open Receipt");
        receiptButton.addActionListener(e -> openSelectedReceipt());

        JPanel ticketActions = new JPanel(new GridLayout(1, 2, 10, 0));
        ticketActions.setOpaque(false);
        ticketActions.add(cancelButton);
        ticketActions.add(receiptButton);

        ticketsPanel.add(createSectionTitle("My Tickets"), BorderLayout.NORTH);
        ticketsPanel.add(ticketScroll, BorderLayout.CENTER);
        ticketsPanel.add(ticketActions, BorderLayout.SOUTH);

        center.add(leftWrapper);
        center.add(ticketsPanel);

        dashboard.add(topBar, BorderLayout.NORTH);
        dashboard.add(center, BorderLayout.CENTER);
        return dashboard;
    }

    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText().trim();
        User user = userService.login(username, password);

        if (user == null) {
            showMessage("Invalid username or password.");
            return;
        }

        currentUser = user;
        welcomeLabel.setText("Welcome, " + currentUser.getUsername());
        refreshDashboard();
        cardLayout.show(rootPanel, "dashboard");
    }

    private void handleRegister() {
        String username = registerUsernameField.getText().trim();
        String password = registerPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in both username and password.");
            return;
        }

        boolean created = userService.registerUser(username, password, "PASSENGER");
        if (!created) {
            showMessage("Username already exists.");
            return;
        }

        registerUsernameField.setText("");
        registerPasswordField.setText("");
        showMessage("Account created successfully. You can login now.");
    }

    private void refreshDashboard() {
        refreshTrains(trainService.getAllTrains());
        refreshTickets();
    }

    private void refreshTrains(List<Train> trains) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Train ID", "Train Name", "Route", "Departure", "Seats Left"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        trainSelector.removeAllItems();
        for (Train train : trains) {
            model.addRow(new Object[]{
                    train.getTrainId(),
                    train.getTrainName(),
                    train.getSource() + " -> " + train.getDestination(),
                    train.getDepartureTime(),
                    train.getAvailableSeats() + "/" + train.getTotalSeats()
            });
            trainSelector.addItem(train.getTrainId() + " - " + train.getTrainName());
        }
        trainTable.setModel(model);
    }

    private void refreshTickets() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Ticket ID", "Passenger", "Train", "Date", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Ticket> tickets = ticketService.getTicketsByUsername(currentUser.getUsername());
        for (Ticket ticket : tickets) {
            model.addRow(new Object[]{
                    ticket.getTicketId(),
                    ticket.getPassengerName(),
                    ticket.getTrainName(),
                    ticket.getJourneyDate(),
                    ticket.getStatus()
            });
        }
        ticketTable.setModel(model);
        infoLabel.setText("Booked tickets: " + ticketService.getBookedTicketCount(currentUser.getUsername())
                + " | Receipt file is created for every booking");
    }

    private void bookSelectedTrain() {
        if (trainSelector.getSelectedItem() == null) {
            showMessage("Select a train first.");
            return;
        }

        String passengerName = passengerField.getText().trim();
        String journeyDate = dateField.getText().trim();

        if (passengerName.isEmpty() || journeyDate.isEmpty()) {
            showMessage("Passenger name and journey date are required.");
            return;
        }

        String selected = trainSelector.getSelectedItem().toString();
        String trainId = selected.split(" - ")[0];

        Ticket ticket = ticketService.bookTicket(currentUser.getUsername(), passengerName, trainId, journeyDate);
        if (ticket == null) {
            showMessage("Booking failed. Seats may be unavailable.");
            return;
        }

        refreshDashboard();
        passengerField.setText("");
        dateField.setText("");
        showMessage("Ticket booked successfully. Receipt saved to file.");
    }

    private void cancelSelectedTicket() {
        int row = ticketTable.getSelectedRow();
        if (row == -1) {
            showMessage("Select a ticket to cancel.");
            return;
        }

        String ticketId = ticketTable.getValueAt(row, 0).toString();
        boolean cancelled = ticketService.cancelTicket(ticketId, currentUser.getUsername());
        if (!cancelled) {
            showMessage("Ticket could not be cancelled.");
            return;
        }

        refreshDashboard();
        showMessage("Ticket cancelled successfully.");
    }

    private void openSelectedReceipt() {
        int row = ticketTable.getSelectedRow();
        if (row == -1) {
            showMessage("Select a ticket first.");
            return;
        }

        String ticketId = ticketTable.getValueAt(row, 0).toString();
        File receiptFile = new File(ticketService.getReceiptPath(ticketId));
        if (!receiptFile.exists()) {
            showMessage("Receipt file not found for this ticket.");
            return;
        }

        try {
            Desktop.getDesktop().open(receiptFile);
        } catch (IOException e) {
            showMessage("Unable to open the receipt file.");
        }
    }

    private void logout() {
        currentUser = null;
        loginUsernameField.setText("");
        loginPasswordField.setText("");
        passengerField.setText("");
        dateField.setText("");
        cardLayout.show(rootPanel, "auth");
    }

    private JPanel createCardPanel(String titleText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 223, 230)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        return card;
    }

    private JPanel labelValue(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        return panel;
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return label;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(34, 111, 176));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(230, 236, 241));
        button.setForeground(new Color(34, 51, 67));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }
}
