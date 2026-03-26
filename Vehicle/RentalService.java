import java.time.LocalDate;
import java.util.*;

public class RentalService {
    private final DataStore ds = DataStore.getInstance();
    private final Map<String, Cart> carts = new HashMap<>();

    private Cart getCart(String email) {
        return carts.computeIfAbsent(email, k -> new Cart());
    }

    public void showCatalogue(User user) {
        System.out.println("=== Available Vehicles ===");
        ds.vehicles.stream()
                .filter(v -> !v.needsService() && v.getAvailableCount() > 0)
                .forEach(System.out::println);
    }

    public void addToCart(User user, Scanner sc) {
        showCatalogue(user);
        System.out.print("Enter Vehicle ID to add to cart: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Optional<Vehicle> opt = ds.findVehicleById(id);
        if (opt.isEmpty()) { System.out.println("Vehicle not found."); return; }
        Vehicle v = opt.get();
        if (v.needsService()) { System.out.println("Vehicle is due for service and cannot be rented."); return; }
        if (v.getAvailableCount() <= 0) { System.out.println("No units available."); return; }

        if (ds.getActiveRentalByUserAndType(user.getEmail(), v.getType()).isPresent()) {
            System.out.println("You already have an active rental for a " + v.getType() + "."); return;
        }

        Cart cart = getCart(user.getEmail());
        if (cart.addVehicle(v)) System.out.println("Added to cart: " + v.getName());
    }

    public void removeFromCart(User user, Scanner sc) {
        Cart cart = getCart(user.getEmail());
        cart.display();
        System.out.print("Enter Vehicle ID to remove: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        ds.findVehicleById(id).ifPresentOrElse(
                v -> { if (cart.removeVehicle(v)) System.out.println("Removed from cart."); },
                () -> System.out.println("Vehicle not found.")
        );
    }

    public void viewCart(User user) {
        getCart(user.getEmail()).display();
    }

    public void checkout(User user, Scanner sc) {
        Cart cart = getCart(user.getEmail());
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }

        List<Vehicle> toRent = new ArrayList<>();
        if (cart.getCar() != null) toRent.add(cart.getCar());
        if (cart.getBike() != null) toRent.add(cart.getBike());

        for (Vehicle v : toRent) {
            if (user.getSecurityDeposit() < v.getMinSecurityDeposit()) {
                System.out.printf("Insufficient security deposit for %s. Required: %.2f, Available: %.2f%n",
                        v.getName(), v.getMinSecurityDeposit(), user.getSecurityDeposit());
                return;
            }
        }

        LocalDate today = LocalDate.now();
        for (Vehicle v : toRent) {
            Rental rental = new Rental(ds.nextRentalId(), user.getEmail(), v, today);
            ds.rentals.add(rental);
            v.setAvailableCount(v.getAvailableCount() - 1);
            System.out.println("Rented: " + rental);
        }
        carts.put(user.getEmail(), new Cart());
    }

