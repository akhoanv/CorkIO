package com.risky.evidencevault.utils;

/**
 * Storing different aspects of the current device, for scaling/adapting purpose
 *
 * @author Khoa Nguyen
 */
public class DeviceProperties {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}
