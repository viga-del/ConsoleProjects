import java.util.*;
import java.util.stream.Collectors;

public class ReportService {
    private final DataStore ds = DataStore.getInstance();

    public void vehiclesDueForService() {
        System.out.println("=== Vehicles Due for Service ===");
        ds.vehicles.stream()
                .filter(Vehicle::needsService)
                .forEach(v -> System.out.printf("%s | KMS since service: %.1f / %d%n",
                        v, v.getKmsSinceLastService(), v.getServiceIntervalKms()));
    }

    public void vehiclesSortedByRentalPrice() {
        System.out.println("=== Vehicles Sorted by Rental Price ===");
        ds.vehicles.stream()
                .sorted(Comparator.comparingDouble(Vehicle::getRentalPricePerDay))
                .forEach(System.out::println);
    }

    public void searchAndFilterVehicles(Scanner sc) {
        System.out.print("Search by name (leave blank to skip): ");
        String name = sc.nextLine().trim();
        System.out.print("Filter by type (CAR/BIKE/ALL): ");
        String typeStr = sc.nextLine().trim().toUpperCase();

        ds.vehicles.stream()
                .filter(v -> name.isEmpty() || v.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(v -> typeStr.equals("ALL") || v.getType().name().equals(typeStr))
                .forEach(System.out::println);
    }

    public void rentedAndNeverRentedVehicles() {
        Set<Integer> rentedIds = ds.rentals.stream()
                .map(r -> r.getVehicle().getId())
                .collect(Collectors.toSet());

        System.out.println("=== Currently Rented Vehicles ===");
        ds.rentals.stream()
                .filter(r -> r.getStatus() == Rental.RentalStatus.ACTIVE || r.getStatus() == Rental.RentalStatus.EXTENDED)
                .forEach(r -> System.out.println(r.getVehicle().getName() + " | Rented by: " + r.getBorrowerEmail()));

        System.out.println("\n=== Never Rented Vehicles ===");
        ds.vehicles.stream()
                .filter(v -> !rentedIds.contains(v.getId()))
                .forEach(System.out::println);
    }

    public void borrowerRentalHistory(User user) {
        System.out.println("=== Your Rental History ===");
        List<Rental> history = ds.getRentalsByUser(user.getEmail());
        if (history.isEmpty()) { System.out.println("No rentals found."); return; }
        history.forEach(System.out::println);
    }
}
