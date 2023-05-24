package com.cork.io.struct;

public enum NoteType {
    GENERIC("Untitled generic note", NoteIcon.GENERIC);

    private String initialTitle;
    private NoteIcon initialIcon;

    NoteType(String initialTitle, NoteIcon initialIcon) {
        this.initialTitle = initialTitle;
        this.initialIcon = initialIcon;
    }

    public String getInitialTitle() {
        return initialTitle;
    }

    public NoteIcon getInitialIcon() {
        return initialIcon;
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
