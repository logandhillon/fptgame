package com.logandhillon.fptgame;

import com.logandhillon.fptgame.networking.GameClient;
import com.logandhillon.fptgame.networking.GamePacket;
import com.logandhillon.fptgame.networking.GameServer;
import com.logandhillon.fptgame.networking.ServerDiscoverer;
import com.logandhillon.fptgame.networking.proto.ConfigProto;
import com.logandhillon.fptgame.scene.DebugGameScene;
import com.logandhillon.fptgame.scene.component.MenuAlertScene;
import com.logandhillon.fptgame.scene.menu.JoinGameContent;
import com.logandhillon.fptgame.scene.menu.LobbyGameContent;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import com.logandhillon.logangamelib.engine.GameEngine;
import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.engine.disk.UserConfigManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.IOException;
import java.net.ConnectException;

public class GameHandler extends Application {
    private static final Logger LOG               = LoggerContext.getContext().getLogger(GameHandler.class);
    public static final  String GAME_NAME         = "FPTGAME TBD";
    public static final  int    CANVAS_WIDTH      = 1280; // the width of the rendered canvas
    public static final  int    CANVAS_HEIGHT     = 720; // the height of the rendered canvas
    public static final  float  ASPECT_RATIO      = (float)CANVAS_WIDTH / CANVAS_HEIGHT;
    public static final  float  SCALING_TOLERANCE = 0.05f; // % to prefer maximizing size over aspect ratio

    // game engine
    private Stage     stage;
    private GameScene activeScene;

    // game state management
    private volatile boolean isInMenu;

    // networking
    private static GameServer       server;
    private static GameClient       client;
    private static ServerDiscoverer discoverer;

    private static ConfigProto.UserConfig userConfig;

    /**
     * Handles communication with JavaFX when this program is signalled to start.
     *
     * @param stage the primary stage for this application, provided by the JavaFX framework.
     */
    @Override
    public void start(Stage stage) {
        // rename thread to shorten logs
        Thread.currentThread().setName("FX");

        this.stage = stage;
        isInMenu = true;

        stage.setTitle(GAME_NAME);
        stage.setWidth(CANVAS_WIDTH);
        stage.setHeight(CANVAS_HEIGHT);
        stage.setMinWidth(CANVAS_WIDTH / 2f);
        stage.setMinHeight(CANVAS_HEIGHT / 2f);

        setScene(new MenuHandler());
        stage.show();
    }

    /**
     * Handles bootstrap and launching the framework + engine.
     *
     * @param args command-line arguments to the Java program.
     *
     * @see GameHandler#start(Stage)
     */
    public static void main(String[] args) throws IOException {
        // load user config first
        userConfig = UserConfigManager.load();

        // then start the javafx program
        launch();

        // this runs AFTER the javafx window closes
        LOG.info("Program terminated, exiting cleanly");
        if (server != null) server.stop();
        if (client != null) client.close();
        terminateDiscoverer();
    }

    /**
     * Discards the currently active scene and replaces it with the provided one.
     *
     * @param scene the GameScene to switch
     */
    public void setScene(GameScene scene) {
        activeScene = GameEngine.setScene(this, stage, activeScene, scene);
    }

    public void goToMainMenu() {
        this.setScene(new MenuHandler());
        setInMenu(true);
        terminateClient();
        terminateServer();
    }

    /**
     * Shows the lobby screen and starts a server.
     *
     * @param roomName the name of the lobby
     */
    public void createLobby(String roomName) {
        LOG.info("Creating lobby named {}", roomName);
        MenuHandler menu = getActiveScene(MenuHandler.class);
        var lobby = new LobbyGameContent(menu, roomName, true);
        menu.setContent(lobby); // set content first so we can populate lobby after
        lobby.addPlayer(
                GameHandler.getUserConfig().getName(), UserConfigManager.parseColor(GameHandler.getUserConfig()));

        if (server != null) throw new IllegalStateException("Server already exists, cannot establish connection");

        server = new GameServer(this);
        isInMenu = true;
        try {
            server.start();
        } catch (IOException e) {
            LOG.error("Failed to start server", e);
        }
    }

    /**
     * Handles a game start
     *
     * @throws IllegalStateException if there is no active server or client
     */
    public void startGame() {
        if (server != null) {
            // TODO: broadcast game start to clients via server
            server.broadcast(new GamePacket(GamePacket.Type.SRV_GAME_STARTING));
        } else if (client != null) {
            // TODO: handle game scene setup from GameClient
            // nothing to do for now !!
        } else {
            throw new IllegalStateException("You cannot start the game without an active server or client!");
        }

        isInMenu = false;

        Platform.runLater(() -> setScene(new DebugGameScene()));
    }

