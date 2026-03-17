import java.util.*;

// Booking Record (Confirmed Booking)
class CancelBookingRecord {
    private String reservationId;
    private String roomType;
    private String roomId;

    public CancelBookingRecord(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

// Inventory Service
class CancelInventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public void incrementRoom(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// Booking History (tracks confirmed bookings)
class CancelBookingHistory {
    private Map<String, CancelBookingRecord> bookings = new HashMap<>();

    public void addBooking(CancelBookingRecord record) {
        bookings.put(record.getReservationId(), record);
    }

    public CancelBookingRecord getBooking(String reservationId) {
        return bookings.get(reservationId);
    }

    public void removeBooking(String reservationId) {
        bookings.remove(reservationId);
    }

    public boolean exists(String reservationId) {
        return bookings.containsKey(reservationId);
    }
}

// Cancellation Service (Core Logic)
class CancellationService {

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              CancelBookingHistory history,
                              CancelInventoryService inventory) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Step 1: Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("❌ Cancellation Failed: Reservation not found.");
            return;
        }

        // Step 2: Fetch booking
        CancelBookingRecord record = history.getBooking(reservationId);

        // Step 3: Push room ID to rollback stack
        rollbackStack.push(record.getRoomId());

        // Step 4: Restore inventory
        inventory.incrementRoom(record.getRoomType());

        // Step 5: Remove booking from history
        history.removeBooking(reservationId);

        // Step 6: Confirmation
        System.out.println("✅ Cancellation Successful!");
        System.out.println("Released Room ID: " + record.getRoomId());
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class uc10 {
    public static void main(String[] args) {

        // Step 1: Setup inventory
        CancelInventoryService inventory = new CancelInventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Double", 0);

        // Step 2: Setup booking history (simulate confirmed bookings)
        CancelBookingHistory history = new CancelBookingHistory();
        history.addBooking(new CancelBookingRecord("RES101", "Single", "SI1"));
        history.addBooking(new CancelBookingRecord("RES102", "Double", "DO1"));

        // Step 3: Cancellation service
        CancellationService service = new CancellationService();

        // Step 4: Perform cancellations
        service.cancelBooking("RES101", history, inventory); // valid
        service.cancelBooking("RES999", history, inventory); // invalid
        service.cancelBooking("RES101", history, inventory); // already cancelled

        // Step 5: View rollback stack
        service.showRollbackStack();

        // Step 6: Check inventory
        inventory.displayInventory();
    }
}