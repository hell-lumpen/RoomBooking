package org.mai.roombooking.utils;

import java.util.HashMap;
import java.util.Map;

public class ObjectsPool <T> {

    Map<String, T> pool;

    public ObjectsPool () {
        pool = new HashMap<>();
    }

    public void add(String key, T value) {
        pool.put(key, value);
    }

    public T get(String key) {
        return pool.get(key);
    }

}
