package com.risky.evidencevault.utils;


import java.util.LinkedHashSet;

/**
 * A {@link LinkedHashSet} specifically designed for Connection, as it can only contains distinct 2 elements
 *
 * @param <T> any type
 */
public class ConnectionSet<T> extends LinkedHashSet<T> {
    @Override
    public boolean add(T o) {
        if (size() <= 2) {
            return super.add(o);
        }

        return false;
    }
}
