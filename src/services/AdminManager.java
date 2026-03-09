package services;

import models.Train;
import models.Ticket;
import services.FileHandler;
import utils.ReportGenerator;
import java.util.*;

public class AdminManager {

    private List<Train> trains;
    private List<Ticket> tickets;

    private static final String TRAINS_FILE = "data/trains.txt";
    private static final String TICKETS_FILE = "data/bookings.txt";

    public AdminManager() {
        trains = FileHandler.loadTrains(TRAINS_FILE);
        tickets = FileHandler.loadTickets(TICKETS_FILE);
    }

    // ADD TRAIN
    public void addTrain(Scanner sc) {

        System.out.println("\n--- Add New Train ---");

        System.out.print("Train ID (example EXP101): ");
        String id = sc.nextLine().trim();

        for (Train t : trains) {
            if (t.getTrainId().equalsIgnoreCase(id)) {
                System.out.println("[Error] Train ID already exists!");
                return;
            }
        }

        System.out.print("Train Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Source Station: ");
        String src = sc.nextLine().trim();

        System.out.print("Destination Station: ");
        String dest = sc.nextLine().trim();

        System.out.print("Distance (km): ");
        int distance;

        try {
            distance = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Distance must be a number!");
            return;
        }

        System.out.print("Total Seats: ");
        int seats;

        try {
            seats = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Seats must be a number!");
            return;
        }

        System.out.print("Departure Time (example 08:30 AM): ");
        String time = sc.nextLine().trim();

        Train train = new Train(id, name, src, dest, seats, seats, time);

        trains.add(train);

        FileHandler.saveTrains(TRAINS_FILE, trains);

        ReportGenerator.logActivity("TRAIN_ADDED: " + id + " - " + name);

        System.out.println("[Success] Train added successfully!");
    }

    // REMOVE TRAIN
    public void removeTrain(Scanner sc) {

        if (trains.isEmpty()) {
            System.out.println("No trains available.");
            return;
        }

        for (Train t : trains) {
            t.display();
        }

        System.out.print("\nEnter Train ID to remove: ");
        String id = sc.nextLine().trim();

        String removedName = "";

        for (Train t : trains) {
            if (t.getTrainId().equalsIgnoreCase(id)) {
                removedName = t.getTrainName();
                break;
            }
        }

        boolean removed = trains.removeIf(t ->
                t.getTrainId().equalsIgnoreCase(id)
        );

        if (removed) {

            FileHandler.saveTrains(TRAINS_FILE, trains);

            ReportGenerator.logActivity(
                    "TRAIN_REMOVED: " + id + " - " + removedName
            );

            System.out.println("[Success] Train removed successfully!");

        } else {

            System.out.println("[Error] Train ID not found.");

        }
    }

    // SHOW ADMIN STATS
    public void showStats() {

        System.out.println("\n======= ADMIN STATS =======");

        System.out.println("Total Trains : " + trains.size());

        int confirmed = 0;
        int cancelled = 0;

        for (Ticket t : tickets) {

            if (t.getStatus().equalsIgnoreCase("CONFIRMED")) {
                confirmed++;
            } else {
                cancelled++;
            }

        }

        System.out.println("Confirmed Tickets : " + confirmed);
        System.out.println("Cancelled Tickets : " + cancelled);
        System.out.println("Total Bookings : " + tickets.size());

        System.out.println("===========================");
    }

    // SHOW ALL TRAINS
    public void showAllTrains() {

        if (trains.isEmpty()) {
            System.out.println("No trains available.");
            return;
        }

        System.out.println("\n======= ALL TRAINS =======");

        for (Train t : trains) {
            t.display();
        }

    }
}