// order/Delivery.java
package order;

import network.TransportNetwork;
import transport.Transport;

public class Delivery {
    private Order order;
    private TransportNetwork transportNetwork;
    private double distance; // Расстояние до адреса доставки

    public Delivery(Order order, TransportNetwork transportNetwork, double distance) {
        this.order = order;
        this.transportNetwork = transportNetwork;
        this.distance = distance;
    }

    public void startDelivery() {
        Transport bestTransport = transportNetwork.getFastestTransport(distance);
        if (bestTransport != null) {
            double deliveryTime = bestTransport.calculateDeliveryTime(distance);
            System.out.println("Доставка будет выполнена транспортом: " + bestTransport.getClass().getSimpleName());
            System.out.println("Время доставки: " + deliveryTime + " часов.");
        } else {
            System.out.println("Доставка невозможна: нет доступного транспорта.");
        }
    }
}
