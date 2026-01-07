package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * A darker version of the {@link MenuButton} for contrast over modals.
 *
 * @author Logan Dhillon, Jack Ross
 * @see DynamicButtonEntity
 */
public class DarkMenuButton extends DynamicButtonEntity {
    private static final ButtonEntity.Style DEFAULT_STYLE = new ButtonEntity.Style(
            Colors.FOREGROUND, Colors.ACTIVE, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.PIXELIFY_SANS, FontWeight.MEDIUM, 20));
    private static final ButtonEntity.Style ACTIVE_STYLE  = new ButtonEntity.Style(
            Colors.FOREGROUND, Colors.PRIMARY, ButtonEntity.Variant.SOLID, true, Font.font(Fonts.PIXELIFY_SANS, FontWeight.MEDIUM, 21));

    private final Runnable pressHandler;

    /**
     * Creates a new dynamic button entity using the preset styles for menu buttons.
     *
     * @param label   the text to show on the button
     * @param w       width
     * @param h       height
     * @param onPress the action that should happen when this button is clicked
     */
    public DarkMenuButton(String label, float x, float y, float w, float h, Runnable onPress) {
        super(label.toUpperCase(), x, y, w, h, e -> onPress.run(), DEFAULT_STYLE, ACTIVE_STYLE);
        this.pressHandler = onPress;
    }

    /**
     * Runs the {@code onPress} press handler.
     */
    public void onPress() {
        pressHandler.run();
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        super.onRender(g, x, y);
        // assuming the fill and font have already been set from super#onRender

        if (this.isActive()) {
            // left arrow
            g.setTextAlign(TextAlignment.LEFT);
            g.fillText(">", x + 8, y + h / 2);

            // right arrow
            g.setTextAlign(TextAlignment.RIGHT);
            g.fillText("<", x + w - 8, y + h / 2);
        }
    }
}