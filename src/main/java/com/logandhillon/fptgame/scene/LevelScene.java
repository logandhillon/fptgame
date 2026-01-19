package com.logandhillon.fptgame.scene;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.game.MovingPlatformEntity;
import com.logandhillon.fptgame.entity.game.PlatformEntity;
import com.logandhillon.fptgame.level.LevelFactory;
import com.logandhillon.fptgame.level.LevelObject;
import com.logandhillon.fptgame.networking.proto.LevelProto;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import com.logandhillon.logangamelib.engine.GameScene;
import com.logandhillon.logangamelib.entity.Renderable;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.ArrayList;

/**
 * A generic level scene contains underlying common methods between the {@link SingleplayerGameScene} and
 * {@link DynamicLevelScene}. Anything displaying {@link com.logandhillon.fptgame.networking.proto.LevelProto.LevelData}
 * is expected to be a generic level scene.
 *
 * @author Logan Dhillon
 */
public abstract class LevelScene extends GameScene {
    private static final Logger LOG = LoggerContext.getContext().getLogger(LevelScene.class);

    private final LevelProto.LevelData            level;
    private final ArrayList<MovingPlatformEntity> movingPlatforms = new ArrayList<>();
    private final ArrayList<PlatformEntity>       platforms       = new ArrayList<>();

    public LevelScene(LevelProto.LevelData level) {
        this.level = level;
        // show the background or a black bg if no background
        Renderable bg = LevelFactory.buildBgOrNull(level);
        if (bg != null) addEntity(bg);
        else addEntity(new Renderable(0, 0, (g, x, y) -> {
            g.setFill(Color.BLACK);
            g.fillRect(0, 0, GameHandler.CANVAS_WIDTH, GameHandler.CANVAS_HEIGHT);
        }));

        for (LevelObject obj: LevelFactory.load(level)) {
            addEntity(obj);
            if (obj instanceof PlatformEntity e) platforms.add(e);
            if (obj instanceof MovingPlatformEntity e) movingPlatforms.add(e);
        }
    }

    protected abstract LevelScene createNext(LevelProto.LevelData level);

    /**
     * Goes to the next level if there is a next level present in the
     * {@link com.logandhillon.fptgame.networking.proto.LevelProto.LevelData}, otherwise returns to the
     * {@link com.logandhillon.fptgame.scene.menu.MainMenuContent}.
     */
    public void nextLevel() {
        if (level.hasNextLevel()) {
            LOG.info("Going to next level");
            getParent().setScene(this.createNext(level.getNextLevel()));
        } else {
            LOG.info("No next level in this level, going to main menu");
            getParent().setScene(new MenuHandler());
        }
    }

    /**
     * Moves any {@link MovingPlatformEntity} in the level, inverts colors of any {@link PlatformEntity}, etc.; as per
     * what happens when a {@link com.logandhillon.fptgame.entity.game.LevelButtonEntity} is pressed.
     */
    public void onButtonPressed() {
        for (MovingPlatformEntity e: movingPlatforms) e.invertGoingTowardsDest();
        for (PlatformEntity e: platforms) e.invertColor();
    }
}
