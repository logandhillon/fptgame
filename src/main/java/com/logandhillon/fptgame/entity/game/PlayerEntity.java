package com.logandhillon.fptgame.entity.game;

import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Textures;
import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.entity.physics.PhysicsEntity;
import com.logandhillon.logangamelib.gfx.AnimationSequence;
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

    private AnimationSequence texture = Textures.ANIM_PLAYER_IDLE.instance();
    private AnimationState    state   = AnimationState.IDLE;

    private int moveDirection = 0; // left=-1, 0=none, 1=right

    public PlayerEntity(float x, float y) {
        super(x, y, 42, 72);
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        // render the active texture
        if (state == AnimationState.JUMP) {
            texture.drawFrame(
                    g, Textures.PLAYER_JUMP_FRAME, x, y - Y_OFFSET, w, h + Y_OFFSET, Colors.PLAYER_SKINS.getFirst());
        } else
            texture.draw(g, x, y - Y_OFFSET, w, h + Y_OFFSET, Colors.PLAYER_SKINS.getFirst());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
        texture.onUpdate(dt);

        // move player based on move direction
        if (Math.abs(moveDirection) > 0) x += MOVE_SPEED * dt * moveDirection;

        // update animation state
        if (state == AnimationState.JUMP && isGrounded()) setAnimation(AnimationState.IDLE);
        else if (!isGrounded()) setAnimation(AnimationState.JUMP);
        else if (moveDirection > 0) setAnimation(AnimationState.WALK_RIGHT);
        else if (moveDirection < 0) setAnimation(AnimationState.WALK_LEFT);
        else if (state != AnimationState.JUMP) setAnimation(AnimationState.IDLE);
    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        LOG.info("Registering events");
        parent.addHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        parent.addHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);
    }

    private void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE && this.isGrounded()) {
            this.vy = -JUMP_POWER;
            this.texture = Textures.ANIM_PLAYER_JUMP.instance();
        }

        if (e.getCode() == KeyCode.A) moveDirection = -1;
        else if (e.getCode() == KeyCode.D) moveDirection = 1;
    }

    private void onKeyReleased(KeyEvent e) {
        if ((e.getCode() == KeyCode.A && moveDirection == -1) ||
            (e.getCode() == KeyCode.D && moveDirection == 1))
            moveDirection = 0;
    }

    /**
     * Safely updates the active {@link AnimationState} and the currently visible {@link PlayerEntity#texture}.
     * <p>
     * Does not update anything if the current {@link AnimationState} is equal to the new state
     *
     * @param state the new state
     */
    private void setAnimation(AnimationState state) {
        if (this.state == state) return;

        this.state = state;
        switch (state) {
            case IDLE -> texture = Textures.ANIM_PLAYER_IDLE.instance();
            case JUMP -> texture = Textures.ANIM_PLAYER_JUMP.instance();
            case WALK_LEFT -> texture = Textures.ANIM_PLAYER_RUN_LEFT.instance();
            case WALK_RIGHT -> texture = Textures.ANIM_PLAYER_RUN_RIGHT.instance();
        }
    }

    /**
     * The current animation that should be visible on the player, each ordinal represents an {@link AnimationSequence}
     */
    private enum AnimationState {
        IDLE, JUMP, WALK_LEFT, WALK_RIGHT
    }
}
