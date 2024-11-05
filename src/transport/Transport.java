// transport/Transport.java
package transport;

public abstract class Transport {
    protected double speed;  // Средняя скорость в км/ч
    protected boolean available; // Доступность транспорта

    public Transport(double speed, boolean available) {
        this.speed = speed;
        this.available = available;
    }

    public abstract double calculateDeliveryTime(double distance); // время доставки в часах

    public boolean isAvailable() {
        return available;
    }

    public double getSpeed() {
        return speed;
    }
}

