package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.BaseNoteData;
import com.risky.evidencevault.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxNoteGenericDataManager implements NoteDataManager<BaseNoteData> {
    private static ObjectBoxNoteGenericDataManager manager;

    public static ObjectBoxNoteGenericDataManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteGenericDataManager();
        }

        return manager;
    }

    @Override
    public BaseNoteData add(BaseNoteData data) {
        long id = ObjectBox.get().boxFor(BaseNoteData.class).put(data);
        return findById(id);
    }

    @Override
    public BaseNoteData findById(long id) {
        return ObjectBox.get().boxFor(BaseNoteData.class).get(id);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(BaseNoteData.class).remove(id);
    }

    @Override
    public boolean update(BaseNoteData data) {
        ObjectBox.get().boxFor(BaseNoteData.class).put(data);
        return true;
    }

    @Override
    public List<BaseNoteData> getAll() {
        return ObjectBox.get().boxFor(BaseNoteData.class).getAll();
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(BaseNoteData.class).removeAll();
        return true;
    }
}
