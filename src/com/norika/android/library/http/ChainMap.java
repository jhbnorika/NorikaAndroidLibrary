
package com.norika.android.library.http;

import java.util.HashMap;

public class ChainMap {
    private final HashMap<String, String> map;

    private ChainMap(String key, String value) {
        this();
        map.put(key, value);
    }

    private ChainMap() {
        map = new HashMap<String, String>();
    }

    public ChainMap put(String key, String value) {
        map.put(key, value);
        return this;
    }

    public HashMap<String, String> map() {
        return map;
    }

    public void putAll(ChainMap chainMap) {
        map.putAll(chainMap.map());
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public static ChainMap create(String key, String value) {
        return new ChainMap(key, value);
    }

    public static ChainMap create() {
        return new ChainMap();
    }

}
