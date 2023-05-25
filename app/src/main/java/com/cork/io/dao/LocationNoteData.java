package com.cork.io.dao;

import io.objectbox.annotation.Entity;

@Entity
public class LocationNoteData extends BaseNoteData{
    public String address;
    public String city;
    public String state;
    public String zip;
    public String country;
    public String longitude;
    public String latitude;

    public LocationNoteData() {}
}
