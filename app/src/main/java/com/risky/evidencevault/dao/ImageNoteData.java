package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.StringArrayConverter;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;

@Entity
public class ImageNoteData extends BaseNoteData {
    public int iconIndex;

    @Convert(converter = StringArrayConverter.class, dbType = String.class)
    public List<String> list;

    public ImageNoteData() {
        iconIndex = -1;
    }
}
