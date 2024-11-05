// transport/Bus.java
package transport;

public class Bus extends Transport {

    public Bus() {
        super(25.0, true); // Средняя скорость автобуса
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        return distance / speed;
    }
}
