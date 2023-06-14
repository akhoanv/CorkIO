package com.risky.evidencevault;

import android.app.Application;

import com.risky.evidencevault.objectbox.ObjectBox;

/**
 * Starting point for application
 */
public class EvidenceVaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }
}
