public class Cart {
    private Vehicle car;
    private Vehicle bike;

    public Vehicle getCar() { return car; }
    public Vehicle getBike() { return bike; }

    public boolean addVehicle(Vehicle v) {
        if (v.getType() == Vehicle.VehicleType.CAR) {
            if (car != null) { System.out.println("Cart already has a Car."); return false; }
            car = v; return true;
        } else {
            if (bike != null) { System.out.println("Cart already has a Bike."); return false; }
            bike = v; return true;
        }
    }

    public boolean removeVehicle(Vehicle v) {
        if (v.getType() == Vehicle.VehicleType.CAR && car != null && car.getId() == v.getId()) {
            car = null; return true;
        } else if (v.getType() == Vehicle.VehicleType.BIKE && bike != null && bike.getId() == v.getId()) {
            bike = null; return true;
        }
        System.out.println("Vehicle not found in cart.");
        return false;
    }

    public boolean isEmpty() { return car == null && bike == null; }

    public void display() {
        System.out.println("--- Cart ---");
        if (car != null) System.out.println("Car : " + car);
        if (bike != null) System.out.println("Bike: " + bike);
        if (isEmpty()) System.out.println("Cart is empty.");
    }
}
