import java.time.LocalDate;

public class Rental {
    private int rentalId;
    private String borrowerEmail;
    private Vehicle vehicle;
    private LocalDate rentDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private RentalStatus status;
    private double totalFine;
    private int extensionCount;
    private double kmsDriven;

    public enum RentalStatus { ACTIVE, RETURNED, LOST, EXTENDED }
    public enum DamageLevel { NONE, LOW, MEDIUM, HIGH }

    public Rental(int rentalId, String borrowerEmail, Vehicle vehicle, LocalDate rentDate) {
        this.rentalId = rentalId;
        this.borrowerEmail = borrowerEmail;
        this.vehicle = vehicle;
        this.rentDate = rentDate;
        this.dueDate = rentDate;
        this.status = RentalStatus.ACTIVE;
        this.totalFine = 0;
        this.extensionCount = 0;
        this.kmsDriven = 0;
    }

    public int getRentalId() { return rentalId; }
    public String getBorrowerEmail() { return borrowerEmail; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDate getRentDate() { return rentDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate date) { this.returnDate = date; }
    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }
    public double getTotalFine() { return totalFine; }
    public void setTotalFine(double fine) { this.totalFine = fine; }
    public int getExtensionCount() { return extensionCount; }
    public void incrementExtension() { this.extensionCount++; }
    public double getKmsDriven() { return kmsDriven; }
    public void setKmsDriven(double kms) { this.kmsDriven = kms; }
    public void extendDueDate() { this.dueDate = this.dueDate.plusDays(1); }

    @Override
    public String toString() {
        return String.format("Rental#%d | %s | %s | Rented: %s | Due: %s | Status: %s | Fine: %.2f",
                rentalId, vehicle.getName(), vehicle.getNumberPlate(), rentDate, dueDate, status, totalFine);
    }
}
