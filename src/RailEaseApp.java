import java.util.List;
import java.util.Scanner;
import models.Ticket;
import models.Train;
import models.User;
import services.TicketService;
import services.TrainService;
import services.UserService;

public class RailEaseApp {
    private static final String USERS_FILE = "data/users.txt";
    private static final String TRAINS_FILE = "data/trains.txt";
    private static final String TICKETS_FILE = "data/tickets.txt";
    private static final String RECEIPTS_DIR = "data/receipts";

    private final UserService userService;
    private final TrainService trainService;
    private final TicketService ticketService;
    private final Scanner scanner;

    public RailEaseApp() {
        this.userService = new UserService(USERS_FILE);
        this.trainService = new TrainService(TRAINS_FILE);
        this.ticketService = new TicketService(TICKETS_FILE, RECEIPTS_DIR, trainService);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new RailEaseApp().start();
    }

    public void start() {
        System.out.println("==========================================");
        System.out.println("   RailEase - Railway Reservation System  ");
        System.out.println("==========================================");

        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    handleRegistration();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    System.out.println("Thank you for using RailEase.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleRegistration() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        boolean success = userService.registerUser(username, password, "PASSENGER");
        if (success) {
            System.out.println("Registration successful. You can now log in.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = userService.login(username, password);
        if (user == null) {
            System.out.println("Invalid credentials.");
            return;
        }

        System.out.println("Login successful. Welcome, " + user.getUsername() + ".");
        passengerMenu(user);
    }

    private void passengerMenu(User user) {
        while (true) {
            System.out.println("\n1. View trains");
            System.out.println("2. Book ticket");
            System.out.println("3. View my tickets");
            System.out.println("4. Cancel ticket");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    displayTrains();
                    break;
                case 2:
                    bookTicket(user);
                    break;
                case 3:
                    displayTickets(user.getUsername());
                    break;
                case 4:
                    cancelTicket(user.getUsername());
                    break;
                case 5:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayTrains() {
        List<Train> trains = trainService.getAllTrains();

        if (trains.isEmpty()) {
            System.out.println("No trains available.");
            return;
        }

        for (Train train : trains) {
            train.display();
        }
    }

    private void bookTicket(User user) {
        displayTrains();

        System.out.print("Enter train ID: ");
        String trainId = scanner.nextLine().trim();
        System.out.print("Enter passenger name: ");
        String passengerName = scanner.nextLine().trim();
        System.out.print("Enter journey date (DD-MM-YYYY): ");
        String journeyDate = scanner.nextLine().trim();

        if (trainService.findTrainById(trainId) == null) {
            System.out.println("Train not found.");
            return;
        }

        Ticket ticket = ticketService.bookTicket(user.getUsername(), passengerName, trainId, journeyDate);
        if (ticket == null) {
            System.out.println("Booking failed. Seats may not be available.");
            return;
        }

        System.out.println("Ticket booked successfully.");
        ticket.display();
    }

    private void displayTickets(String username) {
        List<Ticket> tickets = ticketService.getTicketsByUsername(username);

        if (tickets.isEmpty()) {
            System.out.println("No tickets found for this user.");
            return;
        }

        for (Ticket ticket : tickets) {
            ticket.display();
        }
    }

    private void cancelTicket(String username) {
        displayTickets(username);
        System.out.print("Enter ticket ID to cancel: ");
        String ticketId = scanner.nextLine().trim();

        boolean cancelled = ticketService.cancelTicket(ticketId, username);
        if (cancelled) {
            System.out.println("Ticket cancelled successfully.");
        } else {
            System.out.println("Ticket not found or already cancelled.");
        }
    }

    private int readInt() {
        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
