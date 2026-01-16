package com.logandhillon.logangamelib.entity.ui;

import javafx.scene.input.MouseEvent;

/**
 * A dynamic button is the same as a regular {@link ButtonEntity}, except that it has TWO styles: the default style, and
 * a style to show when the button is being hovered.
 *
 * @author Logan Dhillon
 */
public class DynamicButtonEntity extends ButtonEntity {
    private final Style defaultStyle;
    private final Style activeStyle;

    private boolean isActive;
    private boolean locked;

    /**
     * Creates a new dynamic button entity.
     *
     * @param label        the text to show on the button
     * @param w            width
     * @param h            height
     * @param onClick      the action that should happen when this button is clicked
     * @param defaultStyle how the button looks when it's NOT hovered
     * @param activeStyle  how the button looks when it IS hovered
     */
    public DynamicButtonEntity(String label, float x, float y, float w, float h, MouseEventHandler onClick,
                               Style defaultStyle, Style activeStyle) {
        super(label, x, y, w, h, onClick, defaultStyle);
        this.defaultStyle = defaultStyle;
        this.activeStyle = activeStyle;
    }

    /**
     * Sets the {@link DynamicButtonEntity#isActive} flag and updates the currently visible style of the button.
     */
    public void setActive(boolean isActive, boolean locked) {
        this.isActive = isActive;
        this.locked = locked;
        this.setStyle(isActive ? activeStyle : defaultStyle);
    }

    @Override
    public void onMouseEnter(MouseEvent e) {
        if (!this.locked) {
            this.setActive(true, false);
        }
        super.onMouseEnter(e); // call event handler after changing style
    }

    @Override
    public void onMouseLeave(MouseEvent e) {
        if (!this.locked) {
            this.setActive(false, false);
        }
        super.onMouseLeave(e); // call event handler after changing style
    }

    /**
     * @return if the mouse is currently in this button (if the button is active)
     */
    public boolean isActive() {
        return isActive;
    }
}
