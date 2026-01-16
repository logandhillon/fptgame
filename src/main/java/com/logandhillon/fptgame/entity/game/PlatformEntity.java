package com.logandhillon.fptgame.entity.game;

import com.logandhillon.logangamelib.entity.physics.CollisionEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Logan Dhillon
 */
public class PlatformEntity extends CollisionEntity {
    /**
     * Creates a collidable entity at the specified position with the specified hitbox
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of hitbox
     * @param h height of hitbox
     */
    public PlatformEntity(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Color.BLACK);
        g.fillRect(x, y, w, h);
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }
}
