package com.cork.io.objectbox;

import android.content.Context;

import com.cork.io.dao.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * Wrapper class for ObjectBox
 */
public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore get() { return boxStore; }
}
