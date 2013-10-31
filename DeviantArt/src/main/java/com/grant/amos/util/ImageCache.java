package com.grant.amos.util;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by Grant on 9/12/13.
 */
public class ImageCache {
    static HashMap<String, Bitmap> cache;

    public static void init(){
        cache = new HashMap<String, Bitmap>();
    }

    public static void addImage(String key, Bitmap value){
        if(key != null && !key.isEmpty() && value != null)
            cache.put(key, value);
    }

    public static void removeImage(String key, Bitmap value){
        if(cache.containsKey(key))
            cache.remove(key);
    }

    public static Bitmap getImage(String key){
        if(cache.containsKey(key))
            return cache.get(key);
        return null;
    }
}
