package com.cork.io.utils;

public class NumberUtil {
    public static String convertToDisplayId(Long id) {
        return Long.toHexString(id).toUpperCase();
    }
}
