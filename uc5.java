import java.util.*;

// Reservation (Represents booking intent)
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

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests
    public void viewRequests() {
        System.out.println("\nCurrent Booking Queue:\n");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.displayRequest();
        }
    }

    // Peek next request
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Get size
    public int getQueueSize() {
        return queue.size();
    }
}

// Guest (Renamed to avoid conflict)
class BookingGuest {
    private String name;

    public BookingGuest(String name) {
        this.name = name;
    }

    public void makeBookingRequest(String roomType, BookingRequestQueue queue) {
        Reservation reservation = new Reservation(name, roomType);
        queue.addRequest(reservation);
    }
}

// Main Class
public class uc5 {
    public static void main(String[] args) {

        // Initialize queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Create guests
        BookingGuest g1 = new BookingGuest("Manas");
        BookingGuest g2 = new BookingGuest("Riya");
        BookingGuest g3 = new BookingGuest("Arjun");

        // Guests make booking requests
        g1.makeBookingRequest("Single", requestQueue);
        g2.makeBookingRequest("Suite", requestQueue);
        g3.makeBookingRequest("Double", requestQueue);

        // View queue
        requestQueue.viewRequests();

        // Peek next request
        System.out.println("\nNext request to process:");
        Reservation next = requestQueue.peekNextRequest();
        if (next != null) {
            next.displayRequest();
        }

        // Queue size
        System.out.println("\nTotal Requests: " + requestQueue.getQueueSize());
    }
}