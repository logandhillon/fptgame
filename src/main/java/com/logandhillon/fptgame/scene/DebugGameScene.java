package com.logandhillon.fptgame.scene;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.entity.player.ControllablePlayerEntity;
import com.logandhillon.fptgame.entity.player.PlayerEntity;
import com.logandhillon.fptgame.entity.player.PlayerInputSender;
import com.logandhillon.fptgame.networking.GamePacket;
import com.logandhillon.fptgame.networking.proto.PlayerProto;
import com.logandhillon.fptgame.resource.Textures;
import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.entity.ui.TextEntity;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Basic game scene with a player that can run around and interact with physics objects; to aid development.
 *
 * @author Logan Dhillon
 */
public class DebugGameScene extends GameScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(DebugGameScene.class);

    private final PlayerEntity self;
    private final PlayerEntity other;
    private final PeerMovementPoller movePoller;

    public DebugGameScene() {
        GameHandler.NetworkRole role = GameHandler.getNetworkRole();
        if (role == GameHandler.NetworkRole.SERVER) {
            movePoller = GameHandler.getServer().queuedPeerMovements::poll;
        } else if (role == GameHandler.NetworkRole.CLIENT) {
            movePoller = GameHandler.getClient().queuedPeerMovements::poll;
        } else {
            throw new IllegalStateException("GameHandler is neither SERVER nor CLIENT, cannot poll peer");
        }

        addEntity(Textures.ocean8());

        addEntity(new PlatformEntity(Textures.UNDERGROUND, Textures.UNDERGROUND, 0, 680, 1280, 40, 13, 3, 11, 11));
        addEntity(new PlatformEntity(Textures.UNDERGROUND,200, 550, 200, 40, 13, 3, 5));
        addEntity(new PlatformEntity(Textures.UNDERGROUND,400, 400, 200, 40, 13, 3, 5));
        addEntity(new PlatformEntity(Textures.UNDERGROUND,600, 280, 200, 40, 13, 3, 5));
        addEntity(new PlatformEntity(Textures.UNDERGROUND,700, 100, 40, 320, 13, 7, 3));
        addEntity(new PlatformEntity(Textures.UNDERGROUND,1100, 200, 40, 320, 13, 7, 3));

        self = new ControllablePlayerEntity(0, 0,
                                            role == GameHandler.NetworkRole.SERVER ? 0 : 1,
                                            new PlayerInputSender());
        addEntity(self);

        other = new PlayerEntity(0, 0, role == GameHandler.NetworkRole.SERVER ? 1 : 0, null);
        addEntity(other);

        addEntity(new TextEntity.Builder(10, 30)
                          .setText(() -> String.format(
                                  """
                                  [PLAYER; YOU]
                                  isGrounded: %s
                                  pos: %.1f, %.1f
                                  vel: %.1f, %.1f
                                  collision: %s
                                  dir: %s

                                  [PLAYER; OTHER]
                                  isGrounded: %s
                                  pos: %.1f, %.1f
                                  vel: %.1f, %.1f
                                  collision: %s
                                  dir: %s""",

                                  self.isGrounded(),
                                  self.getX(), self.getY(),
                                  self.vx, self.vy,
                                  self.getCollision() != null,
                                  self.getMoveDirection(),

                                  other.isGrounded(),
                                  other.getX(), other.getY(),
                                  other.vx, other.vy,
                                  other.getCollision() != null,
                                  other.getMoveDirection()))
                          .setFontSize(14)
                          .build());
    }

    @Override
    protected void onUpdate(float dt) {
        super.onUpdate(dt);

        // poll our peer's move and apply it to our instance.
        GamePacket.Type move = movePoller.poll();
        if (move == null) return;
        LOG.debug("Processing peer movement '{}'", move);
        switch (move) {
            case COM_JUMP -> other.jump();
            case COM_MOVE_L -> other.setMoveDirection(-1);
            case COM_MOVE_R -> other.setMoveDirection(1);
            case COM_STOP_MOVING -> other.setMoveDirection(0);
        }
    }

    /**
     * Assuming that 'self' is the host, this builds the {@link PlayerProto.PlayerPositionSync} message and returns it.
     *
     * @return {@link PlayerProto.PlayerPositionSync} with filled-in values
     */
    public PlayerProto.PlayerPositionSync buildMovementSyncMsg() {
        return PlayerProto.PlayerPositionSync
                .newBuilder()
                .setHost(PlayerProto.PlayerMovementData
                                 .newBuilder()
                                 .setX(self.getX())
                                 .setY(self.getY())
                                 .setVx(self.vx)
                                 .setVy(self.vy)
                                 .build())
                .setGuest(PlayerProto.PlayerMovementData
                                  .newBuilder()
                                  .setX(other.getX())
                                  .setY(other.getY())
                                  .setVx(other.vx)
                                  .setVy(other.vy)
                                  .build())
                .build();
    }

    /**
     * Assuming that `other` is the host, this takes in a {@link PlayerProto.PlayerPositionSync} and "synchronizes" the
     * incoming update message to this {@link GameScene}
     *
     * @param update the incoming {@link PlayerProto.PlayerPositionSync} update message
     */
    public void syncMovement(PlayerProto.PlayerPositionSync update) {
        LOG.debug("Updating movement from remote");
        other.setPosition(update.getHost().getX(), update.getHost().getY());
        other.vx = update.getHost().getVx();
        other.vy = update.getHost().getVy();

        self.setPosition(update.getGuest().getX(), update.getGuest().getY());
        self.vx = update.getGuest().getVx();
        self.vy = update.getGuest().getVy();
    }

    private interface PeerMovementPoller {
        /**
         * Retrieves and removes the head of this queue, or returns {@code null} if this queue is empty.
         *
         * @return the head of this queue, or {@code null} if this queue is empty
         */
        GamePacket.Type poll();
    }
}
