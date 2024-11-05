// transport/Pedestrian.java
package transport;

public class Pedestrian extends Transport {

    public Pedestrian() {
        super(5.0, true); // Средняя скорость пешехода
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        return distance / speed;
    }
}
