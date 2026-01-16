package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.entity.Entity;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import com.logandhillon.logangamelib.entity.ui.ModalEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The modal displayed on the left side in every menu screen.
 *
 * @apiNote This cannot be moved as the x-y points are fixed
 * @author Jack Ross
 */
public class MenuModalEntity extends ModalEntity {
    private static final float MARGIN = 32;
    private final boolean back;
    private final MenuHandler menu;
    private final double[] xPoints;
    private final double[] yPoints;

    /**
     * Creates an entity at the specified position. All entities passed to this modal will be translated such that (0, 0) is
     * the top-left corner of this modal.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of modal
     * @param h height of modal
     * @param back determines if a back button is needed
     */
    public MenuModalEntity(float x, float y, float w, float h, boolean back, MenuHandler menu, Entity... entities) {
        super(x, y, w, h, entities);
        this.back = back;
        this.menu = menu;
        xPoints = new double[]{x, x + w, x + w - 75, x}; // offset polygon by 75 pixels
        yPoints = new double[]{y, y, y + h, y + h};
    }

    /**
     * Renders the polygon modal used in menu screens
     *
     * @param g the graphical context to render to.
     * @param x the x position to render the entity at
     * @param y the y position to render the entity at
     *
     */
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Color.rgb(0, 0, 0, 0.4));
        g.fillPolygon(xPoints, yPoints, 4);
    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        // add back button AFTER moving the other entities
        if (this.back) {
            parent.addEntity(new BackButtonEntity(x + MARGIN, y + MARGIN, menu));
        }
    }

    @Override
    public void setPosition(float x, float y) {
        throw new IllegalStateException("MenuModalEntity cannot be moved");
    }

    @Override
    public void translate(float x, float y) {
        throw new IllegalStateException("MenuModalEntity cannot be moved");
    }
}
