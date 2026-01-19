package com.logandhillon.fptgame.networking;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.scene.menu.JoinGameContent;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Logan Dhillon
 */
public class ServerDiscoverer {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ServerDiscoverer.class);

    private final    List<JoinGameContent.ServerEntry> discoveredServers = new ArrayList<>();
    private final    GameHandler                       game;
    private          Thread                            discoverer;
    private          Thread                            purger;
    private volatile DatagramSocket                    socket;

    private volatile boolean listening        = false;
    private volatile long    lastUpdateMillis = System.currentTimeMillis();

    public ServerDiscoverer(GameHandler game) {
        this.game = game;
    }

    /**
     * Starts the server discoverer
     */
    public void start() {
        LOG.info("Starting UDP server discovery thread");
        listening = true;

        discoverer = new Thread(() -> {
            try {
                socket = new DatagramSocket(GameServer.ADVERTISE_PORT);
                byte[] buffer = new byte[256];

                while (listening && !Thread.currentThread().isInterrupted()) {
                    if (game.isInGame()) {
                        Thread.yield();
                        continue; // only listen in lobby
                    }

                    // dump all discovered servers— if they don't advertise again, they are probably gone
                    discoveredServers.clear();

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String pkt = new String(packet.getData(), 0, packet.getLength());
                    if (!pkt.startsWith("MainServer")) {
                        return;
                    } // ignore other services
                    String[] parts = pkt.split(":", 3); // {service name:lobby name:port}
                    if (parts.length < 3) {
                        LOG.warn(
                                "Received malformed server advertisement, there is likely a bad server on the network");
                        return;
                    }

                    String ip = packet.getAddress().getHostAddress();
                    discoveredServers.add(new JoinGameContent.ServerEntry(parts[1], ip));
                    LOG.info("Discovered server at {}:{}", ip, pkt);

                    updateJoinGameScene();
                    lastUpdateMillis = System.currentTimeMillis();
                }
            } catch (SocketException e) {
                LOG.info("UDP listener socket closed");
            } catch (IOException e) {
                LOG.error("Exception while receiving server discovery packet", e);
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }, "UDP-ServerDiscovery");
        discoverer.start();

        // updates the server list if it becomes stale
        purger = new Thread(() -> {
            while (listening && !Thread.currentThread().isInterrupted()) {
                // only update 4s after the last update
                if (lastUpdateMillis + 4000 > System.currentTimeMillis()) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    continue;
                }

                updateJoinGameScene();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOG.warn("Sleep interrupted, attempting to kill this thread");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }, "Discoverer/ServerListUpdater");
        purger.start();
    }

    /**
     * Stops the server discoverer
     */
    public void stop() {
        LOG.info("Stopping discovery threads");
        listening = false;

        if (socket != null && !socket.isClosed()) {
            socket.close(); // force-unblock receive()
        }

        if (discoverer != null) discoverer.interrupt();
        if (purger != null) purger.interrupt();
    }

    private void updateJoinGameScene() {
        Optional<MenuHandler> menu = game.getActiveScene(MenuHandler.class);
        if (menu.isEmpty() || !(menu.get().getContent() instanceof JoinGameContent scene)) return;
        LOG.debug("Updating join game scene with {} servers", discoveredServers.size());
        scene.setDiscoveredServers(discoveredServers);
    }
}
