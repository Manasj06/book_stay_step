/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates object-oriented modeling using abstraction,
 * inheritance, and polymorphism for different room types in a hotel booking system.
 *
 * @author Manas
 * @version 2.1
 */
abstract class Room {

    // Common attributes
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;

    // Constructor
    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    // Abstract method
    public abstract void displayRoomDetails();
}

/**
 * SingleRoom class extending Room
 */
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 2000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type      : " + roomType);
        System.out.println("Beds           : " + numberOfBeds);
        System.out.println("Price/Night    : ₹" + pricePerNight);
    }
}

/**
 * DoubleRoom class extending Room
 */
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 3500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type      : " + roomType);
        System.out.println("Beds           : " + numberOfBeds);
        System.out.println("Price/Night    : ₹" + pricePerNight);
    }
}

/**
 * SuiteRoom class extending Room
 */
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 6000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type      : " + roomType);
        System.out.println("Beds           : " + numberOfBeds);
        System.out.println("Price/Night    : ₹" + pricePerNight);
    }
}

/**
 * Main Application Class
 */
public class uc2 {

    public static void main(String[] args) {

        // Welcome Message
        System.out.println("======================================");
        System.out.println("   Hotel Booking System (v2.1)       ");
        System.out.println("======================================");

        // Creating Room Objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static Availability Variables
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        // Display Room Details + Availability
        System.out.println("\n--- Room Details ---\n");

        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + singleRoomAvailability);
        System.out.println("--------------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleRoomAvailability);
        System.out.println("--------------------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteRoomAvailability);
        System.out.println("--------------------------------------");

        System.out.println("\nApplication executed successfully!");
    }
}