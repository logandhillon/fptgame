package com.logandhillon.logangamelib.entity.ui;

import com.logandhillon.logangamelib.entity.Clickable;
import javafx.scene.input.MouseEvent;

/**
 * A {@link UIObserverEntity} is a type of UI entity that extends {@link Clickable}, but subscribes to ALL events in the
 * {@link com.logandhillon.logangamelib.engine.UIScene}.
 * <p>
 * This entity has access to general mouse movement events, mouse release anywhere on the screen, etc.
 *
 * @author Logan Dhillon
 * @apiNote as this entity subscribes to ALL UI events, it should be used sparingly.
 */
public abstract class UIObserverEntity extends Clickable {
    /**
     * Creates an entity at the specified position.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of this element's hitbox
     * @param h width of this element's hitbox
     */
    public UIObserverEntity(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    /**
     * Called when the mouse releases a click anywhere on the screen
     *
     * @param e the mouse event provided by JavaFX
     */
    public void onRelease(MouseEvent e) {}

    /**
     * Called when the mouse moves anywhere on the screen
     *
     * @param e the mouse event provided by JavaFX
     */
    public void onMouseMove(MouseEvent e) {}
}
