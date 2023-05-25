package com.cork.io.utils;

import com.cork.io.struct.NoteType;

public interface NewNoteCallback {
    void run(NoteType type);
}
