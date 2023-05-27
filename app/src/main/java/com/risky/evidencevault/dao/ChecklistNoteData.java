package com.risky.evidencevault.dao;

import com.risky.evidencevault.dao.converter.ChecklistItemArrayConverter;
import com.risky.evidencevault.dao.converter.ChecklistSortOrderConverter;
import com.risky.evidencevault.struct.ChecklistItem;
import com.risky.evidencevault.struct.ChecklistSortOrder;

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