    public void returnVehicle(User user, Scanner sc) {
        List<Rental> active = ds.rentals.stream()
                .filter(r -> r.getBorrowerEmail().equalsIgnoreCase(user.getEmail())
                        && r.getStatus() == Rental.RentalStatus.ACTIVE)
                .toList();

        if (active.isEmpty()) { System.out.println("No active rentals."); return; }
        active.forEach(System.out::println);

        System.out.print("Enter Rental ID: ");
        int rid = Integer.parseInt(sc.nextLine().trim());
        Optional<Rental> opt = active.stream().filter(r -> r.getRentalId() == rid).findFirst();
        if (opt.isEmpty()) { System.out.println("Rental not found."); return; }
        Rental rental = opt.get();

        System.out.println("Action: 1. Return  2. Extend  3. Exchange  4. Mark as Lost");
        System.out.print("Choice: ");
        int action = Integer.parseInt(sc.nextLine().trim());

        switch (action) {
            case 1 -> processReturn(user, rental, sc);
            case 2 -> extendRental(rental);
            case 3 -> exchangeVehicle(user, rental, sc);
            case 4 -> markLost(user, rental, sc);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void processReturn(User user, Rental rental, Scanner sc) {
        System.out.print("KMs driven: ");
        double kms = Double.parseDouble(sc.nextLine().trim());
        rental.setKmsDriven(kms);
        rental.getVehicle().addKms(kms);

        double fine = 0;
        double baseCharge = rental.getVehicle().getRentalPricePerDay();
        LocalDate today = LocalDate.now();

        if (today.isAfter(rental.getDueDate())) {
            long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(rental.getDueDate(), today);
            fine += overdueDays * baseCharge;
            System.out.printf("Overdue by %d day(s). Fine: %.2f%n", overdueDays, fine);
        }

        if (kms > 500) {
            double kmsFine = baseCharge * 0.15;
            fine += kmsFine;
            System.out.printf("Excess KMs (>500). Additional charge: %.2f%n", kmsFine);
        }

        if (rental.getVehicle().getType() == Vehicle.VehicleType.CAR) {
            System.out.print("Damage level (NONE/LOW/MEDIUM/HIGH): ");
            String dmgStr = sc.nextLine().trim().toUpperCase();
            Rental.DamageLevel dmg;
            try { dmg = Rental.DamageLevel.valueOf(dmgStr); } catch (Exception e) { dmg = Rental.DamageLevel.NONE; }
            double dmgFine = switch (dmg) {
                case LOW -> baseCharge * 0.20;
                case MEDIUM -> baseCharge * 0.50;
                case HIGH -> baseCharge * 0.75;
                default -> 0;
            };
            if (dmgFine > 0) { fine += dmgFine; System.out.printf("Damage (%s) fine: %.2f%n", dmg, dmgFine); }
        }

        rental.setTotalFine(fine);
        rental.setReturnDate(today);
        rental.setStatus(Rental.RentalStatus.RETURNED);
        rental.getVehicle().setAvailableCount(rental.getVehicle().getAvailableCount() + 1);

        if (fine > 0) {
            System.out.printf("Total Fine: %.2f%n", fine);
            System.out.print("Pay by: 1. Cash  2. Deduct from Security Deposit: ");
            int payChoice = Integer.parseInt(sc.nextLine().trim());
            if (payChoice == 2) {
                user.deductDeposit(fine);
                System.out.printf("Deducted. Remaining deposit: %.2f%n", user.getSecurityDeposit());
            } else {
                System.out.println("Please pay Rs. " + fine + " in cash.");
            }
        } else {
            System.out.println("No fine. Vehicle returned successfully.");
        }
    }

    private void extendRental(Rental rental) {
        if (rental.getExtensionCount() >= 2) { System.out.println("Maximum 2 extensions allowed."); return; }
        rental.extendDueDate();
        rental.incrementExtension();
        rental.setStatus(Rental.RentalStatus.EXTENDED);
        System.out.println("Extended. New due date: " + rental.getDueDate() + " (Extension " + rental.getExtensionCount() + "/2)");
    }

    private void exchangeVehicle(User user, Rental rental, Scanner sc) {
        showCatalogue(user);
        System.out.print("Enter new Vehicle ID (same type: " + rental.getVehicle().getType() + "): ");
        int newId = Integer.parseInt(sc.nextLine().trim());
        Optional<Vehicle> newOpt = ds.findVehicleById(newId);
        if (newOpt.isEmpty()) { System.out.println("Vehicle not found."); return; }
        Vehicle newVehicle = newOpt.get();
        if (newVehicle.getType() != rental.getVehicle().getType()) { System.out.println("Must be same type."); return; }
        if (newVehicle.needsService() || newVehicle.getAvailableCount() <= 0) { System.out.println("Vehicle not available."); return; }

        rental.getVehicle().setAvailableCount(rental.getVehicle().getAvailableCount() + 1);
        newVehicle.setAvailableCount(newVehicle.getAvailableCount() - 1);
        rental.setStatus(Rental.RentalStatus.RETURNED);

        Rental newRental = new Rental(ds.nextRentalId(), user.getEmail(), newVehicle, LocalDate.now());
        ds.rentals.add(newRental);
        System.out.println("Exchanged to: " + newVehicle.getName() + ". New Rental: " + newRental);
    }

    private void markLost(User user, Rental rental, Scanner sc) {
        rental.setStatus(Rental.RentalStatus.LOST);
        rental.setReturnDate(LocalDate.now());
        double penalty = rental.getVehicle().getRentalPricePerDay() * 30;
        rental.setTotalFine(penalty);
        System.out.printf("Vehicle marked as LOST. Penalty: %.2f%n", penalty);
        System.out.print("Pay by: 1. Cash  2. Deduct from Security Deposit: ");
        int ch = Integer.parseInt(sc.nextLine().trim());
        if (ch == 2) {
            user.deductDeposit(penalty);
            System.out.printf("Deducted. Remaining deposit: %.2f%n", user.getSecurityDeposit());
        } else {
            System.out.println("Please pay Rs. " + penalty + " in cash.");
        }
    }
}
