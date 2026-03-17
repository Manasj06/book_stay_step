/**
 * UseCase3InventorySetup
 *
 * This class demonstrates centralized inventory management using HashMap.
 * It replaces scattered availability variables with a single source of truth.
 *
 * @author Manas
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory class handles all availability logic
 */
class RoomInventory {

    // Centralized storage for room availability
    private Map<String, Integer> inventory;

    /**
     * Constructor - initializes inventory with default values
     */
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    /**
     * Get availability for a specific room type
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Update availability (increase/decrease)
     */
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + change;

        if (updated < 0) {
            System.out.println("Cannot reduce below zero for " + roomType);
        } else {
            inventory.put(roomType, updated);
        }
    }

    /**
     * Display full inventory
     */
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

/**
 * Main Application Class
 */
public class uc3 {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" Hotel Booking System (v3.1) ");
        System.out.println("======================================");

        // Initialize Inventory (Single Source of Truth)
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Simulate updates
        System.out.println("\nUpdating Inventory...");
        inventory.updateAvailability("Single Room", -1); // booking
        inventory.updateAvailability("Suite Room", +1);  // cancellation

        // Display updated inventory
        inventory.displayInventory();

        // Fetch specific availability
        System.out.println("\nAvailable Double Rooms: "
                + inventory.getAvailability("Double Room"));

        System.out.println("\nApplication executed successfully!");
    }
} 
