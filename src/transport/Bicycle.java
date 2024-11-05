// transport/Bicycle.java
package transport;

public class Bicycle extends Transport {

    public Bicycle() {
        super(15.0, true); // Средняя скорость велосипеда
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        return distance / speed;
    }
}
