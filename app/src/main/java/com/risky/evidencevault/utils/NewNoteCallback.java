package com.risky.evidencevault.utils;

import com.risky.evidencevault.struct.NoteType;

/**
 * Callback for when new note is created
 *
 * @author Khoa Nguyen
 */
public interface NewNoteCallback {
    void run(NoteType type);
}
