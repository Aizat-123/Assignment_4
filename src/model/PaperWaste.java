package model;

public class PaperWaste extends WasteItems {

    public PaperWaste(int id, String name, double weight, boolean recyclable) {
        super(id, name, weight, recyclable);
        this.setCenterId(centerId);
    }

    @Override
    public String getWasteType() {
        return "paper";
    }

    @Override
    public double calculateRecyclingCost() {
        return weight * 50;
    }
}