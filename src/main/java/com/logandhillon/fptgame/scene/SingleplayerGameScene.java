package com.logandhillon.fptgame.scene;

import com.logandhillon.fptgame.entity.player.ControllablePlayerEntity;
import com.logandhillon.fptgame.entity.player.PlayerEntity;
import com.logandhillon.fptgame.level.LevelFactory;
import com.logandhillon.fptgame.level.LevelObject;
import com.logandhillon.fptgame.networking.proto.LevelProto;
import com.logandhillon.fptgame.resource.Textures;
import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.entity.ui.TextEntity;

/**
 * Basic game scene with a player that can run around and interact with physics objects; to aid development.
 *
 * @author Logan Dhillon
 */
public class SingleplayerGameScene extends GameScene {
    private final PlayerEntity self;

    public SingleplayerGameScene(LevelProto.LevelData level) {
        addEntity(Textures.ocean8());

        self = new ControllablePlayerEntity(0, 0, 0, null);
        addEntity(self);

        for (LevelObject obj: LevelFactory.load(level)) addEntity(obj);

        addEntity(new TextEntity.Builder(10, 30)
                          .setText(() -> String.format(
                                  """
                                  [PLAYER; YOU]
                                  isGrounded: %s
                                  pos: %.1f, %.1f
                                  vel: %.1f, %.1f
                                  collision: %s
                                  dir: %s""",

                                  self.isGrounded(),
                                  self.getX(), self.getY(),
                                  self.vx, self.vy,
                                  self.getCollision() != null,
                                  self.getMoveDirection()))
                          .setFontSize(14)
                          .build());
    }
}
