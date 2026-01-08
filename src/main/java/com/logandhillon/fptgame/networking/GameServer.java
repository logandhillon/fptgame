package com.logandhillon.fptgame.networking;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.disk.UserConfigManager;
import com.logandhillon.fptgame.networking.proto.PlayerProto;
import com.logandhillon.fptgame.scene.menu.LobbyGameScene;
import com.logandhillon.fptgame.scene.menu.MenuContent;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A game server handles all outgoing communications to {@link GameClient}s via a valid network connection.
 * <p>
 * The server allows multiple clients to connect (using multi-threading to handle each connection independently) and
 * communicates using {@link GamePacket}s.
 *
 * @author Logan Dhillon
 * @see GameClient
 */
public class GameServer implements Runnable {
    private static final Logger LOG             = LoggerContext.getContext().getLogger(GameServer.class);
    public static final  int    DEFAULT_PORT    = 20670; // default port for game
    public static final  int    ADVERTISE_PORT  = 20671; // for UDP broadcast discovery
    private static final int    MAX_CONNECTIONS = 8;

    private volatile boolean      running; // if the server is running
    private final    GameHandler  game;
    private          ServerSocket socket;
    private          Thread       advertiser;

    /** the list of ALL active client connections, including unregistered ones. */
    private final Set<Socket> clients = Collections.synchronizedSet(new HashSet<>());

    /** list of all REGISTERED client connections; maps socket to name */
    private final HashMap<Socket, ConnectionDetails> registeredClients = new HashMap<>();

    private record ConnectionDetails(String name, Color color, PacketWriter out) {}

    public GameServer(GameHandler game) {
        this.game = game;
    }

    /**
     * Starts the server socket and the acceptor thread, which listens for incoming connections and handles them
     * accordingly.
     *
     * @throws IOException if the server socket fails to start.
     */
    public void start() throws IOException {
        LOG.info("Starting server on port {}...", DEFAULT_PORT);
        socket = new ServerSocket(DEFAULT_PORT);
        running = true;
        new Thread(this, "ServerAcceptor").start();
        startAdvertising(); // start the udp advertiser
    }

    /**
     * Tries to stop the server gracefully, ending the acceptor and closing the thread.
     *
     * @throws IOException if the socket fails to close.
     */
    public void stop() throws IOException {
        LOG.info("Stopping server gracefully");
        running = false;
        if (socket != null) {
            LOG.info("Closing server socket now");
            socket.close();
        }
        stopAdvertising();
        LOG.info("Closing {} client connection(s)", clients.size());
        synchronized (clients) {
            for (Socket c: clients) {
                try {
                    c.close();
                } catch (IOException ignored) {
                }
            }
            clients.clear();
        }
    }

    /**
     * The method that will run when this server is started in the thread.
     *
     * @see GameServer#start()
     */
    @Override
    public void run() {
        while (running) {
            try {
                Socket client = socket.accept();
                handleClient(client);
            } catch (IOException e) {
                if (running) LOG.error(e);
            }
        }
    }

    /**
     * Runs when a new client connects; this starts a dedicated thread to handle this client.
     *
     * @param client the incoming client socket
     */
    private void handleClient(Socket client) {
        new Thread(() -> {
            LOG.info("Incoming client connection at {}", client.getInetAddress().getHostAddress());
            clients.add(client);

            try (client;
                 var out = new PacketWriter(client.getOutputStream());
                 DataInputStream dataInputStream = new DataInputStream(client.getInputStream())
            ) {
                while (true) {
                    int length;
                    try {
                        length = dataInputStream.readInt();
                    } catch (IOException e) {
                        LOG.info("Client {} disconnected", client.getInetAddress());
                        registeredClients.remove(client);
                        clients.remove(client);

                        MenuHandler menu = game.getActiveScene(MenuHandler.class);
                        if (menu != null) {
                            MenuContent content = menu.getContent();
                            if (content instanceof LobbyGameScene lobby) {
                                propagateLobbyUpdate(lobby);
                            }
                        }

                        break; // client disconnected or stream closed
                    }
                    if (length <= 0) {
                        LOG.warn("Received invalid packet length {} from {}", length, client.getInetAddress());
                        break;
                    }
                    byte[] data = new byte[length];
                    dataInputStream.readFully(data);
                    parseRequest(client, GamePacket.deserialize(data), out);
                }
            } catch (IOException e) {
                LOG.error(e);
            } finally {
                clients.remove(client);
            }
        }, "ClientHandler-" + client.getInetAddress()).start();
    }

    /**
     * Parses a request from a client
     *
     * @param client the client socket that this packet is from
     * @param packet the packet itself, from the client
     * @param out    the output stream used to respond to the client.
     */
    private void parseRequest(Socket client, GamePacket packet, PacketWriter out) {
        if (packet == null) return;

        LOG.debug("Received {} from {}", packet.type(), client.getInetAddress());

        try {
            // first, check if the client asked to be registered
            if (packet.type() == GamePacket.Type.CLT_REQ_CONN) handleClientRegistration(client, packet, out);

            // next, if they didn't ask and they still aren't registered, kick them
            if (!registeredClients.containsKey(client)) {
                LOG.warn("Got packet from unregistered client; closing connection");
                client.close();
            }

            // finally, parse the request
            switch (packet.type()) {
                // TODO: nothing to parse yet (oh how you will miss this future logan)
            }
        } catch (IOException e) {
            LOG.error("Failed to close client at {}", client.getInetAddress(), e);
        }
    }

