package com.logandhillon.fptgame.networking;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.disk.UserConfigManager;
import com.logandhillon.fptgame.networking.proto.PlayerProto;
import com.logandhillon.fptgame.scene.menu.LobbyGameContent;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * A game client handles all outgoing communications to the {@link GameServer} via a valid network connection.
 * <p>
 * The client connects to a server using an {@link java.net.InetAddress} and communicates using {@link GamePacket}s.
 *
 * @author Logan Dhillon
 * @see GameServer
 */
public class GameClient {
    private static final Logger LOG = LoggerContext.getContext().getLogger(GameClient.class);

    private final String      host;
    private final int         port;
    private final GameHandler game;

    private Socket          socket;
    private DataInputStream in;
    private PacketWriter    out;

    private List<PlayerProto.PlayerData> players;

    /** if this client is registered with a remote server */
    private boolean isRegistered;

    /**
     * Sets up a new client, does not connect to the server.
     *
     * @param host the FQDN or IP address of the server to connect to
     * @param port the port (default 20670)
     *
     * @see GameClient#connect()
     */
    public GameClient(String host, int port, GameHandler game) {
        this.host = host;
        this.port = port;
        this.game = game;

        isRegistered = false;
    }

    /**
     * Connects to the pre-defined server, sends a REQ_CONN packet, then starts the listener thread.
     *
     * @throws IOException if the socket throws an exception during creation
     * @see GameClient#readLoop()
     */
    public void connect() throws IOException {
        LOG.info("Connecting to server at {}:{}...", host, port);
        socket = new Socket(host, port);

        // setup remote IO
        in = new DataInputStream(socket.getInputStream());
        out = new PacketWriter(socket.getOutputStream());

        String name = GameHandler.getUserConfig().getName();
        Color color = UserConfigManager.parseColor(GameHandler.getUserConfig());

        // ask to connect
        LOG.info("Asking to connect as '{}' with color {}", name, color);
        out.send(new GamePacket(
                GamePacket.Type.CLT_REQ_CONN,
                PlayerProto.PlayerData.newBuilder()
                                      .setName(name)
                                      .setR((float)color.getRed())
                                      .setG((float)color.getGreen())
                                      .setB((float)color.getBlue())
                                      .build()));

        new Thread(this::readLoop, "Client-ReadLoop").start();
    }

    /**
     * The listener thread of the client, handles incoming communication from the server, deserializes it, and sends it
     * to {@link GameClient#parseResponse(GamePacket)}.
     *
     * @apiNote This should be run in a separate thread, as it is a blocking action.
     */
    private void readLoop() {
        try {
            while (true) {
                int length;
                try {
                    length = in.readInt();
                } catch (IOException e) {
                    LOG.warn("Failed to read length byte from server packet");
                    break; // client disconnected or stream closed
                }
                if (length <= 0) {
                    LOG.warn("Received invalid packet length {} from SERVER", length);
                    break;
                }
                byte[] data = new byte[length];
                in.readFully(data);
                parseResponse(GamePacket.deserialize(data));
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Handles an incoming packet
     *
     * @param packet the deserialized packet from the server
     */
    private void parseResponse(GamePacket packet) throws IOException {
        if (packet == null) return;

        LOG.debug("Received {} from SERVER", packet.type());

        switch (packet.type()) {
            case SRV_UPDATE_PLAYERLIST -> {
                if (!isRegistered) {
                    LOG.info("Successfully registered with remote server");
                    isRegistered = true;
                }

                var data = PlayerProto.Lobby.parseFrom(packet.payload());
                this.players = data.getPlayersList();

                MenuHandler menu = game.getActiveScene(MenuHandler.class);
                var lobby = new LobbyGameContent(menu, data.getName(), false);
                lobby.clearPlayers();

                for (var p: this.players)
                    lobby.addPlayer(p.getName(), Color.color(p.getR(), p.getG(), p.getB()));

                game.setInMenu(true);

                // run setContent on the FX thread
                Platform.runLater(() -> menu.setContent(lobby));
            }
            case SRV_DENY_CONN__USERNAME_TAKEN, SRV_DENY_CONN__FULL -> {
                LOG.error("Failed to join: {}", packet.type());
                Platform.runLater(() -> game.showAlert(
                        "Failed to join server",
                        "Could not " + host + ": " + packet.type().name()));
                this.close();
            }
            case SRV_GAME_STARTING -> {
                game.startGame();
            }
        }
    }

    /**
     * Sends a packet to the connected server.
     *
     * @param pkt the packet to send
     *
     * @throws IllegalStateException if the {@link PacketWriter} is null (i.e. client not connected)
     */
    public void sendServer(GamePacket pkt) {
        if (out == null)
            throw new IllegalStateException("Cannot send packets from null PacketWriter; is the client connected?");
        out.send(pkt);
    }

    /**
     * Terminates the active connection with the server
     *
     * @throws IOException if the socket fails to close
     */
    public void close() throws IOException {
        if (socket != null) {
            LOG.info("Closing connection to server");
            socket.close();
        }
    }

    /**
     * Gets the players that the server has told this client about.
     *
     * @return all players in the client
     */
    public List<PlayerProto.PlayerData> getPlayers(int team) {
        return players;
    }
}