package model;

import model.base.BaseEntity;
import exception.InvalidInputException;

public class RecyclingCenter extends BaseEntity {
    private int capacity;
    private Address address;

    public RecyclingCenter(int id, String name, int capacity, Address address) {
        super(id, name);
        this.capacity = capacity;
        this.address = address;
    }

    @Override
    public String getType() {
        return "RecyclingCenter";
    }

    @Override
    public void validate() throws InvalidInputException {
        if (getName() == null || getName().trim().isEmpty()) {
            throw new InvalidInputException("Center name cannot be empty");
        }
        if (capacity <= 0) {
            throw new InvalidInputException("Capacity must be positive");
        }
    }

    public String getLocationInfo() {
        return getName() + " located at " + address.getFullAddress();
    }

    // Getters/Setters
    public int getCapacity() { return capacity; }
    public Address getAddress() { return address; }
}
