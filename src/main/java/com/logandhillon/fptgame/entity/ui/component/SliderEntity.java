package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.logangamelib.entity.Clickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.ArcType;

public class SliderEntity extends Clickable {
    private final int BACKBONE_DIAMETER = 6;
    private final int CIRCLE_DIAMETER = 20;
    private float cx;
    private float startx;
    private boolean stopDrag;

    /**
     * Creates slider at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     */
    public SliderEntity(float x, float y, float w, float h, float cx) {
        super(x, y, w, h);
        this.cx = cx;
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Colors.ACTIVE_TRANS_50);
        g.fillRoundRect(x, y, w, h, BACKBONE_DIAMETER, BACKBONE_DIAMETER);
        g.setFill(Colors.BUTTON_HOVER);
        g.fillRoundRect(x, y, cx - x, h, BACKBONE_DIAMETER, BACKBONE_DIAMETER);
        g.setFill(Colors.SLIDER_HEAD);
        g.fillArc(cx, y - BACKBONE_DIAMETER, CIRCLE_DIAMETER, CIRCLE_DIAMETER, 0, 360, ArcType.ROUND);
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {}

    @Override
    public void onClick(MouseEvent e) {
        cx = (float)e.getX();
        stopDrag = false;
    }

    @Override
    public void onDrag(MouseEvent e) {
        if (stopDrag) return;
        cx = (float)e.getX();
        onDrag(e);
    }
    @Override
    public void onDrop(MouseEvent e) {
        stopDrag = true;
    }

    public float getVolume() {
        return (cx - x + (CIRCLE_DIAMETER / 2f)) / w;
    }
}
