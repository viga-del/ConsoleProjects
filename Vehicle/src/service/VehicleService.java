package service;

import model.*;
import util.DataStore;
import java.util.*;

public class VehicleService {
    private final DataStore ds = DataStore.getInstance();

    public void addVehicle(Scanner sc) {
        System.out.print("Type (CAR/BIKE): ");
        String type = sc.nextLine().trim().toUpperCase();
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Number Plate: ");
        String plate = sc.nextLine().trim();
        System.out.print("Available Count: ");
        int count = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Rental Price/Day: ");
        double price = Double.parseDouble(sc.nextLine().trim());

        Vehicle v;
        if (type.equals("CAR")) v = new Car(ds.nextVehicleId(), name, plate, count, price);
        else if (type.equals("BIKE")) v = new Bike(ds.nextVehicleId(), name, plate, count, price);
        else { System.out.println("Invalid type."); return; }

        ds.vehicles.add(v);
        System.out.println("Vehicle added: " + v);
    }

    public void modifyVehicle(Scanner sc) {
        System.out.print("Enter Vehicle ID to modify: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Optional<Vehicle> opt = ds.findVehicleById(id);
        if (opt.isEmpty()) { System.out.println("Vehicle not found."); return; }
        Vehicle v = opt.get();

        System.out.println("1. Name  2. Number Plate  3. Available Count  4. Rental Price  5. Mark Serviced");
        System.out.print("Choice: ");
        int ch = Integer.parseInt(sc.nextLine().trim());
        switch (ch) {
            case 1 -> { System.out.print("New Name: "); v.setName(sc.nextLine().trim()); }
            case 2 -> { System.out.print("New Plate: "); v.setNumberPlate(sc.nextLine().trim()); }
            case 3 -> { System.out.print("New Count: "); v.setAvailableCount(Integer.parseInt(sc.nextLine().trim())); }
            case 4 -> { System.out.print("New Price: "); v.setRentalPricePerDay(Double.parseDouble(sc.nextLine().trim())); }
            case 5 -> { v.markServiced(); System.out.println("Marked as serviced."); }
            default -> System.out.println("Invalid choice.");
        }
        System.out.println("Updated: " + v);
    }

    public void deleteVehicle(Scanner sc) {
        System.out.print("Enter Vehicle ID to delete: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        boolean removed = ds.vehicles.removeIf(v -> v.getId() == id);
        System.out.println(removed ? "Vehicle deleted." : "Vehicle not found.");
    }

    public void listVehicles(Scanner sc) {
        System.out.println("Sort by: 1. Name  2. Available Count");
        System.out.print("Choice: ");
        int ch = Integer.parseInt(sc.nextLine().trim());
        List<Vehicle> sorted = new ArrayList<>(ds.vehicles);
        if (ch == 1) sorted.sort(Comparator.comparing(Vehicle::getName));
        else sorted.sort(Comparator.comparingInt(Vehicle::getAvailableCount).reversed());
        sorted.forEach(System.out::println);
    }

    public void searchVehicle(Scanner sc) {
        System.out.println("Search by: 1. Name  2. Number Plate");
        System.out.print("Choice: ");
        int ch = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Enter search term: ");
        String term = sc.nextLine().trim();
        Optional<Vehicle> result = (ch == 1) ? ds.findVehicleByName(term) : ds.findVehicleByPlate(term);
        result.ifPresentOrElse(System.out::println, () -> System.out.println("Not found."));
    }

    public void changeSecurityDeposit(Scanner sc) {
        System.out.print("Borrower Email: ");
        String email = sc.nextLine().trim();
        ds.findUser(email).ifPresentOrElse(u -> {
            if (u.getRole() != model.User.Role.BORROWER) { System.out.println("Not a borrower."); return; }
            System.out.print("New Security Deposit Amount: ");
            double amt = Double.parseDouble(sc.nextLine().trim());
            u.setSecurityDeposit(amt);
            System.out.println("Security deposit updated to: " + amt);
        }, () -> System.out.println("User not found."));
    }
}
