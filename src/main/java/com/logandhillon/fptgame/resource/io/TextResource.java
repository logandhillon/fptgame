package com.logandhillon.fptgame.resource.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Loads a resource file as a UTF-8 {@link String}
 *
 * @author Logan Dhillon
 */
public class TextResource extends Resource<String> {
    public TextResource(String filename) throws FileNotFoundException {
        super(filename);
    }

    public String load() throws IOException {
        assert stream != null; // stream obviously isn't null bc resource will kill itself if it is
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
