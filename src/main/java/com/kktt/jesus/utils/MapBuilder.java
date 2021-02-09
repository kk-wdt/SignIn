package com.kktt.jesus.utils;

import java.util.HashMap;
import java.util.Map;

//add by yexuhui
public class MapBuilder<K, V> {

    private Map<K, V> map = new HashMap<>();

    private MapBuilder() { }

    public static <K, V> MapBuilder<K, V> of(K key, V value) {
        MapBuilder<K, V> builder = new MapBuilder<>();
        builder.map.put(key, value);
        return builder;
    }

    public MapBuilder<K, V> add(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return this.map;
    }
}
