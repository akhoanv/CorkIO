package com.cork.io.dao;

import com.cork.io.dao.converter.ChecklistItemArrayConverter;
import com.cork.io.dao.converter.ChecklistSortOrderConverter;
import com.cork.io.struct.ChecklistItem;
import com.cork.io.struct.ChecklistSortOrder;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;

@Entity
public class ChecklistNoteData  extends BaseNoteData {
    @Convert(converter = ChecklistItemArrayConverter.class, dbType = String.class)
    public List<ChecklistItem> list;

    @Convert(converter = ChecklistSortOrderConverter.class, dbType = String.class)
    public ChecklistSortOrder order;

    public Long lastOrder;

    public ChecklistNoteData() {
        this.order = ChecklistSortOrder.CHRONOLOGICAL;
        this.lastOrder = 0L;
    }
}
