// transport/Scooter.java
package transport;

public class Scooter extends Transport {

    public Scooter() {
        super(20.0, true); // Средняя скорость самоката
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        return distance / speed;
    }
}
