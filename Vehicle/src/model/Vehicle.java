package model;

public abstract class Vehicle {
    private int id;
    private String name;
    private String numberPlate;
    private int availableCount;
    private double rentalPricePerDay;
    private double totalKms;
    private int serviceIntervalKms;
    private double kmsSinceLastService;

    public enum VehicleType { CAR, BIKE }

    public Vehicle(int id, String name, String numberPlate, int availableCount,
                   double rentalPricePerDay, int serviceIntervalKms) {
        this.id = id;
        this.name = name;
        this.numberPlate = numberPlate;
        this.availableCount = availableCount;
        this.rentalPricePerDay = rentalPricePerDay;
        this.serviceIntervalKms = serviceIntervalKms;
        this.totalKms = 0;
        this.kmsSinceLastService = 0;
    }

    public abstract VehicleType getType();
    public abstract double getMinSecurityDeposit();

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumberPlate() { return numberPlate; }
    public void setNumberPlate(String numberPlate) { this.numberPlate = numberPlate; }
    public int getAvailableCount() { return availableCount; }
    public void setAvailableCount(int count) { this.availableCount = count; }
    public double getRentalPricePerDay() { return rentalPricePerDay; }
    public void setRentalPricePerDay(double price) { this.rentalPricePerDay = price; }
    public double getTotalKms() { return totalKms; }
    public void addKms(double kms) { this.totalKms += kms; this.kmsSinceLastService += kms; }
    public double getKmsSinceLastService() { return kmsSinceLastService; }
    public int getServiceIntervalKms() { return serviceIntervalKms; }
    public boolean needsService() { return kmsSinceLastService >= serviceIntervalKms; }
    public void markServiced() { this.kmsSinceLastService = 0; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | Type: %s | Available: %d | Price/Day: %.2f | KMS: %.1f | Service Due: %s",
                id, name, numberPlate, getType(), availableCount, rentalPricePerDay, totalKms, needsService() ? "YES" : "No");
    }
}
