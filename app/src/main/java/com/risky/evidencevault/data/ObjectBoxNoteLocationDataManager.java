package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.LocationNoteData;
import com.risky.evidencevault.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxNoteLocationDataManager implements NoteDataManager<LocationNoteData> {
    private static ObjectBoxNoteLocationDataManager manager;

    public static ObjectBoxNoteLocationDataManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteLocationDataManager();
        }

        return manager;
    }

    @Override
    public LocationNoteData add(LocationNoteData data) {
        long id = ObjectBox.get().boxFor(LocationNoteData.class).put(data);
        return findById(id);
    }

    @Override
    public LocationNoteData findById(long id) {
        return ObjectBox.get().boxFor(LocationNoteData.class).get(id);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(LocationNoteData.class).remove(id);
    }

    @Override
    public boolean update(LocationNoteData data) {
        ObjectBox.get().boxFor(LocationNoteData.class).put(data);
        return true;
    }

    @Override
    public List<LocationNoteData> getAll() {
        return ObjectBox.get().boxFor(LocationNoteData.class).getAll();
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(LocationNoteData.class).removeAll();
        return true;
    }
}
