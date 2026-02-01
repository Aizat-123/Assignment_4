package utils;

import model.WasteItems;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {

    public static void sortByWeight(List<WasteItems> list) {
        list.sort(Comparator.comparingDouble(WasteItems::getWeight));
    }
}
