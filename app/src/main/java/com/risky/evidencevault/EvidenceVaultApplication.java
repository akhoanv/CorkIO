package com.risky.evidencevault;

import android.app.Application;

import com.risky.evidencevault.objectbox.ObjectBox;

/**
 * Customize initialization of our application
 */
public class EvidenceVaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }
}
