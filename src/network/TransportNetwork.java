// network/TransportNetwork.java
package network;

import transport.*;

public class TransportNetwork {
    private Transport[] transports;

    public TransportNetwork() {
        transports = new Transport[] {
            new Pedestrian(),
            new Bicycle(),
            new Car(),
            new Bus(),
            new Scooter()
        };
    }

    public Transport getFastestTransport(double distance) {
        Transport bestTransport = null;
        double minTime = Double.MAX_VALUE;

        for (Transport transport : transports) {
            if (transport.isAvailable()) {
                double time = transport.calculateDeliveryTime(distance);
                if (time < minTime) {
                    minTime = time;
                    bestTransport = transport;
                }
            }
        }
        return bestTransport;
    }
}
