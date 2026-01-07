package com.logandhillon.fptgame.engine;

import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.component.MenuButton;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A MenuController controls an ordered list of {@link MenuButton}s, providing keyboard controls and a simplified method
 * for entity registration.
 * <p>
 * MenuControllers are immutable— the buttons it shall control cannot change.
 *
 * @author Logan Dhillon
 */
public class MenuController extends Entity {
    private final MenuButton[] buttons;

    /** idx of currently active button; -1 means none */
    private int activeBtnIdx;

    /**
     * Creates a new immutable menu controller
     *
     * @param buttons ordered list of buttons from top to bottom, which will be used to sort navigation.
     */
    public MenuController(MenuButton... buttons) {
        super(0, 0); // pos doesn't matter, do whatever
        this.buttons = buttons;
        this.activeBtnIdx = -1;

        for (int i = 0; i < buttons.length; i++) {
            int idx = i; // effectively final instance of i

            // select this button if it's hovered by cursor
            buttons[i].setMouseEnterHandler(e -> {
                if (activeBtnIdx >= 0) buttons[activeBtnIdx].setActive(false, false);
                activeBtnIdx = idx;
            });

            // unselect all buttons when unselecting button with cursor
            buttons[i].setMouseLeaveHandler(e -> activeBtnIdx = -1);
        }
    }

    /**
     * Handles key presses from JavaFX, used to change the actively selected button or press it.
     */
    public void onKeyPressed(KeyEvent e) {
        // when W/UP/SHIFT+TAB pressed, go up (-1) in buttons
        if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W ||
            (e.isShiftDown() && e.getCode() == KeyCode.TAB)) {
            if (activeBtnIdx >= 0) buttons[activeBtnIdx].setActive(false, false); // deselect old active button (if any)
            activeBtnIdx = Math.max(0, activeBtnIdx - 1);   // decrement idx (no lower than 0)
            buttons[activeBtnIdx].setActive(true, false); // select new active button
        }
        // when S/DOWN/TAB pressed, go down (+1) in buttons
        else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S ||
                 (!e.isShiftDown() && e.getCode() == KeyCode.TAB)) {
            if (activeBtnIdx >= 0) buttons[activeBtnIdx].setActive(false, false); // deselect old active button (if any)
            activeBtnIdx = Math.min(buttons.length - 1, activeBtnIdx + 1); // increment idx (no higher than highest idx)
            buttons[activeBtnIdx].setActive(true, false);  // select new active button
        }
        // when ENTER/SPACE pressed, simulate a click on the active button
        else if ((e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) && activeBtnIdx >= 0) {
            buttons[activeBtnIdx].onPress();
        }
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {

    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * Attaches this entity and all buttons controlled by it to the parent.
     *
     * @param parent the parent that this object is now attached to.
     */
    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);

        // add all buttons to parent
        for (var btn: buttons) parent.addEntity(btn);

        // register key press event
        parent.addHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
    }
}
