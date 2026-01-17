package com.logandhillon.fptgame.networking;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.networking.proto.PlayerProto;
import com.logandhillon.fptgame.scene.menu.LobbyGameContent;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import com.logandhillon.logangamelib.networking.PacketWriter;
import javafx.application.Platform;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

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

    public final Queue<GamePacket.Type> queuedPeerMovements = new LinkedList<>();

    private final String      serverAddr;
    private final int         port;
    private final GameHandler game;

    private Socket          socket;
    private DataInputStream in;
    private PacketWriter    out;

    private PlayerProto.PlayerData host;

    /** if this client is registered with a remote server */
    private boolean isRegistered;

    /**
     * Sets up a new client, does not connect to the server.
     *
     * @param serverAddr the FQDN or IP address of the server to connect to
     * @param port       the port (default 20670)
     *
     * @see GameClient#connect()
     */
    public GameClient(String serverAddr, int port, GameHandler game) {
        this.serverAddr = serverAddr;
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
        LOG.info("Connecting to server at {}:{}...", serverAddr, port);
        socket = new Socket(serverAddr, port);

        // setup remote IO
        in = new DataInputStream(socket.getInputStream());
        out = new PacketWriter(socket.getOutputStream());

        String name = GameHandler.getUserConfig().getName();

        // ask to connect
        LOG.info("Asking to connect as '{}'", name);
        out.send(new GamePacket(GamePacket.Type.CLT_REQ_CONN, ProtoBuilder.player(name)));

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
                this.host = data.getHost();

                MenuHandler menu = game.getActiveScene(MenuHandler.class);
                var lobby = new LobbyGameContent(menu, data.getName(), false);
                lobby.clearPlayers();

                game.setInMenu(true);

                // run setContent on the FX thread
                Platform.runLater(() -> {
                    menu.setContent(lobby);
                    // set lobby players on FX thread, since the content must exist before setting players
                    lobby.addPlayer(data.getHost().getName(), true);
                    lobby.addPlayer(data.getGuest().getName(), false);
                });
            }
            case SRV_DENY_CONN__USERNAME_TAKEN, SRV_DENY_CONN__FULL -> {
                LOG.error("Failed to join: {}", packet.type());
                Platform.runLater(() -> game.showAlert(
                        "Failed to join server",
                        "Could not " + serverAddr + ": " + packet.type().name()));
                this.close();
            }
            case SRV_GAME_STARTING -> {
                LOG.info("Server has announced that the game is starting");
                game.startGame();
            }
            case SRV_SHUTDOWN -> {
                // going to the main menu will shut down the client
                LOG.info("Server is shutting down, returning to main menu");
                game.showAlert("SERVER CLOSED", "The server has shut down.");
            }
            // if peer is trying to move, add instruction to queue
            case COM_JUMP, COM_MOVE_L, COM_MOVE_R, COM_STOP_MOVING -> queuedPeerMovements.add(packet.type());
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
}