package com.risky.jotterbox.struct;

import com.risky.jotterbox.R;

/**
 * Represent default icon taking from drawable resource for each type of note
 *
 * @author Khoa Nguyen
 */
public enum NoteIcon {
    GENERIC(R.drawable.generic_note),
    CONTACT(R.drawable.contact_note),
    LOCATION(R.drawable.location_note),
    EVENT(R.drawable.event_note),
    CHECKLIST(R.drawable.checklist_note),
    IMAGE(R.drawable.image_note),
    WEBLINK(R.drawable.url_note);

    private int iconId;

    NoteIcon(int iconId) {
        this.iconId = iconId;
    }

    public int getId() {
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
