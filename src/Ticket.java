public class Ticket {
    private String ticketId;
    private String username;
    private String trainId;
    private String trainName;
    private String passengerName;
    private String journeyDate;
    private String status;

    public Ticket(String ticketId, String username, String trainId,
                  String trainName, String passengerName, String journeyDate, String status) {
        this.ticketId      = ticketId;
        this.username      = username;
        this.trainId       = trainId;
        this.trainName     = trainName;
        this.passengerName = passengerName;
        this.journeyDate   = journeyDate;
        this.status        = status;
    }

    public String getTicketId()       { return ticketId; }
    public String getUsername()       { return username; }
    public String getTrainId()        { return trainId; }
    public String getStatus()         { return status; }
    public void   setStatus(String s) { this.status = s; }

    public String toFileString() {
        return ticketId + "," + username + "," + trainId + "," +
               trainName + "," + passengerName + "," + journeyDate + "," + status;
    }

    public static Ticket fromFileString(String line) {
        String[] p = line.split(",");
        return new Ticket(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
    }

    public void display() {
        System.out.println("\n========================================");
        System.out.println("         ** TICKET DETAILS **          ");
        System.out.println("========================================");
        System.out.printf("  Ticket ID : %s%n", ticketId);
        System.out.printf("  Passenger : %s%n", passengerName);
        System.out.printf("  Train     : %s (%s)%n", trainName, trainId);
        System.out.printf("  Date      : %s%n", journeyDate);
        System.out.printf("  Status    : %s%n", status);
        System.out.println("========================================\n");
    }
}