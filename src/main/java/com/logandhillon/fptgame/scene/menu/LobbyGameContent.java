package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.ui.PlayerIconEntity;
import com.logandhillon.fptgame.entity.ui.component.MenuButton;
import com.logandhillon.fptgame.entity.ui.component.MenuModalEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.logangamelib.entity.Entity;
import com.logandhillon.logangamelib.entity.ui.TextEntity;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * The lobby game menu shows all users in a lobby and allots the host with special permissions to start the game
 *
 * @author Jack Ross, Logan Dhillon
 * @see PlayerIconEntity
 */
public class LobbyGameContent implements MenuContent {
    private static final Logger LOG         = LoggerContext.getContext().getLogger(LobbyGameContent.class);
    private static final Font   HEADER_FONT = Font.font(Fonts.TREMOLO, FontWeight.MEDIUM, 40);

    private static final int[][] PLAYER_ICON_ARGS = new int[][]{
            // iconX, iconY, colorIdx, textX, textY
            { 48, 143, 0, 32, 262 },
            { 215, 143, 1, 200, 262 } };

    private final Entity[]        entities;
    private final MenuModalEntity lobbyModal;
    private final String          roomName;
    private final MenuHandler     menu;
    private final boolean         isHosting;
    private final MenuButton      startButton;

    private boolean isStartingAllowed;

    /**
     * @param menu      the game manager responsible for switching active scenes.
     * @param roomName  the name of the lobby stated in {@link HostGameContent}
     * @param isHosting determines if the user is the host of the given lobby or not
     */
    public LobbyGameContent(MenuHandler menu, String roomName, boolean isHosting) {
        this.roomName = roomName;
        this.menu = menu;
        this.isHosting = isHosting;

        // shows different buttons at bottom depending on if the user is hosting
        startButton = new MenuButton(
                isHosting ? "WAITING FOR PLAYER..." : "WAITING FOR HOST...", 32, 640, 304, 48,
                () -> {
                    if (isStartingAllowed) menu.getGameHandler().startGame();
                });

        if (!isHosting) {
            startButton.setActive(false);
            startButton.setLocked(true);
            isStartingAllowed = false;
        }

        lobbyModal = new MenuModalEntity(
                0, 0, 442, GameHandler.CANVAS_HEIGHT, true, menu,
                startButton,
                new TextEntity.Builder(32, 66).setColor(Colors.ACTIVE)
                                              .setText(roomName.toUpperCase())
                                              .setFont(HEADER_FONT)
                                              .setBaseline(VPos.TOP)
                                              .build());

        // creates list of entities to be used by menu handler
        entities = new Entity[]{ lobbyModal };
    }

    /**
     * Adds a player to the list of players
     *
     * @param name   player name
     * @param isHost if the player is the host of this lobby
     */
    public void addPlayer(String name, boolean isHost) {
        LOG.info("Adding player \"{}\" (host={})", name, isHost);
        int[] args = PLAYER_ICON_ARGS[isHost ? 0 : 1];
        lobbyModal.addEntity(new PlayerIconEntity(args[0], args[1], args[2]));
        lobbyModal.addEntity(new TextEntity.Builder(args[3], args[4])
                                     .setColor(Colors.ACTIVE)
                                     .setText(name.toUpperCase())
                                     .setFontSize(18)
                                     .setBaseline(VPos.TOP).build());
        // if a guest joins, and this is the hosting lobby, unlock the start button
        if (!isHost && isHosting) {
            startButton.setLocked(false);
            isStartingAllowed = true;
            startButton.setText("START GAME");
        }
    }

    /**
     * Clears players from list of players
     */
    public void clearPlayers() {
        LOG.info("Clearing player list");
        // TODO: move player's name into player icon entity for this predicate to pass
        menu.clearEntities(true, PlayerIconEntity.class::isInstance);
        startButton.setFlags(false, true);
        isStartingAllowed = false;
        startButton.setText("WAITING FOR PLAYER...");
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

    public String getRoomName() {
        return roomName;
    }
}