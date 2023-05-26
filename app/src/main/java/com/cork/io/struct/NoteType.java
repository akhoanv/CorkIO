package com.cork.io.struct;

import com.cork.io.fragment.INoteEditSummaryFragment;
import com.cork.io.fragment.NoteEditSummaryContactFragment;
import com.cork.io.fragment.NoteEditSummaryEventFragment;
import com.cork.io.fragment.NoteEditSummaryGenericFragment;
import com.cork.io.fragment.NoteEditSummaryLocationFragment;

public enum NoteType {
    GENERIC("Untitled generic note", NoteIcon.GENERIC, NoteEditSummaryGenericFragment.class),
    CONTACT("Untitled contact note", NoteIcon.CONTACT, NoteEditSummaryContactFragment.class),
    LOCATION("Untitled Location note", NoteIcon.LOCATION, NoteEditSummaryLocationFragment.class),
    EVENT("Untitled event note", NoteIcon.EVENT, NoteEditSummaryEventFragment.class);

    private String initialTitle;
    private NoteIcon initialIcon;
    private Class<? extends INoteEditSummaryFragment> fragment;

    NoteType(String initialTitle, NoteIcon initialIcon,
             Class<? extends INoteEditSummaryFragment> fragment) {
        this.initialTitle = initialTitle;
        this.initialIcon = initialIcon;
        this.fragment = fragment;
    }

    public String getInitialTitle() {
        return initialTitle;
    }

    public NoteIcon getIcon() {
        return initialIcon;
    }

    public Class<? extends INoteEditSummaryFragment> getFragment() {
        return fragment;
    }

    public static NoteType getTypeFromString(String type) {
        for (NoteType nt : values()) {
            if (type.equals(nt.name())) {
                return nt;
            }
        }

        return GENERIC; // gotta have a default value
    }
}
