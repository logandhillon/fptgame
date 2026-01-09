package com.logandhillon.fptgame.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * A {@link AutoCloseable} resource manager that handles streaming IO from disk to the application for engine-layer
 * management.
 *
 * @author Logan Dhillon
 */
public class Resource implements AutoCloseable {
    private final InputStream stream;

    public Resource(String path) throws FileNotFoundException {
        this.stream = Resource.class.getResourceAsStream("/" + path);
        if (stream == null) throw new FileNotFoundException("Resource '" + path + "' not found");
    }

    public String readString() throws IOException {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
