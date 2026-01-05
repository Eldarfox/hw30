package domain;

import java.util.List;
import java.util.Objects;

public class Order {
    private final Customer customer;
    private final List<Item> items;
    private final boolean homeDelivery;
    private transient double total = 0.0d;

    public Order(Customer customer, List<Item> orderedItems, boolean homeDelivery) {
        this.customer = customer;
        this.items = List.copyOf(orderedItems);
        this.homeDelivery = homeDelivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return homeDelivery == order.homeDelivery &&
                Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, homeDelivery);
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isHomeDelivery() {
        return homeDelivery;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotal() {
        return total;
    }

    public void calculateTotal() {
        total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getAmount())
                .sum();
    }
    @Override
    public String toString() {
        return "Order{" +
                "customer='" + customer.getFullName() + '\'' +
                ", email='" + customer.getEmail() + '\'' +
                ", items=" + items.size() +
                ", homeDelivery=" + homeDelivery +
                ", total=" + total +
                '}';
    }
}
