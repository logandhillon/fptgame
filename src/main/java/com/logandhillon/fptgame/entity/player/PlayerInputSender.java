package com.logandhillon.fptgame.entity.player;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.networking.GamePacket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Network implementation of {@link PlayerEntity.PlayerMovementListener}, which listens for movements and uses an
 * abstract {@link Communicator} to send them over the network to the correct recipient.
 *
 * @author Logan Dhillon
 */
public class PlayerInputSender implements PlayerEntity.PlayerMovementListener {
    private static final Logger LOG = LoggerContext.getContext().getLogger(PlayerInputSender.class);

    private final Communicator communicator;

    /**
     * Creates a new input sender based on the {@link GameHandler.NetworkRole} available in the {@link GameHandler}
     */
    public PlayerInputSender() {
        if (GameHandler.getNetworkRole() == GameHandler.NetworkRole.SERVER) {
            LOG.info("Creating communicator as server");
            this.communicator = GameHandler.getServer()::broadcast;
        } else if (GameHandler.getNetworkRole() == GameHandler.NetworkRole.CLIENT) {
            LOG.info("Creating communicator as client");
            this.communicator = GameHandler.getClient()::sendServer;
        } else {
            throw new IllegalStateException(
                    "GameHandler is neither SERVER nor CLIENT, cannot create PlayerInputSender");
        }
    }

    /**
     * Sends a jump packet to the {@link Communicator}
     */
    @Override
    public void onJump() {
        communicator.send(new GamePacket(GamePacket.Type.COM_JUMP));
    }

    /**
     * Sends movement packets to the {@link Communicator}
     */
    @Override
    public void onMove(int direction) {
        if (direction == -1) communicator.send(new GamePacket(GamePacket.Type.COM_MOVE_L));
        else if (direction == 1) communicator.send(new GamePacket(GamePacket.Type.COM_MOVE_R));
        else if (direction == 0) communicator.send(new GamePacket(GamePacket.Type.COM_STOP_MOVING));
    }

    /**
     * An interface that handles communication with an anonymous party, such as a
     * {@link com.logandhillon.fptgame.networking.GameClient} or {@link com.logandhillon.fptgame.networking.GameServer}.
     * This interface is expected to ensure the packet is sent to the recipient.
     */
    private interface Communicator {
        void send(GamePacket pkt);
    }
}
