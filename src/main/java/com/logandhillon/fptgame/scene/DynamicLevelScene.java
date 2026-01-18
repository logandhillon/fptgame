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

    private final PlayerEntity other;
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

        other = new PlayerEntity(0, 0, role == GameHandler.NetworkRole.SERVER ? 1 : 0, null);
        addEntity(other);

        PlayerEntity self = new ControllablePlayerEntity(0, 0,
                                                         role == GameHandler.NetworkRole.SERVER ? 0 : 1,
                                                         new PlayerInputSender());
        addEntity(self); // render self on top of other, we should always be visible first.
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
     * Takes in the {@link PlayerProto.PlayerMovementData} from the partner/other, and "synchronizes" their
     * position to the incoming update message.
     *
     * @param update the incoming {@link PlayerProto.PlayerMovementData} update message
     */
    public void syncMovement(PlayerProto.PlayerMovementData update) {
        LOG.debug("Updating movement from remote");
        other.setPosition(update.getX(), update.getY());
        other.vx = update.getVx();
        other.vy = update.getVy();
    }
}
