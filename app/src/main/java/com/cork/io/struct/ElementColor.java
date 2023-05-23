package com.cork.io.struct;

import android.content.Context;
import android.graphics.Paint;

import com.cork.io.R;

/**
 * Represent color for connections and tags
 *
 * @author knguyen
 */
public enum ElementColor {
    BLUE(R.color.blue),
    GREEN(R.color.green),
    ORANGE(R.color.orange),
    PINK(R.color.light_pink),
    RED(R.color.red),
    YELLOW(R.color.yellow);

    private int colorId;

    ElementColor(int colorId) {
        this.colorId = colorId;
    }

    public Paint getPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(context.getColor(colorId));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10);

        return paint;
    }

    public int getId() {
        return colorId;
    }

    public static ElementColor getTypeFromString(String color) {
        for (ElementColor ec : values()) {
            if (color.equals(ec.name())) {
                return ec;
            }
        }

        return BLUE; // gotta have a default value
    }
}
