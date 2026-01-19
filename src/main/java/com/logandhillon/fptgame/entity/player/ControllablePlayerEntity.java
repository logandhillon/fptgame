package com.logandhillon.fptgame.entity.player;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.game.LevelButtonEntity;
import com.logandhillon.fptgame.scene.LevelScene;
import com.logandhillon.logangamelib.engine.GameScene;
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

    private final String keyLeft;
    private final String keyRight;
    private final String keyJump;
    private final String keyInteract;

    private boolean buttonPressed;

    public ControllablePlayerEntity(float x, float y, int color, PlayerInputSender listener) {
        super(x, y, color, listener);

        var config = GameHandler.getUserConfig();
        keyLeft = config.getKeyMoveLeft();
        keyRight = config.getKeyMoveRight();
        keyJump = config.getKeyMoveJump();
        keyInteract = config.getKeyMoveInteract();
    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        LOG.info("Registering events");
        parent.addHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        parent.addHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);
    }

    private void onKeyPressed(KeyEvent e) {
        if (e.getCode().name().equals(keyJump)) this.jump();

        if (e.getCode().name().equals(keyLeft)) setMoveDirection(-1);
        else if (e.getCode().name().equals(keyRight)) setMoveDirection(1);

        if (e.getCode().name().equals(keyInteract) &&
            parent.getCollisionIf(x, y, w, h, this, LevelButtonEntity.class::isInstance) != null &&
            !buttonPressed) {
            LOG.info("Button pressed");
            buttonPressed = true;
            if (listener != null) listener.onButtonPressed();
            // since the player can only be in a level, cast to LevelScene and trigger button press.
            ((LevelScene)parent).onButtonPressed();
        }
    }

    private void onKeyReleased(KeyEvent e) {
        if ((e.getCode().name().equals(keyLeft) && getMoveDirection() == -1) ||
            (e.getCode().name().equals(keyRight) && getMoveDirection() == 1))
            setMoveDirection(0);

        if (e.getCode().name().equals(keyInteract) && buttonPressed)
            buttonPressed = false;
    }
}
