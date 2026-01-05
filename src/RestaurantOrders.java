import com.google.gson.Gson;
import domain.Item;
import domain.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantOrders {
    // Этот блок кода менять нельзя! НАЧАЛО!
    private List<Order> orders;

    private RestaurantOrders(String fileName) {
        var filePath = Path.of("data", fileName);
        Gson gson = new Gson();
        try {
            orders = List.of(gson.fromJson(Files.readString(filePath), Order[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RestaurantOrders read(String fileName) {
        var ro = new RestaurantOrders(fileName);
        ro.getOrders().forEach(Order::calculateTotal);
        return ro;
    }

    public List<Order> getOrders() {
        return orders;
    }
    // Этот блок кода менять нельзя! КОНЕЦ!

    public void printOrders() {
        orders.forEach(System.out::println);
    }

    public List<Order> topNByTotal(int n) {
        return orders.stream()
                .sorted(Comparator.comparingDouble(Order::getTotal).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Order> bottomNByTotal(int n) {
        return orders.stream()
                .sorted(Comparator.comparingDouble(Order::getTotal))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Order> homeDeliveryOrders() {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .collect(Collectors.toList());
    }

    public Optional<Order> maxHomeDeliveryOrder() {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .max(Comparator.comparingDouble(Order::getTotal));
    }

    public Optional<Order> minHomeDeliveryOrder() {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .min(Comparator.comparingDouble(Order::getTotal));
    }

    public List<Order> ordersBetween(double minOrderTotal, double maxOrderTotal) {
        return orders.stream()
                .filter(o -> o.getTotal() > minOrderTotal && o.getTotal() < maxOrderTotal)
                .collect(Collectors.toList());
    }

    public double totalOrdersSum() {
        return orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    public List<String> uniqueSortedEmails() {
        return orders.stream()
                .map(o -> o.getCustomer().getEmail())
                .collect(Collectors.toCollection(TreeSet::new))
                .stream()
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>> ordersByCustomerName() {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCustomer().getFullName()));
    }

    public Map<String, Double> totalByCustomer() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getCustomer().getFullName(),
                        Collectors.summingDouble(Order::getTotal)
                ));
    }

    public Optional<String> customerWithMaxTotal() {
        return totalByCustomer().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Optional<String> customerWithMinTotal() {
        return totalByCustomer().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
    public Map<Item, Integer> soldItemsAmount() {
        return orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item,
                        Collectors.summingInt(Item::getAmount)
                ));
    }
}
