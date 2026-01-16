package com.logandhillon.logangamelib.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * An abstract resource loader
 *
 * @param <T> the type that this loader returns programmatically (e.g. text loader is {@link String}
 *
 * @author Logan Dhillon
 */
public abstract class Resource<T> implements AutoCloseable, IResource<T> {
    protected final InputStream stream;
    protected final URL         url;

    /**
     * Creates a new resource and opens an {@link InputStream} for it.
     * <p>
     * Also defines the 'stream' and 'url' class attributes.
     *
     * @param path the relative path to the resource
     *
     * @throws FileNotFoundException if the file doesn't exist
     */
    public Resource(String path) throws FileNotFoundException {
        this.stream = Resource.class.getResourceAsStream("/" + path);
        this.url = Resource.class.getResource("/" + path);
        if (stream == null) throw new FileNotFoundException("Resource '" + path + "' not found");
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
