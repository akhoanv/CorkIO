package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Uid;

/**
 * Data for Contact note type
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(6447687549601967644L)
public class ContactNoteData extends BaseNoteData {
    @Uid(2517581906662244367L)
    public String name;

    @Uid(143537613007989255L)
    public String phoneNumber;

    @Uid(2622609586988866886L)
    public String emailAddress;

    @Uid(1487727940741944920L)
    public Long bday;

    public ContactNoteData() {
        this.bday = 0L;
    }
}
