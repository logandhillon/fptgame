package com.logandhillon.fptgame.resource;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.logangamelib.resource.AudioResource;
import com.logandhillon.logangamelib.resource.ResourceLoader;
import javafx.scene.media.AudioClip;

/**
 * @author Logan Dhillon
 */
public class Sounds {
    public static final AudioClip UI_CLICK = ResourceLoader.loadSafe(AudioResource.class, "ui/click.wav");
    public static final AudioClip UI_FAIL  = ResourceLoader.loadSafe(AudioResource.class, "ui/fail.wav");

    public static final AudioClip GAME_RESTART = ResourceLoader.loadSafe(AudioResource.class, "game/restart.wav");
    public static final AudioClip GAME_RESPAWN = ResourceLoader.loadSafe(AudioResource.class, "game/respawn.wav");
    public static final AudioClip GAME_STEP    = ResourceLoader.loadSafe(AudioResource.class, "game/step.wav");
    public static final AudioClip GAME_JUMP    = ResourceLoader.loadSafe(AudioResource.class, "game/jump.wav");
    public static final AudioClip GAME_LAND    = ResourceLoader.loadSafe(AudioResource.class, "game/land.wav");

    private static float musicVol;
    private static float sfxVol;

    /**
     * Recalculates volumes from {@link GameHandler#getUserConfig()}
     */
    public static void calcVolume() {
        var c = GameHandler.getUserConfig();
        musicVol = c.getMasterVolume() * c.getMusicVolume();
        sfxVol = c.getMasterVolume() * c.getSfxVolume();
    }

    /**
     * Plays a specified {@link AudioClip} with the volume in the user's config.
     *
     * @param sound sound clip to play
     */
    public static void playSfx(AudioClip sound) {
        sound.play(sfxVol);
    }

    /**
     * Plays a specified {@link AudioClip} with the volume in the user's config.
     *
     * @param sound music clip to play
     */
    public static void playMusic(AudioClip sound) {
        sound.play(musicVol);
    }
}
