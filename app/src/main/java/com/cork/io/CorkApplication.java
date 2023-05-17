package com.cork.io;

import android.app.Application;

import com.cork.io.objectbox.ObjectBox;

/**
 * Customize initialization of our application
 */
public class CorkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }
}
