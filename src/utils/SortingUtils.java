package utils;

import model.WasteItems;

import java.util.Comparator;
import java.util.List;

public class SortingUtils {

    public static final Comparator<WasteItems> BY_WEIGHT =
            (a, b) -> Double.compare(a.getWeight(), b.getWeight());

    public static final Comparator<WasteItems> BY_RECYCLING_COST =
            (a, b) -> Double.compare(a.calculateRecyclingCost(), b.calculateRecyclingCost());

    public static <T> void sort(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
    }

    public static void sortByWeight(List<WasteItems> list) {
        sort(list, BY_WEIGHT);
    }

    public static List<WasteItems> filterRecyclable(List<WasteItems> list) {
        return list.stream()
                .filter(WasteItems::isRecyclable) // лямбда через method reference
                .toList();
    }
}
