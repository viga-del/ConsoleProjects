import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static AuthService authService = new AuthService();
    static VehicleService vehicleService = new VehicleService();
    static RentalService rentalService = new RentalService();
    static ReportService reportService = new ReportService();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Welcome to Vehicle Rental System     ");
        System.out.println("========================================");

        while (true) {
            System.out.println("\n1. Login  2. Signup  3. Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    User user = authService.login(sc);
                    if (user != null) {
                        if (user.getRole() == User.Role.ADMIN) adminMenu(user);
                        else borrowerMenu(user);
                    }
                }
                case "2" -> authService.signup(sc);
                case "3" -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void adminMenu(User admin) {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Modify Vehicle");
            System.out.println("3. Delete Vehicle");
            System.out.println("4. List All Vehicles");
            System.out.println("5. Search Vehicle");
            System.out.println("6. Change Borrower Security Deposit");
            System.out.println("7. Reports");
            System.out.println("8. Logout");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1" -> vehicleService.addVehicle(sc);
                case "2" -> vehicleService.modifyVehicle(sc);
                case "3" -> vehicleService.deleteVehicle(sc);
                case "4" -> vehicleService.listVehicles(sc);
                case "5" -> vehicleService.searchVehicle(sc);
                case "6" -> vehicleService.changeSecurityDeposit(sc);
                case "7" -> adminReportMenu();
                case "8" -> { System.out.println("Logged out."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void adminReportMenu() {
        System.out.println("\n=== Reports ===");
        System.out.println("1. Vehicles Due for Service");
        System.out.println("2. Vehicles Sorted by Rental Price");
        System.out.println("3. Search & Filter Vehicles");
        System.out.println("4. Rented / Never Rented Vehicles");
        System.out.print("Choice: ");
        String ch = sc.nextLine().trim();
        switch (ch) {
            case "1" -> reportService.vehiclesDueForService();
            case "2" -> reportService.vehiclesSortedByRentalPrice();
            case "3" -> reportService.searchAndFilterVehicles(sc);
            case "4" -> reportService.rentedAndNeverRentedVehicles();
            default -> System.out.println("Invalid choice.");
        }
    }

    static void borrowerMenu(User user) {
        while (true) {
            System.out.println("\n=== Borrower Menu ===");
            System.out.printf("Security Deposit: %.2f%n", user.getSecurityDeposit());
            System.out.println("1. View Catalogue");
            System.out.println("2. Add Vehicle to Cart");
            System.out.println("3. Remove Vehicle from Cart");
            System.out.println("4. View Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Return / Extend / Exchange / Lost");
            System.out.println("7. Rental History");
            System.out.println("8. Logout");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1" -> rentalService.showCatalogue(user);
                case "2" -> rentalService.addToCart(user, sc);
                case "3" -> rentalService.removeFromCart(user, sc);
                case "4" -> rentalService.viewCart(user);
                case "5" -> rentalService.checkout(user, sc);
                case "6" -> rentalService.returnVehicle(user, sc);
                case "7" -> reportService.borrowerRentalHistory(user);
                case "8" -> { System.out.println("Logged out."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
