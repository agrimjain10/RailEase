package models;

public class Train {
    private String trainId;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private String departureTime;

    public Train(String trainId, String trainName, String source,
                 String destination, int totalSeats, int availableSeats, String departureTime) {
        this.trainId        = trainId;
        this.trainName      = trainName;
        this.source         = source;
        this.destination    = destination;
        this.totalSeats     = totalSeats;
        this.availableSeats = availableSeats;
        this.departureTime  = departureTime;
    }

    public String getTrainId()        { return trainId; }
    public String getTrainName()      { return trainName; }
    public String getSource()         { return source; }
    public String getDestination()    { return destination; }
    public int    getTotalSeats()     { return totalSeats; }
    public int    getAvailableSeats() { return availableSeats; }
    public String getDepartureTime()  { return departureTime; }
    public int getBookedSeats()       { return totalSeats - availableSeats; }

    public int getOccupancyPercentage() {
        if (totalSeats == 0) {
            return 0;
        }
        return (getBookedSeats() * 100) / totalSeats;
    }

    public boolean bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean cancelSeat() {
        if (availableSeats < totalSeats) {
            availableSeats++;
            return true;
        }
        return false;
    }

    public String toFileString() {
        return trainId + "," + trainName + "," + source + "," +
               destination + "," + totalSeats + "," + availableSeats + "," + departureTime;
    }

    public static Train fromFileString(String line) {
        String[] p = line.split(",");
        return new Train(p[0], p[1], p[2], p[3],
                Integer.parseInt(p[4]), Integer.parseInt(p[5]), p[6]);
    }

    public void display() {
        System.out.println("+--------------------------------------------------+");
        System.out.printf("| Train ID   : %-35s|\n", trainId);
        System.out.printf("| Name       : %-35s|\n", trainName);
        System.out.printf("| Route      : %-35s|\n", source + " --> " + destination);
        System.out.printf("| Departure  : %-35s|\n", departureTime);
        System.out.printf("| Seats Left : %-35s|\n", availableSeats + " / " + totalSeats);
        System.out.println("+--------------------------------------------------+");
    }
}
