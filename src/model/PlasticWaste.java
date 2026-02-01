package model;

public class PlasticWaste extends WasteItems {

    public PlasticWaste(int id, String name, double weight, boolean recyclable) {
        super(id, name, weight, recyclable);
        this.setCenterId(centerId);
    }

    @Override
    public String getWasteType() {
        return "plastic";
    }

    @Override
    public double calculateRecyclingCost() {
        return weight * 40;
    }
}
