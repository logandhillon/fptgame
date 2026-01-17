package com.logandhillon.fptgame.scene;

import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.entity.player.ControllablePlayerEntity;
import com.logandhillon.fptgame.entity.player.PlayerEntity;
import com.logandhillon.fptgame.resource.Textures;
import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.entity.ui.TextEntity;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Basic game scene with a player that can run around and interact with physics objects; to aid development.
 *
 * @author Logan Dhillon
 */
public class DebugGameScene extends GameScene {
    private final PlayerEntity ai;

    public DebugGameScene() {
        addEntity(Textures.ocean8());

        addEntity(new PlatformEntity(0, 680, 1280, 40));
        addEntity(new PlatformEntity(200, 550, 200, 40));
        addEntity(new PlatformEntity(400, 400, 200, 40));
        addEntity(new PlatformEntity(600, 280, 200, 40));
        addEntity(new PlatformEntity(700, 100, 40, 300));
        addEntity(new PlatformEntity(1100, 200, 40, 300));

        ai = new PlayerEntity(100, 500, 1);
        addEntity(ai);

        var player = new ControllablePlayerEntity(1280 / 2f, 200, 0);
        addEntity(player);

        addEntity(new TextEntity.Builder(10, 30)
                          .setText(() -> String.format(
                                  """
                                  [PLAYER; YOU]
                                  isGrounded: %s
                                  pos: %.1f, %.1f
                                  vel: %.1f, %.1f
                                  collision: %s
                                  dir: %s

                                  [PLAYER; AI]
                                  isGrounded: %s
                                  pos: %.1f, %.1f
                                  vel: %.1f, %.1f
                                  collision: %s
                                  dir: %s

                                  [DEBUG]
                                  press [R] to restart
                                  press [T] to force jump""",

                                  player.isGrounded(),
                                  player.getX(), player.getY(),
                                  player.vx, player.vy,
                                  player.getCollision() != null,
                                  player.getMoveDirection(),

                                  ai.isGrounded(),
                                  ai.getX(), ai.getY(),
                                  ai.vx, ai.vy,
                                  ai.getCollision() != null,
                                  ai.getMoveDirection()))
                          .setFontSize(14)
                          .build());

        addHandler(KeyEvent.KEY_PRESSED, e -> {
            // create new instance when R pressed (reload)
            if (e.getCode() == KeyCode.R)
                getParent().setScene(new DebugGameScene());
            if (e.getCode() == KeyCode.T)
                player.vy -= 100f;
        });
    }

    @Override
    protected void onUpdate(float dt) {
        super.onUpdate(dt);
        // go between x=200 and x=1000
        if (ai.getX() > 1000) ai.setMoveDirection(-1);
        else if (ai.getX() < 200) ai.setMoveDirection(1);
        // jump every 1s for 50ms (to make sure it jumps lol)
        if (System.currentTimeMillis() % 1000 <= 50) ai.jump();
    }
}
