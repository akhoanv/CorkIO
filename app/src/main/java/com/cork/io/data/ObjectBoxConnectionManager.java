package com.cork.io.data;

import com.cork.io.dao.Connection;
import com.cork.io.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxConnectionManager implements ConnectionManager{
    public static ObjectBoxConnectionManager manager;

    public static ObjectBoxConnectionManager get() {
        if (manager == null) {
            manager = new ObjectBoxConnectionManager();
        }

        return manager;
    }

    @Override
    public Connection addConnection(Connection connection) {
        long id = ObjectBox.get().boxFor(Connection.class).put(connection);
        return findConnectionById(id);
    }

    @Override
    public Connection findConnectionById(long id) {
        return ObjectBox.get().boxFor(Connection.class).get(id);
    }

    @Override
    public boolean removeConnection(long id) {
        return ObjectBox.get().boxFor(Connection.class).remove(id);
    }

    @Override
    public boolean removeAllConnections() {
        ObjectBox.get().boxFor(Connection.class).removeAll();
        return true;
    }

    @Override
    public boolean updateConnection(Connection connection) {
        ObjectBox.get().boxFor(Connection.class).put(connection);
        return true;
    }

    @Override
    public List<Connection> getAllConnections() {
        return ObjectBox.get().boxFor(Connection.class).getAll();
    }
}
