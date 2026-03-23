import java.util.*;

public class Bill {

    static int counter = 1;

    int billNo;
    Date date;
    List<CartItem> items;
    double total;

    public Bill(List<CartItem> items, double total) {
        this.billNo = counter++;
        this.items = new ArrayList<>(items);
        this.total = total;
        this.date = new Date();
    }

    public void printBill() {

        System.out.println("\nBill No: " + billNo);
        System.out.println("Date: " + date);

        for (CartItem c : items) {
            System.out.println(c.product.name + " x " + c.quantity + " = " + c.getTotal());
        }

        System.out.println("Total: " + total);
    }
}