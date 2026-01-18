package com.logandhillon.fptgame.entity.player;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.networking.GameClient;
import com.logandhillon.fptgame.networking.GamePacket;
import com.logandhillon.fptgame.networking.GameServer;
import com.logandhillon.fptgame.networking.proto.PlayerProto;
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
     * Sends movement packets to the {@link Communicator}, which contain the direction (as the packet type) and the
     * {@link com.logandhillon.fptgame.networking.proto.PlayerProto.PlayerMovementData} (positions, velocities, etc.)
     */
    @Override
    public void onMove(int direction, float x, float y, float vx, float vy) {
        communicator.send(new GamePacket(switch (direction) {
            case -1 -> GamePacket.Type.COM_MOVE_L;
            case 0 -> GamePacket.Type.COM_STOP_MOVING;
            case 1 -> GamePacket.Type.COM_MOVE_R;
            default -> throw new IllegalStateException("Illegal direction: " + direction);
        }, PlayerProto.PlayerMovementData.newBuilder()
                                         .setX(x).setY(y)
                                         .setVx(vx).setVy(vy)
                                         .build()));
    }

    /**
     * An interface that handles communication with an anonymous party, such as a {@link GameClient} or
     * {@link GameServer}. This interface is expected to ensure the packet is sent to the recipient.
     */
    private interface Communicator {
        /**
         * Sends a packet to the recipient.
         *
         * @param pkt packet to send
         */
        void send(GamePacket pkt);
    }
}
