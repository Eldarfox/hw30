import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        RestaurantOrders orders = RestaurantOrders.read("orders_100.json");

        if (orders.getOrders().isEmpty()) {
            System.out.println("no data");
        }

        boolean running = true;

        while (running) {
            printMenu();

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("Total sum of all orders:");
                    System.out.println(orders.totalOrdersSum());
                }
                case "2" -> {
                    System.out.println("Print all orders:");
                    orders.printOrders();
                }
                case "3" -> {
                    int n = readPositiveInt("Enter N: ");
                    orders.topNByTotal(n)
                            .forEach(o -> System.out.println(o.getTotal()));
                }
                case "4" -> {
                    int n = readPositiveInt("Enter N: ");
                    orders.bottomNByTotal(n)
                            .forEach(o -> System.out.println(o.getTotal()));
                }
                case "5" -> {
                    System.out.println("Home delivery orders:");
                    orders.homeDeliveryOrders()
                            .forEach(o -> System.out.println(o.getTotal()));
                }
                case "6" -> {
                    System.out.println("Max home delivery order:");
                    orders.maxHomeDeliveryOrder()
                            .ifPresentOrElse(
                                    o -> System.out.println(o.getTotal()),
                                    () -> System.out.println("No home delivery orders")
                            );
                }
                case "7" -> {
                    System.out.println("Min home delivery order:");
                    orders.minHomeDeliveryOrder()
                            .ifPresentOrElse(
                                    o -> System.out.println(o.getTotal()),
                                    () -> System.out.println("No home delivery orders")
                            );
                }
                case "8" -> {
                    double min = readPositiveDouble("Enter min total: ");
                    double max = readPositiveDouble("Enter max total: ");
                    orders.ordersBetween(min, max)
                            .forEach(o -> System.out.println(o.getTotal()));
                }
                case "9" -> {
                    System.out.println("Orders grouped by customer name:");
                    orders.ordersByCustomerName()
                            .forEach((name, list) ->
                                    System.out.println(name + " -> " + list.size())
                            );
                }
                case "10" -> {
                    System.out.println("Total sum by customer:");
                    orders.totalByCustomer()
                            .forEach((name, sum) ->
                                    System.out.println(name + " -> " + sum)
                            );
                }
                case "11" -> {
                    System.out.println("Customer with max total:");
                    orders.customerWithMaxTotal()
                            .ifPresent(System.out::println);
                }
                case "12" -> {
                    System.out.println("Customer with min total:");
                    orders.customerWithMinTotal()
                            .ifPresent(System.out::println);
                }
                case "13" -> {
                    System.out.println("Sold items statistics:");
                    orders.soldItemsAmount()
                            .forEach((item, amount) ->
                                    System.out.println(item.getName() + " -> " + amount)
                            );
                }
                case "14" -> {
                    System.out.println("Total sum of all orders (duplicate check):");
                    System.out.println(orders.totalOrdersSum());
                }
                case "15" -> {
                    System.out.print("Enter item name: ");
                    String itemName = sc.nextLine().trim();
                    var emails = orders.emailsByItemName(itemName);
                    if (emails.isEmpty()) {
                        System.out.println("No orders found with item: " + itemName);
                    } else {
                        emails.forEach(System.out::println);
                    }
                }
                case "0" -> {
                    running = false;
                    System.out.println("Exit.");
                }
                default -> System.out.println("Invalid option. Try again.");
            }
            System.out.println();
        }
    }

    private static int readPositiveInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                if (value > 0) return value;
                System.out.println("Enter positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private static double readPositiveDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value >= 0) return value;
                System.out.println("Enter non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("""
                ===== RESTAURANT ORDERS MENU =====
                1  - Total sum of all orders
                2  - Print all orders
                3  - Top N most expensive orders
                4  - Top N cheapest orders
                5  - Home delivery orders
                6  - Max home delivery order
                7  - Min home delivery order
                8  - Orders between min and max total
                9  - Orders grouped by customer name
                10 - Total sum by customer
                11 - Customer with max total
                12 - Customer with min total
                13 - Sold items statistics
                14 - Total sum (recheck)
                15 - Emails by item name
                0  - Exit
                =================================
                Choose option:
                """);
    }
}
