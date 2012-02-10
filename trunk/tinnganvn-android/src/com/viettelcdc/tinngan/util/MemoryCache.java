package com.viettelcdc.tinngan.util;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryCache<T> {
    private Map<String, SoftReference<T>> cache=Collections.synchronizedMap(new HashMap<String, SoftReference<T>>());
    
    public T get(String id){
        if(!cache.containsKey(id))
            return null;
        SoftReference<T> ref=cache.get(id);
        return ref.get();
    }
    
    public void put(String id, T obj){
        cache.put(id, new SoftReference<T>(obj));
    }

    public void clear() {
        cache.clear();
    }
}