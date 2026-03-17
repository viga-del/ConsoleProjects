public abstract class Account {
    protected int accountNumber;
    protected String name;
    protected double balance;
    private int pin;

    static int totalAccounts = 0;

    public Account(int accNo, String name, double balance, int pin) {
        this.accountNumber = accNo;
        this.name = name;
        this.balance = balance;
        this.pin = pin;
        totalAccounts++;
    }

    public boolean authenticate(int enteredPin) {
        return this.pin == enteredPin;
    }

    public void changePin(int oldPin, int newPin) {
        if (this.pin == oldPin) {
            this.pin = newPin;
            System.out.println("PIN changed successfully!");
        } else {
            System.out.println("Wrong old PIN!");
        }
    }

    public abstract void displayMenu();

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void checkBalance() {
        System.out.println("Balance: " + balance);
    }

    public int getAccountNumber() {
        return accountNumber;
    }
}