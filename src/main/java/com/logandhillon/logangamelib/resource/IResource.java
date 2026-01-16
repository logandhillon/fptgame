package com.logandhillon.logangamelib.resource;

import java.io.IOException;

/**
 * Interface for a resource loader that provides a method for loading the resource into the type {@code <T>}
 *
 * @param <T> the type that this loader returns programmatically (e.g. text loader is {@link String}
 *
 * @author Logan Dhillon
 */
public interface IResource<T> {
    /**
     * Loads this resource object to the specified type {@code <T>}
     *
     * @return loaded programmatic object
     *
     * @throws IOException failed to load from classpath or disk
     */
    T load() throws IOException;
}
