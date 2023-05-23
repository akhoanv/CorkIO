package com.cork.io.data;

import com.cork.io.dao.Connection;

import java.util.List;

/**
 * Manager Connection objects in the database
 */
public interface ConnectionManager {
    /**
     * Add new connection to the database
     *
     * @param connection new connection
     * @return same connection object, but with allocated ID
     */
    Connection addConnection(Connection connection);

    /**
     * Find and retrieve connection by ID
     *
     * @param id ID of the connection
     * @return the found Connection, null if not found
     */
    Connection findConnectionById(long id);

    /**
     * Remove connection from database
     *
     * @param id ID of the connection
     * @return true if Connection was removed, otherwise false
     */
    boolean removeConnection(long id);

    /**
     * Remove all connections from database
     *
     * @return true if all connection was remove, otherwise false
     */
    boolean removeAllConnection();

    /**
     * Update connection in database
     *
     * @param connection connection to be updated
     * @return true if connection was updated, otherwise false
     */
    boolean updateConnection(Connection connection);

    /**
     * Get all existing connection from database
     *
     * @return List of all connections
     */
    List<Connection> getAllConnections();
}
