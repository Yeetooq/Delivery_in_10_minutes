// product/QualityControl.java
package product;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class QualityControl {

    public static int assessQuality(Product product, double currentTemperature) {
        long daysSinceManufacture = ChronoUnit.DAYS.between(product.getManufactureDate(), LocalDateTime.now());
        int quality = product.getQuality() - (int) (daysSinceManufacture * product.getTemperatureSensitivity() * (currentTemperature - 20));

        return Math.max(quality, 0); // Качество не может быть ниже 0
    }
}
