package com.logandhillon.fptgame.resource;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * Contains constants for all {@link javafx.scene.paint.Paint} items (colors, gradients, etc.) that are to be used
 * throughout the game but require continuity.
 *
 * @author Logan Dhillon
 */
public final class Colors {
    public static final Color ACTIVE     = Color.WHITE;
    public static final Color ACTIVE_TRANS = Color.rgb(255, 255, 255, 0.5);
    public static final Color FOREGROUND       = Color.BLACK;
    public static final Color FOREGROUND_TRANS = Color.rgb(0, 0, 0, 0.4);

    public static final Color GENERIC_BG = Color.valueOf("#D2D1D1");

    public static final Color BUTTON_NORMAL = Color.rgb(207, 209, 235);
    public static final Color BUTTON_HOVER = Color.rgb(75, 150, 249);

    /**
     * The color of the player skin, indexed by the order they appear on the main menu.
     */
    public static final List<Color> PLAYER_SKINS = List.of(
            // red, blue, turquoise, yellow
            Color.valueOf("#E92727"), Color.valueOf("424CDF"), Color.valueOf("#27E9A8"), Color.valueOf("#E5E927")
    );
}
