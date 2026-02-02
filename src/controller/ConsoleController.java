package controller;

import model.*;
import model.interfaces.Searchable;
import repository.WasteItemsRepository;
import service.WasteItemsServiceImpl;
import service.interfaces.WasteItemsService;
import utils.ReflectionUtils;
import utils.SortingUtils;
import exception.WasteAppException;

import java.util.List;
import java.util.Scanner;

public class ConsoleController {

    private final WasteItemsService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleController() {
        WasteItemsRepository repository = new WasteItemsRepository();
        this.service = new WasteItemsServiceImpl(repository);
    }
    public ConsoleController(WasteItemsService service) {
        this.service = service;
    }
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("""
                1. List waste (sorted by weight)
                2. List recyclable waste only
                3. Add waste
                4. Delete waste
                5. Inspect PlasticWaste class (Reflection)
                6. Demonstrate polymorphism
                7. Demonstrate interface static method
                0. Exit
                """);

            try {
                switch (scanner.nextLine()) {
                    case "1" -> listWasteSorted();
                    case "2" -> listRecyclableOnly();
                    case "3" -> addWaste();
                    case "4" -> deleteWaste();
                    case "5" -> ReflectionUtils.inspectClass(PlasticWaste.class);
                    case "6" -> demonstratePolymorphism();
                    case "7" -> demonstrateInterfaceStaticMethod();
                    case "0" -> running = false;

                }
            } catch (WasteAppException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private void listWasteSorted() {
        List<WasteItems> list = service.getAllWaste();
        SortingUtils.sortByWeight(list);

        list.forEach(w ->
                System.out.printf("[%d] %s (%s) %.2f kg, cost=$%.2f, impact=%.2f%n",
                        w.getId(), w.getName(),
                        w.getWasteType(), w.getWeight(),
                        w.calculateRecyclingCost(),
                        w.calculateEnvironmentalImpact()));
    }

    private void listRecyclableOnly() {
        List<WasteItems> list = service.getAllWaste();
        List<WasteItems> recyclable = SortingUtils.filterRecyclable(list);

        System.out.println("\n=== Recyclable Items Only ===");
        recyclable.forEach(w ->
                System.out.printf("[%d] %s (%s)%n",
                        w.getId(), w.getName(), w.getWasteType()));
    }

    private void addWaste() {
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Type: ");
        String type = scanner.nextLine();
        System.out.print("Weight: ");
        double weight = Double.parseDouble(scanner.nextLine());

        WasteItems item = switch (type) {
            case "plastic" -> new PlasticWaste(id, name, weight, true);
            case "electronic" -> new ElectronicWaste(id, name, weight, true);
            default -> new PaperWaste(id, name, weight, true);
        };

        service.registerWaste(item, 1);
    }
    private void demonstratePolymorphism() {
        System.out.println("\n=== Polymorphism Demonstration ===");

        WasteItems plastic = new PlasticWaste(999, "Test Plastic", 5.0, true);
        WasteItems electronic = new ElectronicWaste(998, "Test Electronic", 1.0, true);
        WasteItems paper = new PaperWaste(997, "Test Paper", 2.0, true);

        List<WasteItems> items = List.of(plastic, electronic, paper);

        items.forEach(item -> {
            System.out.println(item.getInfo()); // метод из BaseEntity
            System.out.println("  Disposal: " + item.disposalInfo());
            System.out.println("  Valid: " + item.isValid()); // default method из Validatable
        });
    }

    private void demonstrateInterfaceStaticMethod() {
        System.out.println("\n=== Interface Static Method ===");
        WasteItems testItem = new PlasticWaste(999, "Water Bottle", 1.0, true);

        // Использование static метода из интерфейса
        boolean containsBottle = Searchable.containsKeyword(testItem, "bottle");
        System.out.println("Contains 'bottle': " + containsBottle);

        boolean containsMetal = Searchable.containsKeyword(testItem, "metal");
        System.out.println("Contains 'metal': " + containsMetal);
    }
    private void deleteWaste() {
        System.out.print("ID: ");
        service.removeWaste(Integer.parseInt(scanner.nextLine()));
    }
}