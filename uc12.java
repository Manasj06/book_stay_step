import java.io.*;
import java.util.*;

// Serializable Booking Record
class PersistBookingRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public PersistBookingRecord(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Serializable Inventory
class PersistInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public Map<String, Integer> getAvailability() {
        return availability;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// System State (Snapshot)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<PersistBookingRecord> bookings;
    private PersistInventory inventory;

    public SystemState(List<PersistBookingRecord> bookings, PersistInventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }

    public List<PersistBookingRecord> getBookings() {
        return bookings;
    }

    public PersistInventory getInventory() {
        return inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("\n✅ System state saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving system state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("\n✅ System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("\n⚠ No saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n❌ Error loading state. Starting fresh.");
        }
        return null;
    }
}

// Main Class
public class uc12 {
    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        // Step 1: Try loading existing state
        SystemState state = service.load();

        List<PersistBookingRecord> bookings;
        PersistInventory inventory;

        if (state == null) {
            // Fresh start
            bookings = new ArrayList<>();
            inventory = new PersistInventory();

            inventory.addRoomType("Single", 2);
            inventory.addRoomType("Double", 1);

            bookings.add(new PersistBookingRecord("RES101", "Manas", "Single"));
            bookings.add(new PersistBookingRecord("RES102", "Riya", "Double"));

            System.out.println("\nInitialized new system state.");
        } else {
            // Restore state
            bookings = state.getBookings();
            inventory = state.getInventory();

            System.out.println("\nRestored System State:");
        }

        // Step 2: Display current state
        System.out.println("\nBookings:");
        for (PersistBookingRecord b : bookings) {
            System.out.println(b);
        }

        inventory.display();

        // Step 3: Simulate new booking
        bookings.add(new PersistBookingRecord("RES103", "Arjun", "Single"));
        System.out.println("\nNew booking added: RES103");

        // Step 4: Save state before shutdown
        SystemState newState = new SystemState(bookings, inventory);
        service.save(newState);
    }
}