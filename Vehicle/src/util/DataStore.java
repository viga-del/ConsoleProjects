package util;

import model.*;
import model.User.Role;
import java.util.*;

public class DataStore {
    private static DataStore instance;

    public final List<User> users = new ArrayList<>();
    public final List<Vehicle> vehicles = new ArrayList<>();
    public final List<Rental> rentals = new ArrayList<>();
    private int vehicleIdCounter = 1;
    private int rentalIdCounter = 1;

    private DataStore() { seed(); }

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    public int nextVehicleId() { return vehicleIdCounter++; }
    public int nextRentalId() { return rentalIdCounter++; }

    private void seed() {
        users.add(new User("admin@rental.com", "admin123", "Admin", Role.ADMIN));
        users.add(new User("alice@mail.com", "pass123", "Alice", Role.BORROWER));
        users.add(new User("bob@mail.com", "pass456", "Bob", Role.BORROWER));

        Car c1 = new Car(nextVehicleId(), "Toyota Camry", "TN01AB1234", 3, 1500.0);
        Car c2 = new Car(nextVehicleId(), "Honda City", "TN02CD5678", 2, 1200.0);
        Bike b1 = new Bike(nextVehicleId(), "Royal Enfield", "TN03EF9012", 4, 500.0);
        Bike b2 = new Bike(nextVehicleId(), "Honda Activa", "TN04GH3456", 5, 300.0);

        // Simulate some kms for service-due demo
        c1.addKms(2800);
        b1.addKms(1400);

        vehicles.add(c1);
        vehicles.add(c2);
        vehicles.add(b1);
        vehicles.add(b2);
    }

    public Optional<User> findUser(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public Optional<Vehicle> findVehicleById(int id) {
        return vehicles.stream().filter(v -> v.getId() == id).findFirst();
    }

    public Optional<Vehicle> findVehicleByName(String name) {
        return vehicles.stream().filter(v -> v.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Vehicle> findVehicleByPlate(String plate) {
        return vehicles.stream().filter(v -> v.getNumberPlate().equalsIgnoreCase(plate)).findFirst();
    }

    public List<Rental> getRentalsByUser(String email) {
        List<Rental> result = new ArrayList<>();
        for (Rental r : rentals) if (r.getBorrowerEmail().equalsIgnoreCase(email)) result.add(r);
        return result;
    }

    public Optional<Rental> getActiveRentalByUserAndType(String email, Vehicle.VehicleType type) {
        return rentals.stream()
                .filter(r -> r.getBorrowerEmail().equalsIgnoreCase(email)
                        && r.getStatus() == Rental.RentalStatus.ACTIVE
                        && r.getVehicle().getType() == type)
                .findFirst();
    }
}
