package com.logandhillon.fptgame.resource;

import com.logandhillon.logangamelib.gfx.AnimationSequence;
import com.logandhillon.logangamelib.gfx.ParallaxBackground;
import com.logandhillon.logangamelib.gfx.TextureAtlas;
import com.logandhillon.logangamelib.resource.ImageResource;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * References to static textures
 *
 * @author Logan Dhillon
 */
public class Textures {
    public static final TextureAtlas PLAYER_IDLE     = new TextureAtlas("player/idle.png");
    public static final TextureAtlas PLAYER_RUN_LEFT = new TextureAtlas("player/run_left.png");
    public static final TextureAtlas PLAYER_RUN_RIGHT = new TextureAtlas("player/run.png");
    public static final TextureAtlas PLAYER_JUMP      = new TextureAtlas("player/jump.png");

    public static final AnimationSequence ANIM_PLAYER_IDLE = new AnimationSequence(PLAYER_IDLE, 2,
                                                                                   0, 0,
                                                                                   1, 0,
                                                                                   2, 0,
                                                                                   1, 0);

    public static final AnimationSequence ANIM_PLAYER_RUN_LEFT = new AnimationSequence(PLAYER_RUN_LEFT, 6,
                                                                                       5, 2,
                                                                                       4, 2,
                                                                                       3, 2,
                                                                                       2, 2,
                                                                                       1, 2,
                                                                                       0, 2);

    public static final AnimationSequence ANIM_PLAYER_RUN_RIGHT = new AnimationSequence(PLAYER_RUN_RIGHT, 6,
                                                                                        0, 2,
                                                                                        1, 2,
                                                                                        2, 2,
                                                                                        3, 2,
                                                                                        4, 2,
                                                                                        5, 2);

    public static final AnimationSequence ANIM_PLAYER_JUMP = new AnimationSequence(PLAYER_JUMP, 0,
                                                                                   3, 0);
    public static final int PLAYER_JUMP_FRAME = 0;

    /**
     * Generates a new instance of the ocean8 {@link ParallaxBackground}
     */
    public static ParallaxBackground ocean8() {
        return new ParallaxBackground(
                new ParallaxBackground.Layer("bg/ocean8/1.png", 10f),
                new ParallaxBackground.Layer("bg/ocean8/5.png", 5f), // moon is 5
                new ParallaxBackground.Layer("bg/ocean8/2.png", 25f),
                new ParallaxBackground.Layer("bg/ocean8/3.png", 50f),
                new ParallaxBackground.Layer("bg/ocean8/4.png", 80f));
    }

    public static final Image SETTINGS_ICON;
    public static final Image X_ICON;

    static {
        try (var res = new ImageResource("menuicons/cog.png")) {
            SETTINGS_ICON = res.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (var res = new ImageResource("menuicons/x.png")) {
            X_ICON = res.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
