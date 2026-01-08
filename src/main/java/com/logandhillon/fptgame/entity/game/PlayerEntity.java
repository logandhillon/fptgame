package com.logandhillon.fptgame.entity.game;

import com.logandhillon.fptgame.engine.GameScene;
import com.logandhillon.fptgame.entity.physics.PhysicsEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * @author Logan Dhillon
 */
public class PlayerEntity extends PhysicsEntity {
    private static final Logger LOG = LoggerContext.getContext().getLogger(PlayerEntity.class);

    private static final float JUMP_POWER = 30f;

    private static final int SIZE = 48;

    public PlayerEntity(float x, float y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Color.RED);
        g.fillRect(x, y, w, h);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        LOG.info("Registering events");
        parent.addHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
    }

    private void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE && this.grounded) {
            LOG.info("Jumping!");
            this.vy = -JUMP_POWER;
        }
    }
}
