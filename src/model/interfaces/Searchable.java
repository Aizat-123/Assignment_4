package model.interfaces;

import model.base.BaseEntity;

public interface Searchable<T> {
    boolean matches(T criteria);

    static <T> boolean containsKeyword(T item, String keyword) {
        return item.toString().toLowerCase().contains(keyword.toLowerCase());
    }
}