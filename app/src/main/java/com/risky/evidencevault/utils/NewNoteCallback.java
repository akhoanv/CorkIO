package com.risky.evidencevault.utils;

import com.risky.evidencevault.struct.NoteType;

public interface NewNoteCallback {
    void run(NoteType type);
}
