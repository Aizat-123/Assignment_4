package model;

import model.interfaces.Chargeable;
import model.interfaces.Searchable;
import model.interfaces.Validatable;
import model.base.BaseEntity;
import exception.InvalidInputException;

public abstract class WasteItems extends BaseEntity implements Chargeable, Validatable, Searchable<String> {

    protected double weight;
    protected boolean recyclable;
    protected int centerId;

    public WasteItems(int id, String name, double weight, boolean recyclable) {
        super(id,name);
        this.weight = weight;
        this.recyclable = recyclable;
    }

    @Override
    public String getType() {
        return getWasteType();
    }

    @Override
    public void validate() {
        if (getName() == null || getName().trim().isEmpty())
            throw new InvalidInputException("Waste item name cannot be empty");
        if (weight <= 0)
            throw new InvalidInputException("Weight must be positive");
    }

    @Override
    public boolean matches(String criteria) {
        return getName().toLowerCase().contains(criteria.toLowerCase()) ||
                getWasteType().toLowerCase().contains(criteria.toLowerCase());
    }

    public double calculateEnvironmentalImpact() {
        return weight * (recyclable ? 0.1 : 1.0);
    }

    public abstract String getWasteType();

    public String disposalInfo() {
        return recyclable ? "Recycle" : "Dispose";
    }

    // getters / setters
    public double getWeight() { return weight; }
    public boolean isRecyclable() { return recyclable; }
    public int getCenterId() { return centerId; }
    public void setCenterId(int centerId) { this.centerId = centerId; }
}
