package com.logandhillon.logangamelib.entity;

import javafx.scene.canvas.GraphicsContext;

/**
 * An extremely primitive {@link Entity} that only has a render loop. It can be used for more anonymous contexts, such
 * as creating an entity for only one use case.
 *
 * @author Logan Dhillon
 */
public abstract class Renderable extends Entity {
    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     */
    public Renderable(float x, float y) {
        super(x, y);
    }

    @Override
    protected abstract void onRender(GraphicsContext g, float x, float y);

    @Override
    public void onUpdate(float dt) {
    }

    @Override
    public void onDestroy() {
    }
}
