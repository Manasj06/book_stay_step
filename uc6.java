import java.util.*;

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes in FIFO order
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory Service (State Holder)
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    // Decrement inventory safely
    public void decrementRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// Booking Service (Core Allocation Logic)
class BookingService {

    // Set to ensure unique room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map roomType -> allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private int roomCounter = 1; // for generating unique IDs

    public void processBookings(BookingRequestQueue queue, InventoryService inventory) {

        while (!queue.isEmpty()) {
            Reservation request = queue.getNextRequest();

            String roomType = request.getRoomType();
            String guest = request.getGuestName();

            System.out.println("\nProcessing request for " + guest + " (" + roomType + ")");

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId;
                do {
                    roomId = roomType.substring(0, 2).toUpperCase() + roomCounter++;
                } while (allocatedRoomIds.contains(roomId));

                // Add to Set (ensures uniqueness)
                allocatedRoomIds.add(roomId);

                // Map room type to allocated IDs
                roomAllocations.putIfAbsent(roomType, new HashSet<>());
                roomAllocations.get(roomType).add(roomId);

                // Inventory update (atomic with allocation)
                inventory.decrementRoom(roomType);

                // Confirmation
                System.out.println("✅ Booking Confirmed!");
                System.out.println("Guest: " + guest);
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("❌ Booking Failed for " + guest + " (No rooms available)");
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\nFinal Room Allocations:");
        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }
    }
}

// Main Class
public class uc6 {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);
        inventory.addRoomType("Suite", 1);

        // Step 2: Setup Booking Queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Manas", "Single"));
        queue.addRequest(new Reservation("Riya", "Single"));
        queue.addRequest(new Reservation("Arjun", "Single")); // should fail
        queue.addRequest(new Reservation("Neha", "Suite"));

        // Step 3: Process Bookings
        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);

        // Step 4: Show Results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}