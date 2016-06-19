package com.infinite.myapp.utils;

/**
 * Created by Administrator on 2016-06-18.
 */
public class Utility {
    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
