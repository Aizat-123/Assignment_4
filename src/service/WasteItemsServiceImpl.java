package service;

import model.*;
import repository.WasteItemsRepository;
import exception.*;
import repository.interfaces.CrudRepository;
import service.interfaces.WasteItemsService;

import java.util.List;

public class WasteItemsServiceImpl implements WasteItemsService {

    private final CrudRepository<WasteItems, Integer> repository;
    private static final int CENTER_CAPACITY = 1000;

    public WasteItemsServiceImpl(CrudRepository<WasteItems, Integer> repository) {
        this.repository = repository;
    }

    public void registerWaste(WasteItems item, int centerId) {
        item.validate();
        item.setCenterId(centerId);

        if (repository.getById(item.getId()) != null)
            throw new DuplicateResourceException("Waste item already exists");

        double totalWeight = ((WasteItemsRepository) repository).getTotalWeightByCenter(centerId);
        int centerCapacity = 1000;

        repository.create(item);
        System.out.println("Service: Waste item registered");
    }

    public List<WasteItems> getAllWaste() {
        return repository.getAll();
    }

    public void removeWaste(int id) {
        if (repository.getById(id) == null)
            throw new ResourceNotFoundException("Waste item not found");
        repository.delete(id);
        System.out.println("Service: Waste item removed");
    }
}
