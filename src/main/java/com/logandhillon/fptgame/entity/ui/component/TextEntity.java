package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.resource.Colors;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * A text entity is a highly-customizable text display.
 *
 * @author Logan Dhillon
 */
public class TextEntity extends Entity {
    private final String        text;
    private final Font          font;
    private final Color         color;
    private final TextAlignment align;
    private final VPos          baseline;

    /**
     * Creates a text entity.
     *
     * @param text     the content of the text to render
     * @param font     the font of the text to render
     * @param color    the color of the text
     * @param align    the horizontal alignment
     * @param baseline the vertical alignment
     * @param x        x-position (from left)
     * @param y        y-position (from top)
     */
    public TextEntity(String text, Font font, Color color, TextAlignment align, VPos baseline, float x, float y) {
        super(x, y);
        this.text = text;
        this.font = font;
        this.color = color;
        this.align = align;
        this.baseline = baseline;
    }

    public TextEntity(String text, Font font, TextAlignment align, VPos baseline, float x, float y) {
        this(text, font, Colors.FOREGROUND, align, baseline, x, y);
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(color);
        g.setFont(font);
        g.setTextAlign(align);
        g.setTextBaseline(baseline);
        g.fillText(text, x, y);
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }
}
