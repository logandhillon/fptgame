package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * A stylized version of the {@link DynamicButtonEntity} made for in-game contexts.
 *
 * @author Logan Dhillon
 */
public class GameButton extends DynamicButtonEntity {
    private static final ButtonEntity.Style DEFAULT_STYLE = new ButtonEntity.Style(
            Colors.FOREGROUND, Colors.BUTTON_NORMAL, Variant.OUTLINE, true, Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 16));
    private static final ButtonEntity.Style ACTIVE_STYLE  = new ButtonEntity.Style(
            Colors.FOREGROUND, Colors.BUTTON_HOVER, Variant.OUTLINE, true, Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 17));

    /**
     * Creates a new dynamic button entity using the preset styles for menu buttons.
     *
     * @param label   the text to show on the button
     * @param w       width
     * @param h       height
     * @param onPress the action that should happen when this button is clicked
     */
    public GameButton(String label, float x, float y, float w, float h, Runnable onPress) {
        super(label.toUpperCase(), x, y, w, h, e -> onPress.run(), DEFAULT_STYLE, ACTIVE_STYLE);
    }
}
