package model;

import exception.InvalidInputException;

public interface Validatable {

    void validate() throws InvalidInputException;

    default boolean isValid() {
        try {
            validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static void requireNonNull(Object o) {
        if (o == null)
            throw new InvalidInputException("Null value not allowed");
    }
}
