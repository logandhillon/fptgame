package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.LobbyPlayerEntity;
import com.logandhillon.fptgame.entity.ui.component.DarkMenuButton;
import com.logandhillon.fptgame.entity.ui.component.LabeledModalEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * The lobby game menu shows all users in a lobby and allots the host with special permissions to start the
 * {@link com.logandhillon.fptgame.scene.MainGameScene}
 *
 * @author Jack Ross, Logan Dhillon
 * @see LobbyPlayerEntity
 */
public class LobbyGameScene implements MenuContent {
    private static final Logger LOG = LoggerContext.getContext().getLogger(LobbyGameScene.class);

    private final Entity[] entities;

    private static final Font LABEL_FONT = Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 18);
    private static final float ENTITY_GAP = 48;

    private final LabeledModalEntity lobbyModal;
    private final String roomName;
    private final MenuHandler menu;

    private float playerListDy;

    /**
     * @param menu       the game manager responsible for switching active scenes.
     * @param roomName  the name of the lobby stated in {@link HostGameScene}
     * @param isHosting determines if the user is the host of the given lobby or not
     */
    public LobbyGameScene(MenuHandler menu, String roomName, boolean isHosting) {
        this.roomName = roomName;
        this.menu = menu;
        // containers for each team
        PlayerContainer leftContainer = new PlayerContainer(16, 47, 257, 206);
        PlayerContainer rightContainer = new PlayerContainer(289, 47, 257, 206);

        // labels for each container
        ContainerLabel leftLabel = new ContainerLabel(16, 16, 1);
        ContainerLabel rightLabel = new ContainerLabel(289, 16, 2);

        // shows different buttons at bottom depending on if the user is hosting
        DarkMenuButton startButton = new DarkMenuButton(isHosting ? "START GAME" : "WAITING FOR HOST TO START...",
                16, 269, 530, 48, () -> {
            if (isHosting) menu.startGame();
            // don't do anything if not hosting (button is disabled)
        });

        if (!isHosting) {
            startButton.setActive(false, true);
        }

        lobbyModal = new LabeledModalEntity(
                359, 162, 562, 396, roomName, menu,
                leftContainer, rightContainer, leftLabel, rightLabel, startButton);

        // creates list of entities to be used by menu handler
        entities = new Entity[]{lobbyModal};
    }

    /**
     * Adds a player to the list of players
     *
     * @param name  player name
     * @param color player skin's color
     */
    public void addPlayer(String name, Color color) {
        LOG.info("Adding player \"{}\" with color {}", name, color.toString());

        var p = new LobbyPlayerEntity(color, name);
        p.setPosition(32, p.getY() + playerListDy + 128);
        playerListDy += ENTITY_GAP;
        lobbyModal.addEntity(p);
    }

    /**
     * Clears players from list of players
     */
    public void clearPlayers() {
        LOG.info("Clearing player list");
        this.menu.clearEntities(true, LobbyPlayerEntity.class::isInstance);
        playerListDy = 0;
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


    private static final class PlayerContainer extends Entity {

        private final float w;
        private final float h;

        /**
         * Creates an entity at the specified position.
         *
         * @param x x-position (from left)
         * @param y y-position (from top)
         */
        public PlayerContainer(float x, float y, float w, float h) {
            super(x, y);
            this.w = w;
            this.h = h;
        }

        @Override
        protected void onRender(GraphicsContext g, float x, float y) {
            // render container
            g.setFill(Colors.ACTIVE);
            g.fillRoundRect(x, y, w, h, 8, 8);
        }

        @Override
        public void onUpdate(float dt) {

        }

        @Override
        public void onDestroy() {

        }
    }

    private static final class ContainerLabel extends Entity {

        private final int teamNumber;

        /**
         * Creates an entity at the specified position.
         *
         * @param x x-position (from left)
         * @param y y-position (from top)
         */
        public ContainerLabel(float x, float y, int teamNumber) {
            super(x, y);
            this.teamNumber = teamNumber;
        }

        @Override
        protected void onRender(GraphicsContext g, float x, float y) {
            // set initial text variables
            g.setTextAlign(TextAlignment.LEFT);
            g.setTextBaseline(VPos.TOP);
            g.setFont(LABEL_FONT);
            g.setFill(Colors.FOREGROUND);

            // render label
            g.fillText("TEAM " + this.teamNumber, x, y);
        }

        @Override
        public void onUpdate(float dt) {

        }

        @Override
        public void onDestroy() {

        }
    }

    public String getRoomName() {
        return roomName;
    }
}

