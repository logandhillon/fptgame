package com.logandhillon.fptgame.scene;

import com.logandhillon.fptgame.engine.GameScene;
import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.entity.game.PlayerEntity;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.resource.Colors;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * Basic game scene with a player that can run around and interact with physics objects; to aid development.
 *
 * @author Logan Dhillon
 */
public class DebugGameScene extends GameScene {
    public DebugGameScene() {
        addEntity(new PlatformEntity(0, 680, 1280, 40));
        addEntity(new PlatformEntity(200, 550, 200, 40));
        addEntity(new PlatformEntity(400, 400, 200, 40));
        addEntity(new PlatformEntity(600, 280, 200, 40));
        addEntity(new PlatformEntity(700, 100, 40, 300));
        addEntity(new PlatformEntity(1100, 200, 40, 300));

        var player = new PlayerEntity(1280 / 2f, 200);
        addEntity(player);

        addEntity(new TextEntity.Builder(10, 30)
                .setText(() -> String.format(
                        """
                                [PLAYER]
                                isGrounded: %s
                                pos: %.1f, %.1f
                                vel: %.1f, %.1f
                                collision: %s

                                [DEBUG]
                                press [T] to force jump""",
                        player.isGrounded(),
                        player.getX(), player.getY(),
                        player.vx, player.vy,
                        player.getCollision() != null))
                .setFontSize(14)
                .build());

        addHandler(KeyEvent.KEY_PRESSED, e -> {
            // create new instance when R pressed (reload)
            if (e.getCode() == KeyCode.R)
                getParent().setScene(new DebugGameScene());
            if (e.getCode() == KeyCode.T)
                player.vy -= 10f;
        });
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.GENERIC_BG);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all other entities
        super.render(g);
    }
}
