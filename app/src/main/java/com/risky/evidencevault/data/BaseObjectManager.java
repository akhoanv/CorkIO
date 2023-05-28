package com.risky.evidencevault.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BaseObjectManager<T> {
    /**
     * Add new object to the database.
     * @param object new object.
     * @return same object object, but with allocated ID
     */
    T add(T object);

    /**
     * Add new objects in batch to the database.
     * @param objects list of new objects.
     * @return true if all are successfully added, false if not.
     */
    boolean addMany(List<T> objects);

    /**
     * Find and retrieve object by ID.
     * @param id ID of the object.
     * @return the found object, null if not found.
     */
    T findById(long id);

    /**
     * Find and retrieve objects by IDs.
     * @param ids list of IDs to look for.
     * @return list of found objects, should return empty list if none was found.
     */
    List<T> findManyById(List<Long> ids);

    /**
     * Remove object from the database.
     * @param id ID of the object to be removed.
     * @return true if the object is removed, false if not.
     */
    boolean remove(long id);

    /**
     * Remove all objects in the database.
     * @return true if all objects were removed, false if not.
     */
    boolean removeAll();

    /**
     * Update object in the database.
     * @param object the object to be updated.
     * @return true if the object is updated, false if not.
     */
    boolean update(T object);

    /**
     * Retrieve all objects in the database.
     * @return list of objects in the database, should return empty if none was found.
     */
    List<T> getAll();

    /**
     * Add new object to the database.
     * @param object new object.
     * @return the ID of the added object.
     */
    CompletableFuture<T> addAsync(T object);

    /**
     * Add new objects in batch to the database.
     * @param objects list of new objects.
     * @return true if all are successfully added, false if not.
     */
    CompletableFuture<Boolean> addManyAsync(List<T> objects);

    /**
     * Find and retrieve object by ID.
     * @param id ID of the object.
     * @return the found object, null if not found.
     */
    CompletableFuture<T> findByIdAsync(long id);

    /**
     * Find and retrieve objects by IDs.
     * @param ids list of IDs to look for.
     * @return list of found objects, should return empty list if none was found.
     */
    CompletableFuture<List<T>> findManyByIdAsync(List<Long> ids);

    /**
     * Remove object from the database.
     * @param id ID of the object to be removed.
     * @return true if the object is removed, false if not.
     */
    CompletableFuture<Boolean> removeAsync(long id);

    /**
     * Remove all objects in the database.
     * @return true if all objects were removed, false if not.
     */
    CompletableFuture<Boolean> removeAllAsync();

    /**
     * Update object in the database.
     * @param object the object to be updated.
     * @return true if the object is updated, false if not.
     */
    CompletableFuture<Boolean> updateAsync(T object);

    /**
     * Retrieve all objects in the database.
     * @return list of objects in the database, should return empty if none was found.
     */
    CompletableFuture<List<T>> getAllAsync();
}