    /**
     * Handles a new client request and tries to register it or defer it. Should be called when receiving a CLT_REQ_CONN
     * packet.
     */
    private void handleClientRegistration(Socket client, GamePacket packet, PacketWriter out) throws IOException {
        if (registeredClients.size() < MAX_CONNECTIONS) {
            PlayerProto.PlayerData data = PlayerProto.PlayerData.parseFrom(packet.payload());

            // check if name is already used
            if (data.getName().equals(GameHandler.getUserConfig().getName()) || // remote client matches host
                registeredClients.values().stream().anyMatch(
                        p -> p.name.equals(data.getName()))) { // or remote client matches another client
                LOG.info(
                        "Denying connection from {} (name '{}' in use)", client.getInetAddress(),
                        packet.payload());
                out.send(GamePacket.Type.SRV_DENY_CONN__USERNAME_TAKEN);
                client.close();
                return;
            }

            // get the lobby in advance, so it can get null if we shouldn't do this
            var menu = game.getActiveScene(MenuHandler.class);
            if (menu == null || !(menu.getContent() instanceof LobbyGameScene lobby)) {
                // if lobby IS null, then just throw a warn and return early ;)
                LOG.warn(
                        "Server got a registration request, but was not ready for it. Closing client at {}.",
                        client.getInetAddress());
                out.send(GamePacket.Type.SRV_UNEXPECTED);
                client.close();
                return;
            }

            // all good now! register the client
            Color color = Color.color(data.getR(), data.getG(), data.getB());
            registeredClients.put(client, new ConnectionDetails(data.getName(), color, out));
            LOG.info("Registered new client '{}' with color {} at {}!", data.getName(), color, client.getInetAddress());

            // update everyone's player list
            propagateLobbyUpdate(lobby);
        }

        // check if srv is full
        else {
            LOG.info("Denying connection from {} (server full)", client.getInetAddress());
            out.send(GamePacket.Type.SRV_DENY_CONN__FULL);
            client.close();
        }
    }

    /**
     * Updates the player list and broadcasts the new list to every client
     */
    private void propagateLobbyUpdate(LobbyGameScene lobby) {
        LOG.info("Propagating update for lobby player list");
        lobby.clearPlayers();

        lobby.addPlayer(
                GameHandler.getUserConfig().getName(),
                UserConfigManager.parseColor(GameHandler.getUserConfig()));
        for (ConnectionDetails player: registeredClients.values())
            lobby.addPlayer(player.name, player.color);

        // get the players on each team and send them to the client
        broadcast(new GamePacket(
                GamePacket.Type.SRV_UPDATE_PLAYERLIST,
                PlayerProto.Lobby.newBuilder()
                                 .setName(lobby.getRoomName())
                                 .addAllPlayers(getPlayers().toList())
                                 .build()));
    }

    /**
     * Without checking who, this broadcasts the same packet to all registered clients.
     *
     * @param pkt the packet to broadcast
     */
    public void broadcast(GamePacket pkt) {
        for (ConnectionDetails conn: registeredClients.values()) {
            conn.out.send(pkt);
        }
    }

    public Stream<PlayerProto.PlayerData> getPlayers() {
        // stream registered clients into player data, filtering only those that match the team
        Color color = UserConfigManager.parseColor(GameHandler.getUserConfig());
        return Stream.concat(
                Stream.of(PlayerProto.PlayerData.newBuilder().setName(GameHandler.getUserConfig().getName())
                                                .setR((float)color.getRed())
                                                .setG((float)color.getGreen())
                                                .setB((float)color.getBlue()).build()),
                registeredClients.values().stream()
                                 .map(d -> PlayerProto.PlayerData.newBuilder()
                                                                 .setName(d.name)
                                                                 .setR((float)d.color.getRed())
                                                                 .setG((float)d.color.getGreen())
                                                                 .setB((float)d.color.getBlue())
                                                                 .build()
                                 ));
    }

    /**
     * Starts advertising this server on the IP broadcast channel with UDP discovery packets.
     */
    public void startAdvertising() {
        if (advertiser != null && advertiser.isAlive()) return;

        LOG.info("Starting UDP advertiser on port {}", ADVERTISE_PORT);

        advertiser = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                while (running) {
                    // only listen in lobby
                    if (game.isInGame()) continue;
                    var menu = game.getActiveScene(MenuHandler.class);
                    if (!(menu.getContent() instanceof LobbyGameScene lobby)) {
                        LOG.warn("Supposed to be in lobby, but lobby was null. Will not advertise this frame.");
                        return;
                    }

                    LOG.debug("Broadcasting server advertisement for port");

                    String msg =
                            "MainServer:" + lobby.getRoomName() + ":" + DEFAULT_PORT; // incl. lobby name and port
                    byte[] buffer = msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(
                            buffer,
                            buffer.length,
                            InetAddress.getByName("255.255.255.255"),
                            ADVERTISE_PORT
                    );

                    socket.send(packet);
                    Thread.sleep(2000); // broadcast every 2 seconds
                }
            } catch (IOException e) {
                LOG.error("IO exception in server advertiser", e);
            } catch (InterruptedException e) {
                LOG.info("Advertiser interrupted, stopping");
                stopAdvertising();
            }
        }, "ServerAdvertiser");

        advertiser.start();
    }

    /**
     * Stops the ServerAdvertiser immediately.
     */
    public void stopAdvertising() {
        if (advertiser != null) {
            LOG.info("Stopping UDP advertiser on port {}", ADVERTISE_PORT);
            advertiser.interrupt();
            advertiser = null;
        }
    }
}