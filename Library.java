import java.util.*;

public class Library {
    List<Book> books = new ArrayList<>();
    List<Borrower> borrowers = new ArrayList<>();
    List<Admin> admins = new ArrayList<>();

    Scanner sc = new Scanner(System.in);

    public Library() {
        admins.add(new Admin("admin@gmail.com", "1234"));
    }

    public void addBook() {
        System.out.print("Name: ");
        String name = sc.next();

        System.out.print("ISBN: ");
        String isbn = sc.next();

        System.out.print("Qty: ");
        int q = sc.nextInt();

        System.out.print("Cost: ");
        double c = sc.nextDouble();

        books.add(new Book(name, isbn, q, c));
    }

    public void updateBook() {
        Book b = searchByISBN();
        if (b != null) {
            System.out.print("New Qty: ");
            b.qty = sc.nextInt();
        }
    }

    public void deleteBook() {
        Book b = searchByISBN();
        books.remove(b);
    }

    public void viewBooks() {
        books.sort(Comparator.comparing(b -> b.name));
        books.forEach(System.out::println);
    }

    public void searchBook() {
        Book b = searchByISBN();
        if (b != null) System.out.println(b);
    }

    public Book searchByISBN() {
        System.out.print("Enter ISBN: ");
        String isbn = sc.next();

        for (Book b : books) {
            if (b.isbn.equals(isbn)) return b;
        }
        return null;
    }

    public void addBorrower() {
        System.out.print("Email: ");
        String e = sc.next();

        System.out.print("Password: ");
        String p = sc.next();

        borrowers.add(new Borrower(e, p));
    }

    public void reports() {
        System.out.println("\nLow Stock:");
        books.stream().filter(b -> b.qty < 2).forEach(System.out::println);

        System.out.println("\nMost Borrowed:");
        books.stream().filter(b -> b.borrowedCount > 2).forEach(System.out::println);

        System.out.println("\nNot Borrowed:");
        books.stream().filter(b -> b.borrowedCount == 0).forEach(System.out::println);
    }

    public User authenticate() {
        System.out.print("Email: ");
        String e = sc.next();

        System.out.print("Password: ");
        String p = sc.next();

        for (Admin a : admins) if (a.login(e, p)) return a;
        for (Borrower b : borrowers) if (b.login(e, p)) return b;

        return null;
    }
}