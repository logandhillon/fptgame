package com.logandhillon.fptgame.entity.player;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.networking.GamePacket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * @author Logan Dhillon
 */
public class PlayerInputSender implements PlayerEntity.PlayerMovementListener {
    private static final Logger LOG = LoggerContext.getContext().getLogger(PlayerInputSender.class);

    private final Communicator communicator;

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

    @Override
    public void onJump() {
        communicator.send(new GamePacket(GamePacket.Type.COM_JUMP));
    }

    @Override
    public void onMove(int direction) {
        if (direction == -1) communicator.send(new GamePacket(GamePacket.Type.COM_MOVE_L));
        else if (direction == 1) communicator.send(new GamePacket(GamePacket.Type.COM_MOVE_R));
        else if (direction == 0) communicator.send(new GamePacket(GamePacket.Type.COM_STOP_MOVING));
    }

    private interface Communicator {
        void send(GamePacket pkt);
    }
}
