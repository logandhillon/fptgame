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

    private static final float JUMP_POWER = 75f; // m/s
    private static final float MOVE_SPEED = 400f; // px/s

    private static final int SIZE = 48; // px^2

    private int moveDirection = 0; // left=-1, 0=none, 1=right

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
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        if (Math.abs(moveDirection) > 0) x += MOVE_SPEED * dt * moveDirection;
    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        LOG.info("Registering events");
        parent.addHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        parent.addHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);
    }

    private void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE && this.grounded)
            this.vy = -JUMP_POWER;

        if (e.getCode() == KeyCode.A) moveDirection = -1;
        else if (e.getCode() == KeyCode.D) moveDirection = 1;

    }

    private void onKeyReleased(KeyEvent e) {
        if ((e.getCode() == KeyCode.A && moveDirection == -1) ||
                (e.getCode() == KeyCode.D && moveDirection == 1))
            moveDirection = 0;
    }
}
