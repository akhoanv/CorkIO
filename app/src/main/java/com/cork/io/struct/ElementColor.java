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
    BLUE(R.color.blue, R.drawable.note_checklist_item_blue),
    GREEN(R.color.green, R.drawable.note_checklist_item_green),
    ORANGE(R.color.orange, R.drawable.note_checklist_item_orange),
    PINK(R.color.light_pink, R.drawable.note_checklist_item_pink),
    RED(R.color.red, R.drawable.note_checklist_item_red),
    YELLOW(R.color.yellow, R.drawable.note_checklist_item_yellow);

    private int colorId;
    private int backgroundId;

    ElementColor(int colorId, int backgroundId) {
        this.colorId = colorId;
        this.backgroundId = backgroundId;
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

    public static ElementColor getTypeFromString(String color) {
        for (ElementColor ec : values()) {
            if (color.equals(ec.name())) {
                return ec;
            }
        }

        return BLUE; // gotta have a default value
    }
}
