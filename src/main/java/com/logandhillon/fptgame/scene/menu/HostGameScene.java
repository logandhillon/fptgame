package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.ui.component.DarkMenuButton;
import com.logandhillon.fptgame.entity.ui.component.InputBoxEntity;
import com.logandhillon.fptgame.entity.ui.component.LabeledModalEntity;
import com.logandhillon.fptgame.resource.Colors;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * The host game menu allows the user to input parameters needed for hosting a live server
 *
 * @author Jack Ross, Logan Dhillon
 */
public class HostGameScene extends UIScene {
    private static final String DEFAULT_ROOM_NAME = "My new room";
    private static final int    AJITESH_CONSTANT  = 25;

    private final InputBoxEntity nameInput;

    /**
     * Creates a new main menu
     *
     * @param mgr the game manager responsible for switching active scenes.
     */
    public HostGameScene(GameHandler mgr) {
        nameInput = new InputBoxEntity(16, 47, 530, DEFAULT_ROOM_NAME, "ROOM NAME", AJITESH_CONSTANT);

        DarkMenuButton startButton = new DarkMenuButton("START GAME", 16, 337, 530, 50,
                                                        () -> mgr.createLobby(getRoomName()));

        // create background modal
        addEntity(new LabeledModalEntity(359, 128, 562, 464, "HOST NEW GAME", mgr, nameInput, startButton));
    }

    @Override
    protected void render(GraphicsContext g) {
        g.setFill(Colors.GENERIC_BG);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all entities
        super.render(g);
    }

    /**
     * @return the content of the room name input field, or the default room name if it is blank.
     */
    public String getRoomName() {
        return nameInput.getInput().isBlank() ? DEFAULT_ROOM_NAME : nameInput.getInput();
    }
}
