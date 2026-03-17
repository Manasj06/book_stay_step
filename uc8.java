import java.util.*;

// BookingRecord (represents confirmed reservation)
class BookingRecord {
    private String reservationId;
    private String guestName;
    private String roomType;

    public BookingRecord(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {
    private List<BookingRecord> history = new ArrayList<>();

    // Add booking (called after confirmation)
    public void addBooking(BookingRecord record) {
        history.add(record);
        System.out.println("Booking stored: " + record.getReservationId());
    }

    // Get all bookings (read-only)
    public List<BookingRecord> getAllBookings() {
        return Collections.unmodifiableList(history); // defensive
    }
}

// Booking Report Service
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<BookingRecord> records) {
        System.out.println("\n=== Booking History ===");

        if (records.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (BookingRecord r : records) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(List<BookingRecord> records) {
        System.out.println("\n=== Booking Summary Report ===");

        Map<String, Integer> roomCount = new HashMap<>();

        for (BookingRecord r : records) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (String type : roomCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomCount.get(type));
        }

        System.out.println("Total Bookings: " + records.size());
    }
}

// Admin (Actor)
class Admin {
    public void viewReports(BookingHistory history, BookingReportService reportService) {
        List<BookingRecord> records = history.getAllBookings();

        reportService.displayAllBookings(records);
        reportService.generateSummary(records);
    }
}

// Main Class
public class uc8 {
    public static void main(String[] args) {

        // Step 1: Initialize history
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings
        history.addBooking(new BookingRecord("RES101", "Manas", "Single"));
        history.addBooking(new BookingRecord("RES102", "Riya", "Suite"));
        history.addBooking(new BookingRecord("RES103", "Arjun", "Single"));
        history.addBooking(new BookingRecord("RES104", "Neha", "Double"));

        // Step 3: Admin views reports
        Admin admin = new Admin();
        BookingReportService reportService = new BookingReportService();

        admin.viewReports(history, reportService);
    }
}