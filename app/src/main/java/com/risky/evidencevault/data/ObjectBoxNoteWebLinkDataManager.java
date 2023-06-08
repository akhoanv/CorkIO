package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.WebLinkNoteData;
import com.risky.evidencevault.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxNoteWebLinkDataManager implements NoteDataManager<WebLinkNoteData> {
    private static ObjectBoxNoteWebLinkDataManager manager;

    public static ObjectBoxNoteWebLinkDataManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteWebLinkDataManager();
        }

        return manager;
    }

    @Override
    public WebLinkNoteData add(WebLinkNoteData data) {
        long id = ObjectBox.get().boxFor(WebLinkNoteData.class).put(data);
        return findById(id);
    }

    @Override
    public WebLinkNoteData findById(long id) {
        return ObjectBox.get().boxFor(WebLinkNoteData.class).get(id);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(WebLinkNoteData.class).remove(id);
    }

    @Override
    public boolean update(WebLinkNoteData data) {
        ObjectBox.get().boxFor(WebLinkNoteData.class).put(data);
        return true;
    }

    @Override
    public List<WebLinkNoteData> getAll() {
        return ObjectBox.get().boxFor(WebLinkNoteData.class).getAll();
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(WebLinkNoteData.class).removeAll();
        return true;
    }
}
