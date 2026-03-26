public class Bike extends Vehicle {
    public Bike(int id, String name, String numberPlate, int availableCount, double rentalPricePerDay) {
        super(id, name, numberPlate, availableCount, rentalPricePerDay, 1500);
    }

    @Override
    public VehicleType getType() { return VehicleType.BIKE; }

    @Override
    public double getMinSecurityDeposit() { return 3000.0; }
}
