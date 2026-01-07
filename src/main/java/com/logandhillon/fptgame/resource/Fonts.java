package com.logandhillon.fptgame.resource;

import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Loads and manages all font families. The constants in this class are font families, which can be used with JavaFX's
 * {@link Font#font}.
 *
 * @author Logan Dhillon
 */
public class Fonts {
    // root path of all font resources
    private static final String ROOT = "/font/";

    public static final String PIXELIFY_SANS = load("PixelifySans", "PixelifySans-VariableFont_wght.ttf");

    /**
     * Loads a font into the runtime and returns the name of the font family.
     *
     * @param parent the name of the parent folder containing all the font files
     * @param files  the file names of the fonts to load. these fonts should be of the same family.
     *
     * @return the name of the <b>FIRST-LOADED</b> font family.
     *
     * @throws FontNotFoundException if the font cannot be found
     * @apiNote while this function will load ALL font files passed in, only the first one to load will return its font
     * family name.
     */
    private static String load(String parent, String... files) throws FontNotFoundException {
        String folder = ROOT + parent + "/"; String family = null;

        for (String file: files) {
            String path = folder + file;

            try (InputStream font = Fonts.class.getResourceAsStream(path)) {
                if (font == null) throw new FontNotFoundException(path); // font didn't load? throw error
                if (family == null) family = Font.loadFont(font, 0).getFamily(); // no family yet? define it
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (family == null) throw new FontNotFoundException(files); return family;
    }

    private static class FontNotFoundException extends RuntimeException {
        /**
         * Exception if an individual font resource could not be found
         *
         * @param path the tried path of the font
         */
        public FontNotFoundException(String path) {
            super("Font resource at '" + path + "' does not exist or could not be found");
        }

        /**
         * Exception if a font could not be found within an array of multiple resources.
         *
         * @param files array of all tried file names
         */
        public FontNotFoundException(String... files) {
            super("No fonts could be found, tried: '" + Arrays.toString(files));
        }
    }
}
