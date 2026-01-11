package com.logandhillon.fptgame.resource.io;

import javafx.scene.media.AudioClip;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Loads a resource file from the {@code audio/} folder as an {@link AudioClip}
 *
 * @author Logan Dhillon
 */
public class AudioResource extends Resource<AudioClip> {
    public AudioResource(String filename) throws FileNotFoundException {
        super("sound/" + filename);
    }

    public AudioClip load() throws IOException {
        assert this.url != null;
        return new AudioClip(this.url.toExternalForm());
    }
}
