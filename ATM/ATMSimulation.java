import java.util.*;

public class ATMSimulation {

    private static ATM atm = new ATM();

    public static ATM getATM() {
        return atm;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== ATM SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. User Login");
            System.out.println("3. Admin");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    atm.createAccount();
                    break;
                case 2:
                    atm.userLogin();
                    break;
                case 3:
                    atm.adminMenu();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }
}