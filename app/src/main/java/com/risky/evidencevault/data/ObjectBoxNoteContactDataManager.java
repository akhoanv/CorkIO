package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.ContactNoteData;
import com.risky.evidencevault.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxNoteContactDataManager implements NoteDataManager<ContactNoteData> {
    private static ObjectBoxNoteContactDataManager manager;

    public static ObjectBoxNoteContactDataManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteContactDataManager();
        }

        return manager;
    }

    @Override
    public ContactNoteData add(ContactNoteData data) {
        long id = ObjectBox.get().boxFor(ContactNoteData.class).put(data);
        return findById(id);
    }

    @Override
    public ContactNoteData findById(long id) {
        return ObjectBox.get().boxFor(ContactNoteData.class).get(id);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(ContactNoteData.class).remove(id);
    }

    @Override
    public boolean update(ContactNoteData data) {
        ObjectBox.get().boxFor(ContactNoteData.class).put(data);
        return true;
    }

    @Override
    public List<ContactNoteData> getAll() {
        return ObjectBox.get().boxFor(ContactNoteData.class).getAll();
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(ContactNoteData.class).removeAll();
        return true;
    }
}
