package com.logandhillon.fptgame.entity.game;

import com.logandhillon.fptgame.engine.GameScene;
import com.logandhillon.fptgame.entity.physics.PhysicsEntity;
import com.logandhillon.fptgame.gfx.AnimationSequence;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Textures;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * The player is a physics entity that is controlled by a human either on the device or over the network.
 *
 * @author Logan Dhillon
 */
public class PlayerEntity extends PhysicsEntity {
    private static final Logger LOG = LoggerContext.getContext().getLogger(PlayerEntity.class);

    private static final float JUMP_POWER = 12f * PX_PER_METER; // m/s
    private static final float MOVE_SPEED = 6f * PX_PER_METER; // m/s
    private static final int   Y_OFFSET   = 12;

    private final AnimationSequence texture = Textures.ANIM_PLAYER_IDLE.instance();

    private int moveDirection = 0; // left=-1, 0=none, 1=right

    public PlayerEntity(float x, float y) {
        super(x, y, 42, 72);
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        texture.draw(g, x, y - Y_OFFSET, w, h + Y_OFFSET, Colors.PLAYER_SKINS.getFirst());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        texture.onUpdate(dt);

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
        if (e.getCode() == KeyCode.SPACE && this.isGrounded())
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
