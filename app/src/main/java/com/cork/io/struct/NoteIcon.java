package com.cork.io.struct;

import com.cork.io.R;

public enum NoteIcon {
    GENERIC(R.drawable.icon);

    private int iconId;

    NoteIcon(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public static NoteIcon getTypeFromString(String icon) {
        for (NoteIcon ni : values()) {
            if (icon.equals(ni.name())) {
                return ni;
            }
        }

        return GENERIC; // gotta have a default value
    }
}
