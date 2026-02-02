package repository.interfaces;

import model.WasteItems;
import java.util.List;
import java.util.Optional;

public interface WasteRepository extends CrudRepository<WasteItems, Integer> {
    double getTotalWeightByCenter(int centerId);
    List<WasteItems> findByCriteria(Criteria<WasteItems> criteria);
    List<WasteItems> findRecyclableItems();
    List<WasteItems> findByWasteType(String wasteType);
    List<WasteItems> findByCenter(int centerId);
    void createBatch(List<WasteItems> items);
    Optional<WasteItems> findByIdOptional(Integer id);

    @FunctionalInterface
    interface Criteria<T> {
        boolean test(T item);
    }
}