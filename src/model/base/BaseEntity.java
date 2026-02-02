package model.base;

import exception.InvalidInputException;

public abstract class BaseEntity {
    private int id;
    private String name;

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Абстрактные методы (требуется минимум 2)
    public abstract String getType();
    public abstract void validate() throws InvalidInputException;

    // Конкретный метод (требуется минимум 1)
    public String getInfo() {
        return String.format("[%d] %s (%s)", id, name, getType());
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
