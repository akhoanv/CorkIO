package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * Base data object for a {@link com.risky.evidencevault.struct.NoteType} database record to inherit
 * Can be used as a database object itself
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(507492008547493052L)
public class BaseNoteData {
    @Id
    public long id;

    // No Uid annotation from here. Uid for inherited fields cannot be inherited,
    // learned it the hard way. Guess we don't update this *ever* when the product is released
    public String content;

    public BaseNoteData() {}
}
