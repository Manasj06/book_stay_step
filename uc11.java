import java.util.*;

// Booking Request
class ConcurrentBookingRequest {
    private String guestName;
    private String roomType;

    public ConcurrentBookingRequest(String guestName, String roomType) {
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

// Thread-Safe Booking Queue
class ConcurrentBookingQueue {
    private Queue<ConcurrentBookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(ConcurrentBookingRequest request) {
        queue.offer(request);
        System.out.println("Request added: " + request.getGuestName());
    }

    public synchronized ConcurrentBookingRequest getRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Thread-Safe Inventory
class ConcurrentInventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public ConcurrentInventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
    }

    // Critical Section
    public synchronized boolean allocateRoom(String roomType) {
        int count = availability.getOrDefault(roomType, 0);

        if (count > 0) {
            availability.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// Booking Processor (Runnable Thread)
class BookingProcessor implements Runnable {

    private ConcurrentBookingQueue queue;
    private ConcurrentInventoryService inventory;

    public BookingProcessor(ConcurrentBookingQueue queue, ConcurrentInventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {

            ConcurrentBookingRequest request;

            // Synchronized queue access
            synchronized (queue) {
                if (queue.isEmpty()) break;
                request = queue.getRequest();
            }

            if (request != null) {
                process(request);
            }
        }
    }

    private void process(ConcurrentBookingRequest request) {
        String guest = request.getGuestName();
        String roomType = request.getRoomType();

        // Critical Section (Inventory)
        boolean success = inventory.allocateRoom(roomType);

        if (success) {
            System.out.println("✅ " + guest + " booked " + roomType);
        } else {
            System.out.println("❌ " + guest + " failed (No " + roomType + " available)");
        }
    }
}

// Main Class
public class uc11 {
    public static void main(String[] args) {

        // Step 1: Shared resources
        ConcurrentBookingQueue queue = new ConcurrentBookingQueue();
        ConcurrentInventoryService inventory = new ConcurrentInventoryService();

        // Step 2: Add booking requests
        queue.addRequest(new ConcurrentBookingRequest("Manas", "Single"));
        queue.addRequest(new ConcurrentBookingRequest("Riya", "Single"));
        queue.addRequest(new ConcurrentBookingRequest("Arjun", "Single")); // should fail
        queue.addRequest(new ConcurrentBookingRequest("Neha", "Double"));
        queue.addRequest(new ConcurrentBookingRequest("Karan", "Double")); // should fail

        // Step 3: Create threads (simulate multiple users)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory));
        Thread t2 = new Thread(new BookingProcessor(queue, inventory));
        Thread t3 = new Thread(new BookingProcessor(queue, inventory));

        // Step 4: Start threads
        t1.start();
        t2.start();
        t3.start();

        // Step 5: Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Step 6: Final inventory
        inventory.displayInventory();
    }
}