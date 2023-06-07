package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.Setting;
import com.risky.evidencevault.objectbox.ObjectBox;

public class ObjectBoxSettingManager {
    private static ObjectBoxSettingManager manager;
    private static Setting object;

    public static ObjectBoxSettingManager get() {
        if (manager == null) {
            manager = new ObjectBoxSettingManager();
        }

        return manager;
    }

    private ObjectBoxSettingManager() {
        if (ObjectBox.get().boxFor(Setting.class).isEmpty()) {
            object = new Setting();
            ObjectBox.get().boxFor(Setting.class).put(object);
        } else {
            object = ObjectBox.get().boxFor(Setting.class).getAll().get(0);
        }
    }

    public long getLastVisitedBoard() {
        return object.lastVisitedBoard;
    }

    public void setLastVisitedBoard(long boardId) {
        object.lastVisitedBoard = boardId;
        ObjectBox.get().boxFor(Setting.class).put(object);
    }
}
