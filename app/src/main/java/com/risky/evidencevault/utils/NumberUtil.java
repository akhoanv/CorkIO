package com.risky.evidencevault.utils;

/**
 * Contains all utilities for number manipulation
 *
 * @author Khoa Nguyen
 */
public class NumberUtil {
    public static String convertToDisplayId(Long id) {
        return Long.toHexString(id).toUpperCase();
    }
}
