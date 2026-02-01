package model;

import exception.InvalidInputException;

public abstract class WasteItems implements Chargeable, Validatable {

    private int id;
    private String name;
    private double weight;
    private boolean recyclable;
    private int centerId;

    public WasteItems(int id, String name, double weight, boolean recyclable) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.recyclable = recyclable;
    }

    // concrete method
    public String disposalInfo() {
        return recyclable ? "Recycle" : "Dispose";
    }

    // abstract methods
    public abstract String getWasteType();

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Waste item name cannot be empty");
        if (weight <= 0)
            throw new InvalidInputException("Weight must be positive");
    }

    // getters / setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getWeight() { return weight; }
    public boolean isRecyclable() { return recyclable; }
    public int getCenterId() { return centerId; }
    public void setCenterId(int centerId) { this.centerId = centerId; }
}
