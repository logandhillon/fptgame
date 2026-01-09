package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.ServerEntryEntity;
import com.logandhillon.fptgame.entity.ui.component.DarkMenuButton;
import com.logandhillon.fptgame.entity.ui.component.InputBoxEntity;
import com.logandhillon.fptgame.entity.ui.component.LabeledModalEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The join game menu allows users to join existing servers through manual IP Address searching or local server
 * discovery. When the user has joined a game, they will be transported to the {@link LobbyGameContent}
 *
 * @author Jack Ross, Logan Dhillon
 */
public class JoinGameContent implements MenuContent {
    private static final Logger LOG = LoggerContext.getContext().getLogger(JoinGameContent.class);

    private final Entity[] entities;

    private static final Font LABEL_FONT = Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 18);
    private static final int ENTITY_GAP = 53;

    private final LabeledModalEntity joinModal;
    private final ServerEntryEntity[] serverButtons = new ServerEntryEntity[4];

    private int scrollServerIndex;
    private int currentServerIndex;
    private int rawCurrentServerIndex;
    private String selectedServerAddr; // the addr of the selected server in Discovery
    private List<ServerEntry> serverList = new ArrayList<>();

    /**
     * @param menu the {@link MenuHandler} responsible for switching active menus.
     */
    public JoinGameContent(MenuHandler menu, JoinGameHandler onJoin) {
        // rect in background for server list
        Entity serverListRect = new Entity(16, 152) {
            @Override
            protected void onRender(GraphicsContext g, float x, float y) {
                g.setFill(Colors.ACTIVE);
                g.fillRect(x, y, 530, 228);
            }

            @Override
            public void onUpdate(float dt) {
            }

            @Override
            public void onDestroy() {
            }
        };

        // label for server list
        Entity serverListLabel = new Entity(16, 121) {
            @Override
            protected void onRender(GraphicsContext g, float x, float y) {
                g.setTextAlign(TextAlignment.LEFT);
                g.setTextBaseline(VPos.TOP);
                g.setFont(LABEL_FONT);
                g.setFill(Colors.FOREGROUND);

                // render label
                g.fillText("FIND A LOCAL SERVER", x, y);
            }

            @Override
            public void onUpdate(float dt) {

            }

            @Override
            public void onDestroy() {

            }
        };

        // join server input field
        InputBoxEntity joinServer = new InputBoxEntity(16, 47, 379, "ex. 192.168.0.1", "JOIN A SERVER DIRECTLY", 39);

        // join button (direct)
        DarkMenuButton joinDirectButton = new DarkMenuButton("JOIN", 407, 47, 139, 50, () -> {
            LOG.info("Attempting to join {} via manual input", joinServer.getInput());
            onJoin.handleJoin(joinServer.getInput());
        });

        // join button (discovery)
        DarkMenuButton joinDiscoverButton = new DarkMenuButton("JOIN", 16, 396, 530, 48, () -> {
            if (selectedServerAddr == null) {
                LOG.warn("Tried to join discovered server, but no server was selected. Ignoring");
                return;
            }
            LOG.info("Attempting to join {} via discovery", joinServer.getInput());
            onJoin.handleJoin(selectedServerAddr);
        });

        joinModal = new LabeledModalEntity(
                359, 99, 562, 523, "JOIN A GAME", menu, serverListRect, serverListLabel, joinServer, joinDirectButton,
                joinDiscoverButton);

        // creates list of entities to be used by menu handler
        entities = new Entity[]{joinModal};

        // create event handler that uses the event and the array of buttons
        menu.addHandler(KeyEvent.KEY_PRESSED, e -> onKeyPressed(e, serverButtons));
    }

    @Override
    public void onShow() {
        // attach server buttons (via modal) only once content is shown (so this content has a parent)
        // XXX: this should not be in the constructor (for reasons above)
        for (int i = 0; i < serverButtons.length; i++) {
            // populate with dummy values and hide them
            serverButtons[i] = new ServerEntryEntity(32, 231 + (ENTITY_GAP * i), 498, 37,
                    "...", "...", () -> {
            });
            serverButtons[i].hidden = true;
            LOG.debug("Creating (hidden) server button for this modal. {}/{}", i + 1, serverButtons.length);
            joinModal.addEntity(serverButtons[i]);
        }
    }

    /**
     * Clears the UI discovered server list and repopulates it with the values of {@link JoinGameContent#serverList}
     */
    private void updateServerList() {
        // repopulate items and add to list
        AtomicInteger currentServer = new AtomicInteger();

        for (int i = 0; i < serverButtons.length; i++) {
            if (i >= serverList.size()) {
                serverButtons[i].hidden = true;
                continue;
            }

            // get it immediately so it doesn't change
            var entry = serverList.get(i);
            int finalI = i;

            // set new server button with available information
            LOG.debug("Preparing server button with data: {{}, {}}", entry.name, entry.address);
            serverButtons[i].setData(entry);
            serverButtons[i].setOnClick(() -> {
                // runnable (runs on click)

                // highlight button
                serverButtons[finalI].setActive(true, true);
                currentServer.set(finalI);

                currentServerIndex = finalI;
                rawCurrentServerIndex = finalI;
                selectedServerAddr = entry.address;

                // reset button highlight for non-clicked buttons
                for (int j = 0; j < serverButtons.length; j++) {
                    if (currentServer.get() != j) {
                        serverButtons[j].setActive(false, false);
                    }
                }
            });
            serverButtons[i].hidden = false;
        }
    }

    /**
     * Replaces the current server list with a new set of them
     *
     * @param newList list of all discovered servers
     */
    public void setDiscoveredServers(List<ServerEntry> newList) {
        serverList = newList;
        updateServerList();
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
     * An entry in the server list of the join game screen.
     *
     * @param name    name of the server/room
     * @param address FQDN or IP address of server
     */
    public record ServerEntry(String name, String address) {
    }

    /**
     * @param e       any key event registered by javafx
     * @param entries list of buttons on screen
     */
    private void onKeyPressed(KeyEvent e, ServerEntryEntity[] entries) {

        if (e.getCode() != KeyCode.UP && e.getCode() != KeyCode.DOWN) return;

        // increment/decrement the 4 shown servers
        if (e.getCode() == KeyCode.UP) {
            if (scrollServerIndex > 0) {
                rawCurrentServerIndex++;
                // un-highlight all buttons
                for (ServerEntryEntity entry : entries) {
                    entry.setActive(false, false);
                }
                if (currentServerIndex < entries.length - 1 && rawCurrentServerIndex > 0) {
                    currentServerIndex++;
                    // re-highlight button if it isn't still off-screen
                    entries[currentServerIndex].setActive(true, true);
                }

                if (currentServerIndex == 0) {
                    if (rawCurrentServerIndex < -1) {
                        // un-highlight all buttons if the selected button is not in the array
                        for (ServerEntryEntity entry : entries) {
                            entry.setActive(false, false);
                        }
                    }
                    // if the button was put back in the array by moving up, put it at the start
                    if (rawCurrentServerIndex > -1) {
                        currentServerIndex = 0;
                        entries[0].setActive(true, true);
                    }
                }
                // increments entire list of shown servers
                scrollServerIndex--;
            }
        }
        if (e.getCode() == KeyCode.DOWN) {
            if (scrollServerIndex < serverList.toArray().length - entries.length) {
                // opposite to KeyCode.UP, the index of the current button must decrease when down arrow is pressed
                rawCurrentServerIndex--;
                for (ServerEntryEntity entry : entries) {
                    entry.setActive(false, false);
                }

                if (currentServerIndex > 0 && rawCurrentServerIndex < entries.length - 1) {
                    currentServerIndex--;

                    entries[currentServerIndex].setActive(true, true);
                }
                if (currentServerIndex == entries.length - 1) {
                    if (rawCurrentServerIndex > entries.length) {
                        for (ServerEntryEntity entry : entries) {
                            entry.setActive(false, false);
                        }
                    }
                    if (rawCurrentServerIndex < entries.length) {
                        currentServerIndex = entries.length - 1;
                        entries[entries.length - 1].setActive(true, true);
                    }
                }
                // decrements entire list of shown servers
                scrollServerIndex++;
            }
        }

        // now that index has changed, re-populate the server list
        for (int i = 0; i < entries.length; i++) {
            entries[i].setData(serverList.get(i + scrollServerIndex));
        }
    }

    public interface JoinGameHandler {
        void handleJoin(String serverAddress);
    }
}
