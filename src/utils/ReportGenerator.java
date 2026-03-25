package utils;
import services.FileHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class ReportGenerator {
 private static final String REPORT_FILE = "data/reports.txt";
 private static final DateTimeFormatter fmt =
 DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
 public static void logActivity(String activity) {
 String timestamp = LocalDateTime.now().format(fmt);
 String logLine = "[" + timestamp + "] " + activity;
 FileHandler.appendReport(REPORT_FILE, logLine);
 }
 public static void showAllReports() {
 List<String> reports = FileHandler.loadReports(REPORT_FILE);
 System.out.println("\n======= ACTIVITY REPORTS =======");
 if (reports.isEmpty()) {
 System.out.println(" Koi activity log nahi hai abhi.");
 return;
 }
 for (String r : reports) {
 System.out.println(" " + r);
 }
 System.out.println("================================");
 }
 public static void generateSummary(int totalTrains,
 int confirmedTickets,
 int cancelledTickets) {
 String timestamp = LocalDateTime.now().format(fmt);
 String summary =
 "\n===== SUMMARY REPORT =====" +
 "\nGenerated : " + timestamp +
 "\nTrains : " + totalTrains +
 "\nConfirmed : " + confirmedTickets +
 "\nCancelled : " + cancelledTickets +
 "\n==========================";
 FileHandler.appendReport(REPORT_FILE, summary);
 System.out.println("[Success] Summary report save ho gayi!");
 }
}