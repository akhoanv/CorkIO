package com.cork.io.struct;

import com.cork.io.fragment.notepreset.INoteEditSummaryFragment;
import com.cork.io.fragment.notepreset.NoteEditSummaryChecklistFragment;
import com.cork.io.fragment.notepreset.NoteEditSummaryContactFragment;
import com.cork.io.fragment.notepreset.NoteEditSummaryEventFragment;
import com.cork.io.fragment.notepreset.NoteEditSummaryGenericFragment;
import com.cork.io.fragment.notepreset.NoteEditSummaryLocationFragment;

public enum NoteType {
    GENERIC("Untitled Generic Note", NoteIcon.GENERIC, NoteEditSummaryGenericFragment.class),
    CONTACT("Untitled Contact Note", NoteIcon.CONTACT, NoteEditSummaryContactFragment.class),
    LOCATION("Untitled Location Note", NoteIcon.LOCATION, NoteEditSummaryLocationFragment.class),
    EVENT("Untitled Event Note", NoteIcon.EVENT, NoteEditSummaryEventFragment.class),
    CHECKLIST("Untitled Checklist Note", NoteIcon.CHECKLIST, NoteEditSummaryChecklistFragment.class);

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
