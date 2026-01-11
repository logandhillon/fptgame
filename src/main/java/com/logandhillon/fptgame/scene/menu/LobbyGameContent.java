package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.LobbyPlayerEntity;
import com.logandhillon.fptgame.entity.ui.component.DarkMenuButton;
import com.logandhillon.fptgame.entity.ui.component.LabeledModalEntity;
import com.logandhillon.fptgame.entity.ui.component.MenuModalEntity;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.fptgame.resource.Textures;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * The lobby game menu shows all users in a lobby and allots the host with special permissions to start the game
 *
 * @author Jack Ross, Logan Dhillon
 * @see LobbyPlayerEntity
 */
public class LobbyGameContent implements MenuContent {
    private static final Logger LOG = LoggerContext.getContext().getLogger(LobbyGameContent.class);

    private final Entity[] entities;

    private static final Font HEADER_FONT = Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 40);


    private static final float ENTITY_GAP = 167.5f;

    private final MenuModalEntity lobbyModal;
    private final String          roomName;
    private final MenuHandler     menu;

    private float playerListDx;

    /**
     * @param menu      the game manager responsible for switching active scenes.
     * @param roomName  the name of the lobby stated in {@link HostGameContent}
     * @param isHosting determines if the user is the host of the given lobby or not
     */
    public LobbyGameContent(MenuHandler menu, String roomName, boolean isHosting) {
        this.roomName = roomName;
        this.menu = menu;

        // shows different buttons at bottom depending on if the user is hosting
        DarkMenuButton startButton = new DarkMenuButton(
                isHosting ? "START GAME" : "WAITING FOR HOST TO START...",
                32, 640, 304, 48, () -> {
            if (isHosting) menu.startGame();
            // don't do anything if not hosting (button is disabled)
        });

        if (!isHosting) {
            startButton.setActive(false, true);
        }

        lobbyModal = new MenuModalEntity(
                0, 0, 442, GameHandler.CANVAS_HEIGHT, true, menu, startButton, new PlayerIconEntity(48, 143, 0),
                new PlayerIconEntity(215, 143, 1), //TODO: Make this join only when the other player is in lobby (have fun logan ;) )
                new TextEntity.Builder(32, 66).setColor(Colors.ACTIVE)
                                              .setText(roomName::toUpperCase)
                                              .setFont(HEADER_FONT)
                                              .setBaseline(VPos.TOP)
                                              .build());

        // creates list of entities to be used by menu handler
        entities = new Entity[]{ lobbyModal };
    }

    /**
     * Adds a player to the list of players
     *
     * @param name  player name
     * @param color player skin's color
     */
    public void addPlayer(String name, Color color) {
        LOG.info("Adding player \"{}\" with color {}", name, color.toString());
        var p = new TextEntity.Builder(0 + playerListDx +  32, 262)
                .setColor(Colors.ACTIVE)
                .setText(name::toUpperCase)
                .setFontSize(18)
                .setBaseline(VPos.TOP).build();
        playerListDx += ENTITY_GAP;
        lobbyModal.addEntity(p);
    }

    /**
     * Clears players from list of players
     */
    public void clearPlayers() {
        LOG.info("Clearing player list");
        this.menu.clearEntities(true, LobbyPlayerEntity.class::isInstance);
        playerListDx = 0;
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

    private static class PlayerIconEntity extends Entity {

        private final int color;

        /**
         * Creates an entity at the specified position.
         *
         * @param x x-position (from left)
         * @param y y-position (from top)
         */
        public PlayerIconEntity(float x, float y, int color) {
            super(x, y);
            this.color = color;
        }

        @Override
        protected void onRender(GraphicsContext g, float x, float y) {
            g.save();
            g.beginPath();
            int radius = 49;
            g.arc(x + 33, y + 58, radius, radius, 0, 360);
            g.clip();
            g.setFill(Colors.ACTIVE_TRANS_2);
            g.fillArc(x - 16, 153, radius * 2, radius * 2, 0, 360, ArcType.ROUND);
            Textures.PLAYER_IDLE.draw(g, 0, 0, x, y, 66, 132, Colors.PLAYER_SKINS.get(color));
            g.restore();
        }

        @Override
        public void onUpdate(float dt) {

        }

        @Override
        public void onDestroy() {

        }
    }
}