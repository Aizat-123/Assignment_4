package service.interfaces;

import model.WasteItems;
import java.util.List;

public interface WasteItemsService {
    void registerWaste(WasteItems item, int centerId);
    List<WasteItems> getAllWaste();
    void removeWaste(int id);
}
