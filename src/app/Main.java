// app/Main.java
package app;

import consumer.Consumer;
import network.TransportNetwork;
import order.Delivery;
import order.Order;
import product.Product;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Создаем потребителя
        Consumer consumer = new Consumer("Иван", "ул. Ленина, д. 10");

        // Создаем товары для заказа
        Product milk = new Product("Молоко", LocalDateTime.now().minusDays(1), 100, Duration.ofDays(7), 0.5);
        Product bread = new Product("Хлеб", LocalDateTime.now().minusHours(6), 100, Duration.ofDays(3), 0.2);

        // Потребитель делает заказ
        Order order = consumer.makeOrder(Arrays.asList(milk, bread));

        // Инициализируем транспортную сеть
        TransportNetwork transportNetwork = new TransportNetwork();

        // Создаем объект доставки и начинаем процесс
        Delivery delivery = new Delivery(order, transportNetwork, 10.0); // расстояние до адреса доставки в км
        delivery.startDelivery();
    }
}
