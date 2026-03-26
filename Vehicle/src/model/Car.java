package model;

public class Car extends Vehicle {
    public Car(int id, String name, String numberPlate, int availableCount, double rentalPricePerDay) {
        super(id, name, numberPlate, availableCount, rentalPricePerDay, 3000);
    }

    @Override
    public VehicleType getType() { return VehicleType.CAR; }

    @Override
    public double getMinSecurityDeposit() { return 10000.0; }
}
