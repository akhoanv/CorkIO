package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.ChecklistItemArrayConverter;
import com.risky.evidencevault.dao.converter.ChecklistSortOrderConverter;
import com.risky.evidencevault.struct.ChecklistItem;
import com.risky.evidencevault.struct.ChecklistSortOrder;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Uid;

/**
 * Data for Checklist note type
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(963096075698626870L)
public class ChecklistNoteData  extends BaseNoteData {
    @Convert(converter = ChecklistItemArrayConverter.class, dbType = String.class)
    @Uid(9104703633512048675L)
    public List<ChecklistItem> list;

    @Convert(converter = ChecklistSortOrderConverter.class, dbType = String.class)
    @Uid(2842026878554156075L)
    public ChecklistSortOrder order;

    @Uid(7230341852083849065L)
    public int completed;

    @Uid(7172984587234460947L)
    public Long lastOrder;

    public ChecklistNoteData() {
        this.order = ChecklistSortOrder.CHRONOLOGICAL;
        this.lastOrder = 0L;
        this.completed = 0;
    }
}
