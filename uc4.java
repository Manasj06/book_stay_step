import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

// Inventory (State Holder)
class RoomInventory {
    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        availabilityMap.put(type, count);
    }

    // Read-only access method
    public int getAvailability(String type) {
        return availabilityMap.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availabilityMap); // Defensive programming
    }
}

// Search Service (Read-Only)
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomCatalog) {
        System.out.println("\nAvailable Rooms:\n");

        Map<String, Integer> availability = inventory.getAllAvailability();

        for (String type : availability.keySet()) {
            int count = availability.get(type);

            // Validation Logic: Only show rooms with availability > 0
            if (count > 0 && roomCatalog.containsKey(type)) {
                Room room = roomCatalog.get(type);

                room.displayDetails();
                System.out.println("Available Count: " + count);
                System.out.println("===========================");
            }
        }
    }
}

// Guest (Actor)
class Guest {
    public void searchRooms(SearchService service, RoomInventory inventory, Map<String, Room> roomCatalog) {
        service.searchAvailableRooms(inventory, roomCatalog);
    }
}

// Main Class
public class UseCase4RoomSearch {
    public static void main(String[] args) {

        // Step 1: Create Room Catalog (Domain Objects)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room("Single", 2000,
                Arrays.asList("WiFi", "TV")));

        roomCatalog.put("Double", new Room("Double", 3500,
                Arrays.asList("WiFi", "TV", "AC")));

        roomCatalog.put("Suite", new Room("Suite", 6000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Step 2: Initialize Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 0); // Should NOT be shown
        inventory.addRoomType("Suite", 2);

        // Step 3: Guest initiates search
        Guest guest = new Guest();
        SearchService searchService = new SearchService();

        guest.searchRooms(searchService, inventory, roomCatalog);
    }
}