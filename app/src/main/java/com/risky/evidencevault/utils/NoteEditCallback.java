package com.risky.evidencevault.utils;

/**
 * Callback for when user finishes editing a note a return to the world board UI
 *
 * @author Khoa Nguyen
 */
public interface NoteEditCallback {
   void run(boolean doDelete);
}
