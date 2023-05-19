package com.cork.io.data;

import com.cork.io.dao.Connection;
import java.util.List;

/**
 * Manage Connection object in the database
 *
 * @author knguyen
 */
public interface ConnectionManager {
    /**
     * Add new connection to the database.
     * @param connection new connection
     * @return the ID of the added note.
     */
    Connection addConnection(Connection connection);

    /**
     * Find and retrieve connection by ID.
     * @param id ID of the connection.
     * @return the found connection, null if not found.
     */
    Connection findConnectionById(long id);

    /**
     * Remove connection from the database.
     * @param id ID of the connection to be removed.
     * @return true if the connection is removed, false if not.
     */
    boolean removeConnection(long id);

    /**
     * Remove all connections in the database.
     * @return true if all connections were removed, false if not.
     */
    boolean removeAllConnections();

    /**
     * Update connection in the database.
     * @param connection the connection to be updated.
     * @return true if the connection is updated, false if not.
     */
    boolean updateConnection(Connection connection);

    /**
     * Retrieve all connections in the database.
     * @return list of connections in the database, should return empty if none was found.
     */
    List<Connection> getAllConnections();
}
