import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Borrower extends User {

    List<Transaction> transactions = new ArrayList<>();
    List<String> fineHistory = new ArrayList<>();
    double deposit = 1500;

    public Borrower(String email, String password) {
        super(email, password);
    }

    @Override
    public void menu(Library lib) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- BORROWER MENU ---");
            System.out.println("1.View Books\n2.Borrow Book\n3.Return Book");
            System.out.println("4.Extend\n5.Exchange\n6.Lost Book");
            System.out.println("7.Card Lost\n8.My Books\n9.Fine History\n10.Exit");

            int ch = sc.nextInt();

            switch (ch) {

                case 1:
                    lib.viewBooks();
                    break;

                case 2:
                    Book b = lib.searchByISBN();
                    if (b != null && b.qty > 0) {

                        if (transactions.size() >= 3) {
                            System.out.println("Max 3 books allowed!");
                            break;
                        }

                        for (Transaction t : transactions) {
                            if (t.book == b && !t.returned) {
                                System.out.println("Already borrowed!");
                                return;
                            }
                        }

                        if (deposit < 500) {
                            System.out.println("Minimum deposit required!");
                            return;
                        }

                        transactions.add(new Transaction(b));
                        b.qty--;
                        b.borrowedCount++;

                        System.out.println("Book borrowed!");
                    }
                    break;

                case 3:
                    returnBook(sc);
                    break;

                case 4:
                    extendBook();
                    break;

                case 5:
                    exchangeBook(lib);
                    break;

                case 6:
                    lostBook(sc);
                    break;

                case 7:
                    deposit -= 10;
                    fineHistory.add("Card lost fine: 10");
                    System.out.println("₹10 deducted");
                    break;

                case 8:
                    for (Transaction t : transactions)
                        System.out.println(t.book.name + " Returned: " + t.returned);
                    break;

                case 9:
                    fineHistory.forEach(System.out::println);
                    break;

                case 10:
                    return;
            }
        }
    }

    private void returnBook(Scanner sc) {
        System.out.print("Enter ISBN: ");
        String isbn = sc.next();

        for (Transaction t : transactions) {
            if (t.book.isbn.equals(isbn) && !t.returned) {

                System.out.print("Enter return date (dd/MM/yyyy): ");
                String date = sc.next();

                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                t.returnDate = LocalDate.parse(date, f);

                long days = Duration.between(
                        t.borrowDate.atStartOfDay(),
                        t.returnDate.atStartOfDay()).toDays();

                double fine = 0;

                if (days > 15) {
                    long extra = days - 15;
                    fine = extra * 2;

                    if (extra > 10)
                        fine *= 2;

                    double maxFine = t.book.cost * 0.8;
                    fine = Math.min(fine, maxFine);
                }

                deposit -= fine;
                fineHistory.add("Return fine: " + fine);

                t.returned = true;
                t.book.qty++;

                System.out.println("Returned. Fine: " + fine);
                return;
            }
        }
    }

    private void extendBook() {
        for (Transaction t : transactions) {
            if (!t.returned && t.extendCount < 2) {
                t.extendCount++;
                System.out.println("Extended!");
                return;
            }
        }
    }

    private void exchangeBook(Library lib) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Old ISBN: ");
        String oldIsbn = sc.next();

        for (Transaction t : transactions) {
            if (t.book.isbn.equals(oldIsbn) && !t.returned) {

                t.book.qty++;

                Book newBook = lib.searchByISBN();
                if (newBook != null && newBook.qty > 0) {
                    t.book = newBook;
                    newBook.qty--;
                    System.out.println("Exchanged!");
                }
                return;
            }
        }
    }

    private void lostBook(Scanner sc) {
        System.out.print("Enter ISBN: ");
        String isbn = sc.next();

        for (Transaction t : transactions) {
            if (t.book.isbn.equals(isbn) && !t.returned) {
                double fine = t.book.cost * 0.5;
                deposit -= fine;
                fineHistory.add("Lost book fine: " + fine);
                t.returned = true;
                System.out.println("Fine applied: " + fine);
                return;
            }
        }
    }
}