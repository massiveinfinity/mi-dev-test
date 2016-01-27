package com.blackhat.factory;

import java.util.List;

/**
 * @param <T>
 * @author Sachith Dickwella
 */
public abstract class FactoryFacade<T> {

    public abstract int create(T type);

    public abstract List<T> getAll();

    public T getById(int id) {
        return null;
    }

    public T getByUserName(String userName) {
        return null;
    }

    public List<T> getByName(String name) {
        return null;
    }

    public abstract int update(T type);

    public abstract int delete(int id);
}
