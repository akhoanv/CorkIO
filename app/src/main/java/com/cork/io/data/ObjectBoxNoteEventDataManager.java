package com.cork.io.data;

import com.cork.io.dao.EventNoteData;
import com.cork.io.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxNoteEventDataManager implements NoteDataManager<EventNoteData> {
    private static ObjectBoxNoteEventDataManager manager;

    public static ObjectBoxNoteEventDataManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteEventDataManager();
        }

        return manager;
    }

    @Override
    public EventNoteData add(EventNoteData data) {
        long id = ObjectBox.get().boxFor(EventNoteData.class).put(data);
        return findById(id);
    }

    @Override
    public EventNoteData findById(long id) {
        return ObjectBox.get().boxFor(EventNoteData.class).get(id);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(EventNoteData.class).remove(id);
    }

    @Override
    public boolean update(EventNoteData data) {
        ObjectBox.get().boxFor(EventNoteData.class).put(data);
        return true;
    }

    @Override
    public List<EventNoteData> getAll() {
        return ObjectBox.get().boxFor(EventNoteData.class).getAll();
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(EventNoteData.class).removeAll();
        return true;
    }
}
