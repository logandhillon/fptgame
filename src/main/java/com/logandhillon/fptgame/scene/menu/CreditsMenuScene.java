package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.ui.component.GameButton;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.resource.Colors;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * @author Logan Dhillon
 */
public class CreditsMenuScene extends UIScene {
    /**
     * Original credits text from resources, split by newlines.
     */
    private static final String CREDITS;

    public CreditsMenuScene(GameHandler game) {
        addEntity(new TextEntity.Builder(150, CANVAS_WIDTH / 2f)
                          .setText(CREDITS)
                          .setFontSize(18)
                          .setAlign(TextAlignment.CENTER)
                          .setBaseline(VPos.TOP)
                          .build());

        addEntity(new GameButton("BACK TO MENU", 481, 613, 318, 45, game::goToMainMenu));
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.GENERIC_BG);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all other entities
        super.render(g);
    }

    static {
        // read credits from resources and store them into static
        try (InputStream is = CreditsMenuScene.class.getResourceAsStream("/credits.txt")) {
            if (is == null) {
                throw new IllegalStateException("credits.txt not found on classpath");
            }
            CREDITS = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load credits.txt", e);
        }
    }
}
