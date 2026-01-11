package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.component.*;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The host game menu allows the user to input parameters needed for hosting a live server
 *
 * @author Jack Ross, Logan Dhillon
 */
public class HostGameContent implements MenuContent {
    private static final String   DEFAULT_ROOM_NAME = "My new room";
    private static final String   header            = "Host a New Game";
    private static final Font     HEADER_FONT       = Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 32);
    private final        Entity[] entities;

    private final InputBoxEntity nameInput;

    /**
     * Creates a new main menu
     *
     * @param menu the {@link MenuHandler} responsible for switching active scenes.
     */
    public HostGameContent(MenuHandler menu) {
        nameInput = new InputBoxEntity(32, 189, 327, DEFAULT_ROOM_NAME, "ROOM NAME", 16);

        DarkMenuButton startButton = new DarkMenuButton(
                "START GAME", 32, 640, 304, 48, () -> menu.createLobby(getRoomName()));

        entities = new Entity[]{ new MenuModalEntity(
                0, 0, 442, GameHandler.CANVAS_HEIGHT, true, menu, nameInput, startButton),
                                 new TextEntity.Builder(32, 66)
                                         .setColor(Colors.ACTIVE)
                                         .setText(header::toUpperCase)
                                         .setFont(HEADER_FONT)
                                         .setBaseline(VPos.TOP)
                                         .build()
                , startButton };
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

