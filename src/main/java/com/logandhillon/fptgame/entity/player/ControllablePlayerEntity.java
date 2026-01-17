package com.logandhillon.fptgame.entity.player;

import com.logandhillon.logangamelib.engine.GameScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * A version of the {@link PlayerEntity} that is controlled by the keyboard and mouse of the program.
 *
 * @author Logan Dhillon
 */
public class ControllablePlayerEntity extends PlayerEntity {
    private static final Logger LOG = LoggerContext.getContext().getLogger(PlayerEntity.class);

    public ControllablePlayerEntity(float x, float y, int color, PlayerMovementListener listener) {
        super(x, y, color, listener);
    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        LOG.info("Registering events");
        parent.addHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        parent.addHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);
    }

    private void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE) this.jump();

        if (e.getCode() == KeyCode.A) setMoveDirection(-1);
        else if (e.getCode() == KeyCode.D) setMoveDirection(1);
    }

    private void onKeyReleased(KeyEvent e) {
        if ((e.getCode() == KeyCode.A && getMoveDirection() == -1) ||
            (e.getCode() == KeyCode.D && getMoveDirection() == 1))
            setMoveDirection(0);
    }
}
