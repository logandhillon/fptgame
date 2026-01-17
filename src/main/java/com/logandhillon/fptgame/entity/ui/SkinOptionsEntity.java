package com.logandhillon.fptgame.entity.ui;

import com.logandhillon.logangamelib.entity.Clickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

@Deprecated
public class SkinOptionsEntity extends Clickable {
    private final Color    color;
    private final Runnable onPress;
    private       boolean  clicked;

    /**
     * Creates a singular skin option entity.
     *
     * @param x       x-position (from left)
     * @param y       y-position (from top)
     * @param color   the color of the skin
     * @param onPress the event handler to run when this option is pressed
     */
    public SkinOptionsEntity(float x, float y, Color color, Runnable onPress) {
        super(x, y, 64, 64);
        this.color = color;
        this.onPress = onPress;
        this.clicked = false;
    }

    @Override
    public void onClick(MouseEvent e) {
        this.onPress();
    }

    public void onPress() {
        onPress.run();
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(this.color);
        g.fillRect(x, y, w, h);

        if (clicked) {
            g.setLineWidth(3);
            g.setStroke(this.color.brighter());
            g.strokeRect(x, y, w, h);
        }
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * Sets the size to 80px if big, 64px otherwise.
     *
     * @param isBig if the size should be big (i.e. if it is selected)
     */
    public void setSize(boolean isBig) {
        this.w = isBig ? 80 : 64;
        this.h = isBig ? 80 : 64;
    }

    public boolean isClicked() {
        return this.clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Color getColor() {
        return color;
    }
}
