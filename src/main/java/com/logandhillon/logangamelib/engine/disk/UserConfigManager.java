package com.logandhillon.logangamelib.engine.disk;

import com.logandhillon.fptgame.networking.proto.ConfigProto;
import com.logandhillon.fptgame.networking.proto.ConfigProto.UserConfig;
import com.logandhillon.fptgame.resource.Colors;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Handles (de)serialization of save game data and (un)loading said data to the disk.
 *
 * @author Logan Dhillon
 */
public class UserConfigManager {
    private static final Logger LOG  = LoggerContext.getContext().getLogger(UserConfigManager.class);
    private static final File   FILE = new File("logangamelib.dat");

    private static final UserConfig DEFAULT_CONFIG = ConfigProto.UserConfig.newBuilder()
                                                                           .build();

    /**
     * Saves the provided {@link UserConfig} to the disk.
     *
     * @return user config that is now saved to disk
     *
     * @throws RuntimeException if the user config cannot be saved to disk
     */
    public static UserConfig save(UserConfig config) {
        try (FileOutputStream file = new FileOutputStream(FILE)) {
            if (FILE.getParent() != null) {
                LOG.warn("Parent directory for user config file doesn't exist, creating folder(s).");
                new File(FILE.getParent()).mkdirs();
            }

            LOG.info("Writing user configuration to {}", FILE.getAbsolutePath());
            config.writeTo(file);
            return config;
        } catch (IOException e) {
            LOG.error("Failed to save user configuration to {}", FILE.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to load the {@link UserConfig} from disk, returning the {@link UserConfigManager#DEFAULT_CONFIG} if it (a)
     * fails or (b) doesn't exist.
     *
     * @return user config from disk
     */
    public static UserConfig load() {
        // if there is no save file, save the default and return
        if (!FILE.exists()) {
            LOG.warn("Saved user config doesn't exist, saving default config to disk");
            save(DEFAULT_CONFIG);
            return DEFAULT_CONFIG;
        }

        try (FileInputStream file = new FileInputStream(FILE)) {
            var c = UserConfig.parseFrom(file);
            LOG.info("Successfully loaded user config for '{}' from disk", c.getName());
            return c;
        } catch (IOException e) {
            LOG.error("Failed to load user configuration from {}", FILE.getAbsolutePath(), e);
            return DEFAULT_CONFIG;
        }
    }

    /**
     * Updates only the fields specified and saves the resulting config.
     *
     * @param partial the partial values, whatever is set here will be updated, otherwise it will remain the same.
     */
    public static UserConfig update(UserConfig current, UserConfig partial) {
        LOG.debug("Updating user config");
        UserConfig.Builder builder = current.toBuilder();

        if (!partial.getName().isEmpty()) {
            LOG.info("Setting name to {}", partial.getName());
            builder.setName(partial.getName());
        }

        // Only override if the field is explicitly set (non-default)
        if (partial.hasColorIdx()) {
            LOG.info("Setting color to index {}", partial.getColorIdx().getValue());
            builder.setColorIdx(partial.getColorIdx());
        }

        // Build and save
        UserConfig merged = builder.build();
        return save(merged);
    }

    /**
     * Pure method to get the color index from user config and parse it into a {@link Color}
     *
     * @param config the user config to get the color idx from
     *
     * @return the javafx color
     */
    public static Color parseColor(UserConfig config) {
        return Colors.PLAYER_SKINS.get(config.getColorIdx().getValue());
    }
}
