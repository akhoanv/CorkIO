package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.StringArrayConverter;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Uid;

/**
 * Data for Image type note
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(7744701942661063032L)
public class ImageNoteData extends BaseNoteData {
    @Uid(1971192994218556777L)
    public int iconIndex;

    @Convert(converter = StringArrayConverter.class, dbType = String.class)
    @Uid(1841814919460543484L)
    public List<String> list;

    public ImageNoteData() {
        iconIndex = -1;
    }
}
