public final class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public void show() {
        System.out.println(type + " : " + amount);
    }
}