    /**
     * @deprecated not updated to use {@link MenuHandler}, implement that before using this method
     */
    @Deprecated
    public void showJoinGameMenu() {
        discoverer = new ServerDiscoverer(this);
        discoverer.start();
        MenuHandler menu = getActiveScene(MenuHandler.class);
        menu.setContent(new JoinGameContent(menu, this::joinGame));
    }

    /**
     * Joins a remote server, registers itself, and displays the lobby.
     *
     * @param serverAddress address and port of the server to join (addr:port)
     */
    public void joinGame(String serverAddress) {
        String host;
        int port;
        int i = serverAddress.lastIndexOf(':');
        if (i == -1) {
            host = serverAddress;
            port = GameServer.DEFAULT_PORT;
        } else {
            host = serverAddress.substring(0, i);
            port = Integer.parseInt(serverAddress.substring(i + 1));
        }

        discoverer.stop();

        LOG.info("Attempting to join game at {}, port {}", host, port);

        if (client != null) throw new IllegalStateException("Client already exists, cannot establish connection");

        setInMenu(false);
        try {
            client = new GameClient(host, port, this);
            client.connect();
        } catch (ConnectException e) {
            terminateClient();
            showAlert("COULD NOT JOIN SERVER", e.getMessage());
        } catch (IOException e) {
            terminateClient();
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the client and nullifies the pointer.
     */
    private void terminateClient() {
        if (client == null) {
            LOG.warn("Client does not exist, skipping termination");
            return;
        }

        try {
            client.close();
        } catch (IOException e) {
            LOG.error("Failed to close socket during termination", e);
        }
        client = null;
    }

    /**
     * Closes the terminator and nullifies the pointer.
     */
    private static void terminateDiscoverer() {
        if (discoverer == null) {
            LOG.warn("Server discoverer does not exist, skipping termination");
            return;
        }

        discoverer.stop();
        discoverer = null;
    }

    /**
     * Stops the server and nullifies the pointer.
     */
    private void terminateServer() {
        if (server == null) {
            LOG.warn("Server does not exist, skipping termination");
            return;
        }

        try {
            server.stop();
        } catch (IOException e) {
            LOG.error("Failed to close socket during termination", e);
        }
        server = null;
    }

    /**
     * Discards the current scene and shows a new {@link MenuAlertScene} with the provided alert details.
     */
    public void showAlert(String title, String message) {
        MenuHandler menu = getActiveScene(MenuHandler.class);
        menu.setContent(new MenuAlertScene(title, message, menu));
    }

    /**
     * Tries to return the active scene as the (expected) type, casting it to said type, and returning null if such
     * fails.
     *
     * @param type the expected type of {@link GameScene}
     *
     * @return the active {@link GameScene} if it is the right type, or null if it's not
     */
    public <T extends GameScene> T getActiveScene(Class<T> type) {
        if (!type.isInstance(activeScene))
            return null;

        return type.cast(activeScene);
    }

    /**
     * Gets the current network role based on the active network manager (server or client)
     *
     * @return SERVER, CLIENT, or NONE
     */
    public NetworkRole getNetworkRole() {
        if (server != null) return NetworkRole.SERVER;
        else if (client != null) return NetworkRole.CLIENT;
        return NetworkRole.NONE;
    }

    /**
     * A network role is the "active" type of network manager
     */
    public enum NetworkRole {
        SERVER, CLIENT, NONE
    }

    /**
     * @return true if we are in a game, false if we are in the menu
     */
    public boolean isInGame() {
        return !isInMenu;
    }

    public void setInMenu(boolean inMenu) {
        isInMenu = inMenu;
    }

    /**
     * Gets the user config that is actively loaded in memory, NOT from disk.
     *
     * @return stored user confirm
     *
     * @throws NullPointerException if there is no stored user config (this shouldn't happen)
     */
    public static ConfigProto.UserConfig getUserConfig() {
        if (userConfig == null) throw new NullPointerException("User config is null!");
        return userConfig;
    }

    /**
     * Updates only the fields specified and saves the resulting config.
     *
     * @param partial the partial values, whatever is set here will be updated, otherwise it will remain the same.
     */
    public static void updateUserConfig(ConfigProto.UserConfig partial) {
        userConfig = UserConfigManager.update(userConfig, partial);
    }
}