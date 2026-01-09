package com.logandhillon.fptgame.scene.menu;


import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.component.DarkMenuButton;
import com.logandhillon.fptgame.entity.ui.component.InputBoxEntity;
import com.logandhillon.fptgame.entity.ui.component.LabeledModalEntity;


/**
 * The host game menu allows the user to input parameters needed for hosting a live server
 *
 * @author Jack Ross, Logan Dhillon
 */
public class HostGameContent implements MenuContent {
    private static final String DEFAULT_ROOM_NAME = "My new room";
    private static final int AJITESH_CONSTANT = 25;
    private final Entity[] entities;

    private final InputBoxEntity nameInput;

    /**
     * Creates a new main menu
     *
     * @param menu the {@link MenuHandler} responsible for switching active scenes.
     */
    public HostGameContent(MenuHandler menu) {
        nameInput = new InputBoxEntity(16, 47, 530, DEFAULT_ROOM_NAME, "ROOM NAME", AJITESH_CONSTANT);

        DarkMenuButton startButton = new DarkMenuButton("START GAME", 16, 337, 530, 50, () -> menu.createLobby(getRoomName()));


        entities = new Entity[]{startButton, new LabeledModalEntity(359, 128, 562, 464, "HOST NEW GAME", menu, nameInput, startButton)};

    }

    /**
     * Allows {@link MenuHandler} to access content for this menu
     *
     * @return entity list
     */
    @Override
    public Entity[] getEntities() {
        return entities;
    }

    /**
     * @return the content of the room name input field, or the default room name if it is blank.
     */
    public String getRoomName() {
        return nameInput.getInput().isBlank() ? DEFAULT_ROOM_NAME : nameInput.getInput();
    }
}

