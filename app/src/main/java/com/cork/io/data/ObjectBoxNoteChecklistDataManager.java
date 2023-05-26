package com.cork.io.data;

import com.cork.io.dao.ChecklistNoteData;
import com.cork.io.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxNoteChecklistDataManager implements NoteDataManager<ChecklistNoteData> {
    private static ObjectBoxNoteChecklistDataManager manager;

    public static ObjectBoxNoteChecklistDataManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteChecklistDataManager();
        }

        return manager;
    }

    @Override
    public ChecklistNoteData add(ChecklistNoteData data) {
        long id = ObjectBox.get().boxFor(ChecklistNoteData.class).put(data);
        return findById(id);
    }

    @Override
    public ChecklistNoteData findById(long id) {
        return ObjectBox.get().boxFor(ChecklistNoteData.class).get(id);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(ChecklistNoteData.class).remove(id);
    }

    @Override
    public boolean update(ChecklistNoteData data) {
        ObjectBox.get().boxFor(ChecklistNoteData.class).put(data);
        return true;
    }

    @Override
    public List<ChecklistNoteData> getAll() {
        return ObjectBox.get().boxFor(ChecklistNoteData.class).getAll();
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(ChecklistNoteData.class).removeAll();
        return true;
    }
}
