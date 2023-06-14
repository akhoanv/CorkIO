package com.risky.jotterbox.utils;

import com.risky.jotterbox.struct.NoteType;

/**
 * Callback for when new note is created
 *
 * @author Khoa Nguyen
 */
public interface NewNoteCallback {
    void run(NoteType type);
}
