package com.logandhillon.fptgame.scene;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.player.ControllablePlayerEntity;
import com.logandhillon.fptgame.entity.player.PlayerEntity;
import com.logandhillon.fptgame.entity.player.PlayerInputSender;
import com.logandhillon.fptgame.level.LevelFactory;
import com.logandhillon.fptgame.level.LevelObject;
import com.logandhillon.fptgame.networking.GamePacket;
import com.logandhillon.fptgame.networking.PeerMovementPoller;
import com.logandhillon.fptgame.networking.proto.LevelProto;
import com.logandhillon.fptgame.networking.proto.PlayerProto;
import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.entity.Renderable;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Loads encoded level data into a playable {@link GameScene}
 *
 * @author Logan Dhillon
 */
public class DynamicLevelScene extends GameScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(DynamicLevelScene.class);

    private static final float SYNC_LERP      = 0.5f; // how aggressively we correct per second
    private static final float SYNC_THRESHOLD = 0.25f; // sq px to consider a position "valid"

    // our real position as per the server
    private float   selfTx;
    private float   selfTy;
    private boolean hasSelfTarget = false;

    private final PlayerEntity       self;
    private final PlayerEntity       other;
    private final PeerMovementPoller movePoller;

    public DynamicLevelScene(LevelProto.LevelData level) {
        // show the background or a black bg if no background
        Renderable bg = LevelFactory.buildBgOrNull(level);
        if (bg != null) addEntity(bg);
        else addEntity(new Renderable(0, 0, (g, x, y) -> {
            g.setFill(Color.BLACK);
            g.fillRect(0, 0, GameHandler.CANVAS_WIDTH, GameHandler.CANVAS_HEIGHT);
        }));

        GameHandler.NetworkRole role = GameHandler.getNetworkRole();
        if (role == GameHandler.NetworkRole.SERVER) {
            movePoller = GameHandler.getServer().queuedPeerMovements::poll;
        } else if (role == GameHandler.NetworkRole.CLIENT) {
            movePoller = GameHandler.getClient().queuedPeerMovements::poll;
        } else {
            throw new IllegalStateException("GameHandler is neither SERVER nor CLIENT, cannot poll peer");
        }

        for (LevelObject obj: LevelFactory.load(level)) addEntity(obj);

        self = new ControllablePlayerEntity(0, 0,
                                            role == GameHandler.NetworkRole.SERVER ? 0 : 1,
                                            new PlayerInputSender());
        addEntity(self);

        other = new PlayerEntity(0, 0, role == GameHandler.NetworkRole.SERVER ? 1 : 0, null);
        addEntity(other);
    }

    @Override
    protected void onUpdate(float dt) {
        super.onUpdate(dt);

        // poll our peer's move and apply it to our instance.
        GamePacket.Type move = movePoller.poll();
        if (move != null) {
            LOG.debug("Processing peer movement '{}'", move);
            switch (move) {
                case COM_JUMP -> other.jump();
                case COM_MOVE_L -> other.setMoveDirection(-1);
                case COM_MOVE_R -> other.setMoveDirection(1);
                case COM_STOP_MOVING -> other.setMoveDirection(0);
            }
        }

        // continuous lerp for local player
        if (hasSelfTarget && self.isGrounded()) {
            // if not moving, move directly to target
            if (self.getMoveDirection() == 0) {
                self.setPosition(selfTx, selfTy);
                hasSelfTarget = false;
            } else {
                // otherwise lerp towards it
                float alpha = 1f - (float)Math.exp(-SYNC_LERP * dt);
                float nx = self.getX() + (selfTx - self.getX()) * alpha;
                float ny = self.getY() + (selfTy - self.getY()) * alpha;
                self.setPosition(nx, ny);

                // stop correcting when close enough
                float dx = selfTx - nx;
                float dy = selfTy - ny;
                if (dx * dx + dy * dy < SYNC_THRESHOLD) {
                    self.setPosition(selfTx, selfTy);
                    hasSelfTarget = false;
                }
            }
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

        // remote player is always authoritative
        other.setPosition(
                other.getX() + (update.getHost().getX() - other.getX()) * SYNC_LERP,
                other.getY() + (update.getHost().getY() - other.getY()) * SYNC_LERP
        );
        other.vx = update.getHost().getVx();
        other.vy = update.getHost().getVy();

        // our position is an estimate, we are the authoritative answer; therefore lerp our position
        if (!self.isGrounded()) return; // airborne: ignore position, keep local prediction

        selfTx = update.getGuest().getX();
        selfTy = update.getGuest().getY();
        hasSelfTarget = true;

        self.vx = update.getGuest().getVx();
        self.vy = update.getGuest().getVy();
    }
}
