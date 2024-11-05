// product/Product.java
package product;

import java.time.LocalDateTime;
import java.time.Duration;

public class Product {
    private String name;
    private LocalDateTime manufactureDate;
    private int quality; // Качество товара от 0 до 100
    private Duration shelfLife; // Срок годности
    private double temperatureSensitivity; // Чувствительность к температуре

    public Product(String name, LocalDateTime manufactureDate, int quality, Duration shelfLife, double temperatureSensitivity) {
        this.name = name;
        this.manufactureDate = manufactureDate;
        this.quality = quality;
        this.shelfLife = shelfLife;
        this.temperatureSensitivity = temperatureSensitivity;
    }

    public int getQuality() {
        return quality;
    }

    public double getTemperatureSensitivity() {
        return temperatureSensitivity;
    }

    public LocalDateTime getManufactureDate() {
        return manufactureDate;
    }

    public Duration getShelfLife() {
        return shelfLife;
    }
}
