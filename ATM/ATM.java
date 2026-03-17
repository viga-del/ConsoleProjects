import java.util.*;

public class ATM {
    private List<Account> accounts = new ArrayList<>();

    public void createAccount() {
        Scanner sc = new Scanner(System.in);

        int accNo = 1000 + accounts.size() + 1;
        System.out.println("Your Account Number: " + accNo);

        System.out.print("Enter Name: ");
        String name = sc.next();

        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        System.out.print("Set PIN: ");
        int pin = sc.nextInt();

        accounts.add(new SavingsAccount(accNo, name, balance, pin));
        System.out.println("Account created successfully!");
    }

    public Account findAccount(int accNo) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accNo) {
                return acc;
            }
        }
        return null;
    }

    public void userLogin() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        Account acc = findAccount(accNo);

        if (acc != null) {
            System.out.print("Enter PIN: ");
            int pin = sc.nextInt();

            if (acc.authenticate(pin)) {
                acc.displayMenu();
            } else {
                System.out.println("Wrong PIN!");
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    public void adminMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. Deposit");
        System.out.println("2. Check Balance");
        System.out.println("3. Transaction History");

        int choice = sc.nextInt();

        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        Account acc = findAccount(accNo);

        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }

        switch (choice) {
            case 1:
                System.out.print("Enter amount: ");
                acc.deposit(sc.nextDouble());
                break;

            case 2:
                acc.checkBalance();
                break;

            case 3:
                if (acc instanceof SavingsAccount) {
                    SavingsAccount sa = (SavingsAccount) acc;
                    for (Transaction t : sa.getTransactions()) {
                        t.show();
                    }
                }
                break;

            default:
                System.out.println("Invalid choice!");
        }
    }
}