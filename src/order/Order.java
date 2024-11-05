// order/Order.java
package order;

import product.Product;
import java.util.List;

public class Order {
    private List<Product> products;
    private String deliveryAddress;

    public Order(List<Product> products, String deliveryAddress) {
        this.products = products;
        this.deliveryAddress = deliveryAddress;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
}
