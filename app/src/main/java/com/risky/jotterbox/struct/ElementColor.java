package com.risky.jotterbox.struct;

import android.content.Context;
import android.graphics.Paint;

import com.risky.jotterbox.R;

/**
 * Represent common colors for users to choose from
 *
 * @author knguyen
 */
public enum ElementColor {
    BLUE(R.color.blue, R.drawable.background_capsule_blue, R.drawable.color_palette_blue),
    GREEN(R.color.green, R.drawable.background_capsule_green, R.drawable.color_palette_green),
    ORANGE(R.color.orange, R.drawable.background_capsule_orange, R.drawable.color_palette_orange),
    PINK(R.color.light_pink, R.drawable.background_capsule_pink, R.drawable.color_palette_pink),
    RED(R.color.red, R.drawable.background_capsule_red, R.drawable.color_palette_red),
    YELLOW(R.color.yellow, R.drawable.background_capsule_yellow, R.drawable.color_palette_yellow);

    private int colorId;
    private int backgroundId;
    private int roundId;

    ElementColor(int colorId, int backgroundId, int roundId) {
        this.colorId = colorId;
        this.backgroundId = backgroundId;
        this.roundId = roundId;
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

    public int getBackgroundId() {
        return backgroundId;
    }

    public int getRoundId() {
        return roundId;
    }

    public static ElementColor getTypeFromString(String color) {
        for (ElementColor ec : values()) {
            if (color.equals(ec.name())) {
                return ec;
            }
        }

        return BLUE; // gotta have a default value
    }


    public ElementColor next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
