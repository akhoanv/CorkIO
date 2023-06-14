package com.risky.jotterbox.struct;

import com.risky.jotterbox.fragment.notepreset.INoteEditSummaryFragment;
import com.risky.jotterbox.fragment.notepreset.NoteEditSummaryChecklistFragment;
import com.risky.jotterbox.fragment.notepreset.NoteEditSummaryContactFragment;
import com.risky.jotterbox.fragment.notepreset.NoteEditSummaryEventFragment;
import com.risky.jotterbox.fragment.notepreset.NoteEditSummaryGenericFragment;
import com.risky.jotterbox.fragment.notepreset.NoteEditSummaryImageFragment;
import com.risky.jotterbox.fragment.notepreset.NoteEditSummaryLocationFragment;
import com.risky.jotterbox.fragment.notepreset.NoteEditSummaryWebLinkFragment;

/**
 * Represent type of note, and containing default data values for each type
 *
 * @author Khoa Nguyen
 */
public enum NoteType {
    GENERIC("Untitled Generic Note", NoteIcon.GENERIC, NoteEditSummaryGenericFragment.class),
    CONTACT("Untitled Contact Note", NoteIcon.CONTACT, NoteEditSummaryContactFragment.class),
    LOCATION("Untitled Location Note", NoteIcon.LOCATION, NoteEditSummaryLocationFragment.class),
    EVENT("Untitled Event Note", NoteIcon.EVENT, NoteEditSummaryEventFragment.class),
    CHECKLIST("Untitled Checklist Note", NoteIcon.CHECKLIST, NoteEditSummaryChecklistFragment.class),
    IMAGE("Untitled Image Note", NoteIcon.IMAGE, NoteEditSummaryImageFragment.class),
    WEBLINK("Untitled Link Note Note", NoteIcon.WEBLINK, NoteEditSummaryWebLinkFragment.class);

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
