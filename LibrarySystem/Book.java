public class Book {
    String name, isbn;
    int qty;
    double cost;
    int borrowedCount = 0;

    public Book(String name, String isbn, int qty, double cost) {
        this.name = name;
        this.isbn = isbn;
        this.qty = qty;
        this.cost = cost;
    }

    public String toString() {
        return name + " | ISBN: " + isbn + " | Qty: " + qty;
    }
}