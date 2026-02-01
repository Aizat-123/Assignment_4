package controller;

import model.*;
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
        this.service = new WasteItemsServiceImpl(new WasteItemsRepository());
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("""
                1. List waste (sorted)
                2. Add waste
                3. Delete waste
                4. Inspect class (Reflection)
                0. Exit
                """);

            try {
                switch (scanner.nextLine()) {
                    case "1" -> listWaste();
                    case "2" -> addWaste();
                    case "3" -> deleteWaste();
                    case "4" -> ReflectionUtils.inspectClass(PlasticWaste.class);
                    case "0" -> running = false;
                }
            } catch (WasteAppException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private void listWaste() {
        List<WasteItems> list = service.getAllWaste();
        SortingUtils.sortByWeight(list);

        list.forEach(w ->
                System.out.printf("[%d] %s (%s) cost=%.2f%n",
                        w.getId(), w.getName(),
                        w.getWasteType(),
                        w.calculateRecyclingCost()));
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

    private void deleteWaste() {
        System.out.print("ID: ");
        service.removeWaste(Integer.parseInt(scanner.nextLine()));
    }
}