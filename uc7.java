import java.util.*;

// AddOnReservation (renamed to avoid conflicts)
class AddOnReservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public AddOnReservation(String reservationId, String guestName, String roomType) {
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
}

// Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println(serviceName + " - ₹" + cost);
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // ReservationID -> List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service '" + service.getServiceName() +
                "' to Reservation ID: " + reservationId);
    }

    public void viewServices(String reservationId) {
        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            s.displayService();
        }
    }

    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

// Guest (Actor)
class AddOnGuest {
    private String name;

    public AddOnGuest(String name) {
        this.name = name;
    }

    public void selectService(String reservationId, AddOnService service, AddOnServiceManager manager) {
        manager.addService(reservationId, service);
    }
}

// Main Class
public class uc7 {
    public static void main(String[] args) {

        // Step 1: Create Reservation
        AddOnReservation reservation = new AddOnReservation("RES101", "Manas", "Suite");

        // Step 2: Create Services
        AddOnService wifi = new AddOnService("Premium WiFi", 500);
        AddOnService breakfast = new AddOnService("Breakfast", 800);
        AddOnService spa = new AddOnService("Spa Access", 1500);

        // Step 3: Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Step 4: Guest selects services
        AddOnGuest guest = new AddOnGuest("Manas");

        guest.selectService(reservation.getReservationId(), wifi, manager);
        guest.selectService(reservation.getReservationId(), breakfast, manager);
        guest.selectService(reservation.getReservationId(), spa, manager);

        // Step 5: View services
        manager.viewServices(reservation.getReservationId());

        // Step 6: Total cost
        double totalCost = manager.calculateTotalCost(reservation.getReservationId());
        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);
    }
}