package org.vandrade.support.repository;

import org.jooq.lambda.tuple.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 12:32 PM
 */

public interface BaseRepository<E, ID, F extends Tuple> {
    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be null.
     * @return the entity with the given id or null if none found
     */
    @Transactional(readOnly = true)
    E findOne(ID id);


    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    @Transactional(readOnly = true)
    Collection<E> findAll();


    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    @Transactional(readOnly = true)
    Collection<E> findAll(F filters, Pageable pageable);


    /**
     * Returns all instances of the type with the given IDs.
     *
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    Collection<E> findAll(Collection<ID> ids);


    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity
     * @return the saved entity
     */
    @Transactional
    E save(E entity);


    /**
     * Saves all given entities.
     *
     * @param entities
     * @return the saved entities
     */
    @Transactional
    Collection<E> save(Collection<E> entities);


    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be null.
     * @return true if an entity with the given id exists, false otherwise
     */
    @Transactional(readOnly = true)
    Boolean exists(ID id);


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    @Transactional(readOnly = true)
    Long count();


    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be null.
     */
    @Transactional
    Integer delete(ID id);


    /**
     * Deletes the given entities.
     *
     * @param entities
     */
    //fun delete(entities: Collection<T>)
}
