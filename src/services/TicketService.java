package services;

import java.util.ArrayList;
import java.util.List;
import models.Ticket;
import utils.FileUtil;

public class TicketService {
    private final String ticketFilePath;
    private final String receiptDirectory;
    private final TrainService trainService;

    public TicketService(String ticketFilePath, String receiptDirectory, TrainService trainService) {
        this.ticketFilePath = ticketFilePath;
        this.receiptDirectory = receiptDirectory;
        this.trainService = trainService;
    }

    public Ticket bookTicket(String username, String passengerName, String trainId, String journeyDate) {
        if (!trainService.reserveSeat(trainId)) {
            return null;
        }

        models.Train train = trainService.findTrainById(trainId);
        if (train == null) {
            return null;
        }

        List<Ticket> tickets = getAllTickets();
        String ticketId = "TKT" + String.format("%03d", tickets.size() + 1);
        Ticket ticket = new Ticket(
                ticketId,
                username,
                train.getTrainId(),
                train.getTrainName(),
                passengerName,
                journeyDate,
                "BOOKED"
        );

        tickets.add(ticket);
        saveTickets(tickets);
        writeReceipt(ticket);
        return ticket;
    }

    public boolean cancelTicket(String ticketId, String username) {
        List<Ticket> tickets = getAllTickets();

        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId)
                    && ticket.getUsername().equalsIgnoreCase(username)
                    && !"CANCELLED".equalsIgnoreCase(ticket.getStatus())) {
                ticket.setStatus("CANCELLED");
                trainService.restoreSeat(ticket.getTrainId());
                saveTickets(tickets);
                return true;
            }
        }
        return false;
    }

    public List<Ticket> getTicketsByUsername(String username) {
        List<Ticket> userTickets = new ArrayList<>();

        for (Ticket ticket : getAllTickets()) {
            if (ticket.getUsername().equalsIgnoreCase(username)) {
                userTickets.add(ticket);
            }
        }
        return userTickets;
    }

    public List<Ticket> getAllTickets() {
        List<String> lines = FileUtil.readLines(ticketFilePath);
        List<Ticket> tickets = new ArrayList<>();

        for (String line : lines) {
            tickets.add(Ticket.fromFileString(line));
        }
        return tickets;
    }

    public String getReceiptPath(String ticketId) {
        return receiptDirectory + "/" + ticketId + ".txt";
    }

    public int getBookedTicketCount(String username) {
        int count = 0;

        for (Ticket ticket : getTicketsByUsername(username)) {
            if ("BOOKED".equalsIgnoreCase(ticket.getStatus())) {
                count++;
            }
        }
        return count;
    }

    private void saveTickets(List<Ticket> tickets) {
        List<String> lines = new ArrayList<>();

        for (Ticket ticket : tickets) {
            lines.add(ticket.toFileString());
        }
        FileUtil.writeLines(ticketFilePath, lines);
    }

    private void writeReceipt(Ticket ticket) {
        List<String> receiptLines = new ArrayList<>();
        receiptLines.add("RailEase Ticket Receipt");
        receiptLines.add("-----------------------");
        receiptLines.add("Ticket ID    : " + ticket.getTicketId());
        receiptLines.add("Username     : " + ticket.getUsername());
        receiptLines.add("Passenger    : " + ticket.getPassengerName());
        receiptLines.add("Train ID     : " + ticket.getTrainId());
        receiptLines.add("Train Name   : " + ticket.getTrainName());
        receiptLines.add("Journey Date : " + ticket.getJourneyDate());
        receiptLines.add("Status       : " + ticket.getStatus());

        FileUtil.writeLines(getReceiptPath(ticket.getTicketId()), receiptLines);
    }
}
