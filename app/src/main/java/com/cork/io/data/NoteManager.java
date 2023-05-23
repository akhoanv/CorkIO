package com.cork.io.data;

import com.cork.io.dao.Note;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Manager Note objects in the database.
 */
public interface NoteManager {
    /**
     * Add new note to the database.
     * @param note new note.
     * @return same note object, but with allocated ID
     */
    long addNote(Note note);

    /**
     * Add new notes in batch to the database.
     * @param notes list of new notes.
     * @return true if all are successfully added, false if not.
     */
    boolean addNotes(List<Note> notes);

    /**
     * Find and retrieve note by ID.
     * @param id ID of the note.
     * @return the found note, null if not found.
     */
    Note findNoteById(long id);

    /**
     * Find and retrieve notes by IDs.
     * @param ids list of IDs to look for.
     * @return list of found notes, should return empty list if none was found.
     */
    List<Note> findNotesById(List<Long> ids);

    /**
     * Remove note from the database.
     * @param id ID of the note to be removed.
     * @return true if the note is removed, false if not.
     */
    boolean removeNote(long id);

    /**
     * Remove all notes in the database.
     * @return true if all notes were removed, false if not.
     */
    boolean removeAllNotes();

    /**
     * Update note in the database.
     * @param note the note to be updated.
     * @return true if the note is updated, false if not.
     */
    boolean updateNote(Note note);

    /**
     * Retrieve all notes in the database.
     * @return list of notes in the database, should return empty if none was found.
     */
    List<Note> getAllNotes();

    /**
     * Add new note to the database.
     * @param note new note.
     * @return the ID of the added note.
     */
    CompletableFuture<Long> addNoteAsync(Note note);

    /**
     * Add new notes in batch to the database.
     * @param notes list of new notes.
     * @return true if all are successfully added, false if not.
     */
    CompletableFuture<Boolean> addNotesAsync(List<Note> notes);

    /**
     * Find and retrieve note by ID.
     * @param id ID of the note.
     * @return the found note, null if not found.
     */
    CompletableFuture<Note> findNoteByIdAsync(long id);

    /**
     * Find and retrieve notes by IDs.
     * @param ids list of IDs to look for.
     * @return list of found notes, should return empty list if none was found.
     */
    CompletableFuture<List<Note>> findNotesByIdAsync(List<Long> ids);

    /**
     * Remove note from the database.
     * @param id ID of the note to be removed.
     * @return true if the note is removed, false if not.
     */
    CompletableFuture<Boolean> removeNoteAsync(long id);

    /**
     * Remove all notes in the database.
     * @return true if all notes were removed, false if not.
     */
    CompletableFuture<Boolean> removeAllNotesAsync();

    /**
     * Update note in the database.
     * @param note the note to be updated.
     * @return true if the note is updated, false if not.
     */
    CompletableFuture<Boolean> updateNoteAsync(Note note);

    /**
     * Retrieve all notes in the database.
     * @return list of notes in the database, should return empty if none was found.
     */
    CompletableFuture<List<Note>> getAllNotesAsync();
}
