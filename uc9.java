import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Booking Request
class BookingInput {
    private String guestName;
    private String roomType;

    public BookingInput(String guestName, String roomType) {
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

// Inventory Service (with validation safety)
class SafeInventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, -1); // -1 means invalid type
    }

    public void decrementRoom(String type) throws InvalidBookingException {
        int count = availability.getOrDefault(type, -1);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        availability.put(type, count - 1);
    }
}

// Validator (Fail-Fast)
class InvalidBookingValidator {

    public void validate(BookingInput input, SafeInventoryService inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (input.getGuestName() == null || input.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type exists
        int availability = inventory.getAvailability(input.getRoomType());
        if (availability == -1) {
            throw new InvalidBookingException("Invalid room type: " + input.getRoomType());
        }

        // Validate availability
        if (availability == 0) {
            throw new InvalidBookingException("Room not available: " + input.getRoomType());
        }
    }
}

// Booking Processor (uses validation)
class SafeBookingService {

    private SafeInventoryService inventory;
    private InvalidBookingValidator validator;

    public SafeBookingService(SafeInventoryService inventory) {
        this.inventory = inventory;
        this.validator = new InvalidBookingValidator();
    }

    public void processBooking(BookingInput input) {
        try {
            // Step 1: Validate (fail-fast)
            validator.validate(input, inventory);

            // Step 2: Safe allocation
            inventory.decrementRoom(input.getRoomType());

            // Step 3: Confirmation
            System.out.println("✅ Booking Successful for " + input.getGuestName()
                    + " (" + input.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("❌ Booking Failed: " + e.getMessage());
        }
    }
}

// Main Class
public class uc9 {
    public static void main(String[] args) {

        // Step 1: Setup inventory
        SafeInventoryService inventory = new SafeInventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Double", 0);

        // Step 2: Booking service
        SafeBookingService bookingService = new SafeBookingService(inventory);

        // Step 3: Test cases

        // Valid booking
        bookingService.processBooking(new BookingInput("Manas", "Single"));

        // Invalid room type
        bookingService.processBooking(new BookingInput("Riya", "Suite"));

        // No availability
        bookingService.processBooking(new BookingInput("Arjun", "Double"));

        // Empty guest name
        bookingService.processBooking(new BookingInput("", "Single"));

        // Booking after inventory exhausted
        bookingService.processBooking(new BookingInput("Neha", "Single"));
    }
}