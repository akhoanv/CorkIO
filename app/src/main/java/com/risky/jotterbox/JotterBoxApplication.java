package com.risky.jotterbox;

import android.app.Application;

import com.risky.jotterbox.objectbox.ObjectBox;

/**
 * Starting point for application
 */
public class JotterBoxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }
}
