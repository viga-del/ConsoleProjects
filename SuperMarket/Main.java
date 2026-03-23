import java.util.*;

public class Main {

    static Scanner sc=new Scanner(System.in);

    public static void main(String[] args){

        while(true){

            System.out.println("\n1.Login");
            System.out.println("2.Exit");

            int ch=sc.nextInt();

            if(ch==1){

                User user=Store.login(sc);

                if(user==null){
                    System.out.println("Invalid login");
                    continue;
                }

                if(user.role.equals("admin"))
                    adminMenu();
                else
                    customerMenu(user);
            }
            else break;
        }
    }

    static void adminMenu(){

        while(true){

            System.out.println("\nADMIN MENU");
            System.out.println("1.View Products");
            System.out.println("2.Sort by Name");
            System.out.println("3.Sort by Price");
            System.out.println("4.Search Product");
            System.out.println("5.Add Product");
            System.out.println("6.Modify Product");
            System.out.println("7.Delete Product");
            System.out.println("8.Add User");
            System.out.println("9.Reports");
            System.out.println("10.Logout");

            int ch=sc.nextInt();

            switch(ch){

                case 1: Store.viewProducts(); break;
                case 2: Store.sortByName(); break;
                case 3: Store.sortByPrice(); break;

                case 4:
                    System.out.print("Name: ");
                    String name=sc.next();
                    Product p=Store.searchProduct(name);
                    if(p!=null) p.display();
                    else System.out.println("Not found");
                    break;

                case 5: Store.addProduct(sc); break;
                case 6: Store.modifyProduct(sc); break;
                case 7: Store.deleteProduct(sc); break;
                case 8: Store.addUser(sc); break;

                case 9: reportsMenu(); break;
                case 10: return;
            }
        }
    }

    static void reportsMenu(){

        while(true){

            System.out.println("\nREPORTS");
            System.out.println("1.Low Stock");
            System.out.println("2.Products never purchased");
            System.out.println("3.Top Customers");
            System.out.println("4.Back");

            int ch=sc.nextInt();

            switch(ch){

                case 1: Store.lowStock(); break;
                case 2: Store.neverPurchased(); break;
                case 3: Store.topCustomers(); break;
                case 4: return;
            }
        }
    }

    static void customerMenu(User user){

        List<CartItem> cart=new ArrayList<>();

        while(true){

            System.out.println("\nCUSTOMER MENU");
            System.out.println("1.View Products");
            System.out.println("2.Add to Cart");
            System.out.println("3.Edit Cart");
            System.out.println("4.Remove Item");
            System.out.println("5.Checkout");
            System.out.println("6.View History");
            System.out.println("7.Logout");

            int ch=sc.nextInt();

            switch(ch){

                case 1: Store.viewProducts(); break;

                case 2:

                    System.out.print("Product ID: ");
                    int id=sc.nextInt();

                    Product p=Store.searchById(id);

                    if(p==null){
                        System.out.println("Not found");
                        break;
                    }

                    System.out.print("Qty: ");
                    int qty=sc.nextInt();

                    cart.add(new CartItem(p,qty));
                    break;

                case 3:

                    System.out.print("Product ID to edit: ");
                    int eid=sc.nextInt();

                    for(CartItem c:cart)
                        if(c.product.id==eid){
                            System.out.print("New qty:");
                            c.quantity=sc.nextInt();
                        }
                    break;

                case 4:

                    System.out.print("Product ID remove: ");
                    int rid=sc.nextInt();

                    cart.removeIf(c->c.product.id==rid);
                    break;

                case 5:

                    double total=0;

                    for(CartItem c:cart)
                        total+=c.getTotal();

                    if(total>user.credit){
                        System.out.println("Credit exceeded");
                        break;
                    }

                    user.credit-=total;
                    user.totalSpent+=total;

                    if(total>=5000){
                        user.credit+=100;
                        System.out.println("100 cashback added");
                    }
                    else{

                        user.points+=total/100;

                        if(user.points>=50){
                            user.credit+=100;
                            user.points-=50;
                            System.out.println("Loyalty reward 100");
                        }
                    }

                    for(CartItem c:cart){
                        c.product.quantity-=c.quantity;
                        c.product.soldCount+=c.quantity;
                    }

                    Bill bill=new Bill(cart,total);
                    bill.printBill();

                    Store.purchaseHistory
                    .computeIfAbsent(user.email,k->new ArrayList<>())
                    .add(bill);

                    cart=new ArrayList<>();
                    break;

                case 6:

                    List<Bill> history=Store.purchaseHistory.get(user.email);

                    if(history!=null)
                        history.forEach(Bill::printBill);

                    break;

                case 7: return;
            }
        }
    }
}