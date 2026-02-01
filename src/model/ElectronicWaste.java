package model;

public class ElectronicWaste extends WasteItems {

    public ElectronicWaste(int id, String name, double weight, boolean recyclable) {
        super(id, name, weight, recyclable);
        this.setCenterId(centerId);
    }

    @Override
    public String getWasteType() {
        return "electronic";
    }

    @Override
    public double calculateRecyclingCost(){
        return weight * 600;
    }
}
