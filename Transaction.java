import java.time.LocalDate;

public class Transaction {
    Book book;
    LocalDate borrowDate;
    LocalDate returnDate;
    boolean returned = false;
    int extendCount = 0;

    public Transaction(Book book) {
        this.book = book;
        this.borrowDate = LocalDate.now();
    }
}