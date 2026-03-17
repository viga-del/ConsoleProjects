import java.util.*;

public class SavingsAccount extends Account implements ATMOperations {

    List<Transaction> transactions = new ArrayList<>();

    public SavingsAccount(int accNo, String name, double balance, int pin) {
        super(accNo, name, balance, pin);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance!");
        } else {
            balance -= amount;
            transactions.add(new Transaction("Withdraw", amount));
            System.out.println("Withdrawn: " + amount);
        }
    }

    @Override
    public void transfer(Account receiver, double amount) {
        if (amount > balance) {
            System.out.println("Transfer failed!");
        } else {
            balance -= amount;
            receiver.deposit(amount);
            transactions.add(new Transaction("Transfer", amount));
            System.out.println("Transferred successfully!");
        }
    }

    @Override
    public void miniStatement() {
        System.out.println("\n--- Mini Statement ---");
        for (Transaction t : transactions) {
            t.show();
        }
    }

    @Override
    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Change PIN");
            System.out.println("5. Transfer");
            System.out.println("6. Mini Statement");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    double d = sc.nextDouble();
                    deposit(d);
                    transactions.add(new Transaction("Deposit", d));
                    break;

                case 2:
                    System.out.print("Enter amount: ");
                    withdraw(sc.nextDouble());
                    break;

                case 3:
                    checkBalance();
                    break;

                case 4:
                    System.out.print("Enter old PIN: ");
                    int oldPin = sc.nextInt();
                    System.out.print("Enter new PIN: ");
                    int newPin = sc.nextInt();
                    changePin(oldPin, newPin);
                    break;

                case 5:
                    System.out.print("Enter receiver account number: ");
                    int rAcc = sc.nextInt();
                    System.out.print("Enter amount: ");
                    double amt = sc.nextDouble();

                    ATM atm = ATMSimulation.getATM();
                    Account receiver = atm.findAccount(rAcc);

                    if (receiver != null) {
                        transfer(receiver, amt);
                    } else {
                        System.out.println("Receiver not found!");
                    }
                    break;

                case 6:
                    miniStatement();
                    break;

                case 7:
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 7);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}