package com.logandhillon.fptgame.resource;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Loads a resource from the /gfx/ folder as an {@link Image}
 *
 * @author Logan Dhillon
 */
public class ImageResource extends Resource<Image> {
    /**
     * Creates a new resource and opens an {@link InputStream} for it.
     *
     * @param path the relative path to the resource
     *
     * @throws FileNotFoundException if the file doesn't exist
     */
    public ImageResource(String path) throws FileNotFoundException {
        super("gfx/" + path);
    }

    @Override
    public Image read() {
        assert this.stream != null;
        return new Image(this.stream);
    }
}
