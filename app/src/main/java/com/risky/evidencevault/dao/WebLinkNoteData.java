package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;

@Entity
public class WebLinkNoteData extends BaseNoteData {
    public String url;

    public WebLinkNoteData() {}
}
