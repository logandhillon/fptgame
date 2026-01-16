package com.logandhillon.logangamelib.entity;

import com.logandhillon.logangamelib.engine.GameScene;
import javafx.scene.canvas.GraphicsContext;

/**
 * A GameObject is very the layer below {@link Entity}, which is an independent game object that can be added to scenes.
 * Unlike its independent counterpart, GameObject cannot be rendered on its own (it cannot be added to a
 * {@link GameScene}).
 * <p>
 * A GameObject can only be rendered by other objects within the {@link com.logandhillon.fptgame.entity} package.
 *
 * @author Logan Dhillon
 * @see Entity
 */
public interface GameObject {
    /**
     * Called every tick for non-graphics-related updates (Entity lifecycle, etc.)
     *
     * @param dt the delta time: change in time (seconds) since the last frame
     */
    void onUpdate(float dt);

    /**
     * Called every tick to render the entity.
     *
     * @param g the graphical context to render to.
     *
     * @implNote do not implement this method to change how an {@link Entity} is rendered (unless you're making a
     * {@link GameObject}).
     */
    void render(GraphicsContext g, float x, float y);

    /**
     * Called when this entity is scheduled to be destroyed.
     *
     * @implNote This method should clean up the entity, so it's ready for destruction.
     */
    void onDestroy();
}
