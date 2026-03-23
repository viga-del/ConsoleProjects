public class Product {

    int id;
    String name;
    double price;
    int quantity;
    int soldCount = 0;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void display() {
        System.out.println(id + " | " + name + " | Rs." + price + " | Qty:" + quantity);
    }
}