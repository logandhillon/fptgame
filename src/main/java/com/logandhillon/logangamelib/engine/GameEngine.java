package com.logandhillon.logangamelib.engine;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.resource.Colors;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Provides utility methods for creation of a game engine
 *
 * @author Logan Dhillon
 */
public class GameEngine {
    private static final Logger LOG = LoggerContext.getContext().getLogger(GameEngine.class);

    private static final int FADE_TIME = 200;

    /**
     * Discards the old {@link GameScene}, builds the new {@link GameScene}, and displays the built scene to the
     * {@link Stage}
     *
     * @param stage    the javafx application stage
     * @param oldScene the previously active scene that will be discarded
     * @param newScene the new scene that will be built and displayed
     *
     * @return a pointer to the {@link GameScene} that is now displayed
     */
    public static GameScene setScene(GameHandler game, Stage stage, GameScene oldScene, GameScene newScene) {
        LOG.info("Switching scene to {}", newScene);

        Scene currentScene = stage.getScene();
        Pane overlayPane = new Pane();
        overlayPane.setPrefSize(stage.getWidth(), stage.getHeight());

        Rectangle fadeRect = new Rectangle(stage.getWidth(), stage.getHeight(), Colors.GENERIC_BG);
        fadeRect.setOpacity(0);
        overlayPane.getChildren().add(fadeRect);

        if (currentScene == null) {
            // no previous scene, just immediately show the new one
            stage.setScene(newScene.build(game));
            return newScene;
        }

        // add fade rect to current scene
        ((Pane)currentScene.getRoot()).getChildren().add(overlayPane);

        // fade the rect to black
        FadeTransition fadeOut = new FadeTransition(Duration.millis(FADE_TIME), fadeRect);
        fadeOut.setFromValue(0);
        fadeOut.setToValue(1);

        fadeOut.setOnFinished(e -> {
            if (oldScene != null) oldScene.discard(currentScene);

            // Switch scene
            Scene newFxScene = newScene.build(game);
            stage.setScene(newFxScene);

            // put the rect on the new scene too...
            ((Pane)newFxScene.getRoot()).getChildren().add(fadeRect);

            // ...and fade it in
            FadeTransition fadeIn = new FadeTransition(Duration.millis(FADE_TIME), fadeRect);
            fadeIn.setFromValue(1);
            fadeIn.setToValue(0);
            fadeIn.setOnFinished(ev -> {
                // finally , it can be removed!!!
                ((Pane)newFxScene.getRoot()).getChildren().remove(fadeRect);
            });
            fadeIn.play();
        });

        fadeOut.play();

        return newScene;
    }
}
