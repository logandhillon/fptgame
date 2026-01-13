package com.logandhillon.fptgame.resource;

import com.logandhillon.fptgame.gfx.AnimationSequence;
import com.logandhillon.fptgame.gfx.ParallaxBackground;

/**
 * References to static textures
 *
 * @author Logan Dhillon
 */
public class Textures {
    public static final TextureAtlas PLAYER_IDLE = new TextureAtlas("player/idle.png");
    public static final TextureAtlas PLAYER_WALK = new TextureAtlas("player/walk.png");
    public static final TextureAtlas PLAYER_RUN  = new TextureAtlas("player/run.png");
    public static final TextureAtlas PLAYER_JUMP = new TextureAtlas("player/jump.png");

    public static final AnimationSequence ANIM_PLAYER_IDLE = new AnimationSequence(PLAYER_IDLE, 2,
                                                                                   0, 0,
                                                                                   1, 0,
                                                                                   2, 0,
                                                                                   1, 0);

    // TODO: fine tune animations (i have no idea if these frames are correct lmao)
    public static final AnimationSequence ANIM_PLAYER_WALK = new AnimationSequence(PLAYER_WALK, 6,
                                                                                   0, 2,
                                                                                   1, 2,
                                                                                   2, 2,
                                                                                   3, 2);
    public static final AnimationSequence ANIM_PLAYER_RUN  = new AnimationSequence(PLAYER_RUN, 6,
                                                                                   0, 0,
                                                                                   1, 0,
                                                                                   2, 0,
                                                                                   3, 0);
    public static final AnimationSequence ANIM_PLAYER_JUMP = new AnimationSequence(PLAYER_JUMP, 6,
                                                                                   0, 0,
                                                                                   1, 0,
                                                                                   2, 0,
                                                                                   3, 0);

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
}
