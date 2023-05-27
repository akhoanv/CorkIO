package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;

@Entity
public class ContactNoteData extends BaseNoteData {
    public String name;
    public String phoneNumber;
    public String emailAddress;
    public Long bday;

    public ContactNoteData() {
        this.bday = 0L;
    }
}
