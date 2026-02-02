package service;

import model.*;
import repository.WasteItemsRepository;
import repository.interfaces.WasteRepository;
import exception.*;
import service.interfaces.WasteItemsService;

import java.util.List;
import java.util.Optional;

public class WasteItemsServiceImpl implements WasteItemsService {
    private final WasteRepository repository;

    public WasteItemsServiceImpl(WasteRepository repository) {
        this.repository = repository;
    }

    public void registerWaste(WasteItems item, int centerId) {
        item.validate();
        item.setCenterId(centerId);

        Optional<WasteItems> existing = repository.findByIdOptional(item.getId());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Waste item already exists");
        }

        double totalWeight = repository.getTotalWeightByCenter(centerId);
        int centerCapacity = 1000;

        repository.create(item);
        System.out.println("Service: Waste item registered");
    }

    public List<WasteItems> getAllWaste() {
        return repository.findAll();
    }

    public void removeWaste(int id) {
        if (repository.getById(id) == null)
            throw new ResourceNotFoundException("Waste item not found");
        repository.delete(id);
        System.out.println("Service: Waste item removed");
    }
    public List<WasteItems> getAllSortedByWeight() {
        List<WasteItems> items = repository.findAll();
        items.sort((a, b) -> Double.compare(a.getWeight(), b.getWeight())); // ЛЯМБДА
        return items;
    }
}
