import controller.ConsoleController;
import model.*;

public class Main {
    public static void main(String[] args) {
        ConsoleController controller = new ConsoleController();
        controller.start();
        System.out.println("\n=== Демонстрация композиции ===");
        Address address = new Address("123 Green St", "EcoCity", "EC123");
        RecyclingCenter center = new RecyclingCenter(1, "EcoCenter", 1000, address);
        System.out.println("Создан центр: " + center.getLocationInfo());
    }
}