import java.util.*;

public class Store {

    static List<Product> products = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static Map<String, List<Bill>> purchaseHistory = new HashMap<>();

    static {
        users.add(new User("admin@mail.com","admin","admin"));
        users.add(new User("cust@mail.com","1234","customer"));

        products.add(new Product(1,"Rice",50,20));
        products.add(new Product(2,"Milk",30,30));
        products.add(new Product(3,"Bread",40,15));
    }

    public static User login(Scanner sc){

        System.out.print("Email: ");
        String email=sc.next();

        System.out.print("Password: ");
        String pass=sc.next();

        for(User u:users)
            if(u.email.equals(email)&&u.password.equals(pass))
                return u;

        return null;
    }

    public static void viewProducts(){

        for(Product p:products)
            p.display();
    }

    public static void sortByName(){

        products.stream()
        .sorted(Comparator.comparing(p->p.name))
        .forEach(Product::display);
    }

    public static void sortByPrice(){

        products.stream()
        .sorted(Comparator.comparingDouble(p->p.price))
        .forEach(Product::display);
    }

    public static Product searchProduct(String name){

        for(Product p:products)
            if(p.name.equalsIgnoreCase(name))
                return p;

        return null;
    }

    public static Product searchById(int id){

        for(Product p:products)
            if(p.id==id)
                return p;

        return null;
    }

    public static void addProduct(Scanner sc){

        System.out.print("ID: ");
        int id=sc.nextInt();

        System.out.print("Name: ");
        String name=sc.next();

        System.out.print("Price: ");
        double price=sc.nextDouble();

        System.out.print("Qty: ");
        int qty=sc.nextInt();

        products.add(new Product(id,name,price,qty));
    }

    public static void modifyProduct(Scanner sc){

        System.out.print("Enter product id: ");
        int id=sc.nextInt();

        Product p=searchById(id);

        if(p==null){
            System.out.println("Not found");
            return;
        }

        System.out.print("New price: ");
        p.price=sc.nextDouble();

        System.out.print("New qty: ");
        p.quantity=sc.nextInt();
    }

    public static void deleteProduct(Scanner sc){

        System.out.print("Product ID: ");
        int id=sc.nextInt();

        products.removeIf(p->p.id==id);
    }

    public static void addUser(Scanner sc){

        System.out.print("Email: ");
        String email=sc.next();

        System.out.print("Password: ");
        String pass=sc.next();

        System.out.print("Role(admin/customer): ");
        String role=sc.next();

        users.add(new User(email,pass,role));
    }

    public static void lowStock(){

        System.out.println("Low stock:");

        for(Product p:products)
            if(p.quantity<5)
                p.display();
    }

    public static void neverPurchased(){

        System.out.println("Never purchased products:");

        for(Product p:products)
            if(p.soldCount==0)
                p.display();
    }

    public static void topCustomers(){

        users.stream()
        .filter(u->u.role.equals("customer"))
        .sorted((a,b)->Double.compare(b.totalSpent,a.totalSpent))
        .limit(3)
        .forEach(u->System.out.println(u.email+" spent "+u.totalSpent));
    }
}