package com.cork.io.utils;


import java.util.LinkedHashSet;

public class ConnectionSet<T> extends LinkedHashSet<T> {
    @Override
    public boolean add(T o) {
        if (size() <= 2) {
            return super.add(o);
        }

        return false;
    }
}
