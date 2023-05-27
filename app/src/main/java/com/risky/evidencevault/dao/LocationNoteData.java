package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;

@Entity
public class LocationNoteData extends BaseNoteData {
    public String address;

    public LocationNoteData() {}
}
