import java.util.*;

public class Admin extends User {

    public Admin(String email, String password) {
        super(email, password);
    }

    @Override
    public void menu(Library lib) {
        Scanner sc = new Scanner(System.in);
        int ch;

        do {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Book");
            System.out.println("2. Update Quantity");
            System.out.println("3. Delete Book");
            System.out.println("4. View Books");
            System.out.println("5. Search Book");
            System.out.println("6. Add Borrower");
            System.out.println("7. Reports");
            System.out.println("8. Exit");

            ch = sc.nextInt();

            switch (ch) {
                case 1: lib.addBook(); break;
                case 2: lib.updateBook(); break;
                case 3: lib.deleteBook(); break;
                case 4: lib.viewBooks(); break;
                case 5: lib.searchBook(); break;
                case 6: lib.addBorrower(); break;
                case 7: lib.reports(); break;
            }

        } while (ch != 8);
    }
}