// transport/Car.java
package transport;

public class Car extends Transport {

    public Car() {
        super(40.0, true); // Средняя скорость автомобиля
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        return distance / speed;
    }
}
