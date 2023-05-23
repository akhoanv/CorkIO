package com.cork.io.data;

import com.cork.io.dao.Connection;

import java.util.List;

public interface ConnectionManager {
    Connection addConnection(Connection connection);
    Connection findConnectionById(long id);
    boolean removeConnection(long id);
    boolean removeAllConnection();
    boolean updateConnection(Connection connection);
    List<Connection> getAllConnections();
}
