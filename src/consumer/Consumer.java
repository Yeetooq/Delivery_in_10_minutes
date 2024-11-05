// consumer/Consumer.java
package consumer;
import java.util.List;

import order.Order;
import product.Product;

public class Consumer {
    private String name;
    private String address;

    public Consumer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Order makeOrder(List<Product> products) {
        return new Order(products, address);
    }
}
