package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.ui.component.GameButton;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private static final String[] CREDITS;
    private static final Font     FONT        = Font.font(Fonts.PIXELIFY_SANS, 18);
    private static final int      TEXT_Y      = 150;
    private static final int      TEXT_X      = CANVAS_WIDTH / 2;
    private static final int      LINE_HEIGHT = (int)(1.5 * 18);

    public CreditsMenuScene(GameHandler game) {
        for (int i = 0; i < CREDITS.length; i++) {
            addEntity(new TextEntity(CREDITS[i], FONT, Colors.FOREGROUND, TextAlignment.CENTER, VPos.TOP, TEXT_X,
                                     TEXT_Y + i * LINE_HEIGHT));
        }

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
            CREDITS = new String(is.readAllBytes(), StandardCharsets.UTF_8).split("\n");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load credits.txt", e);
        }
    }
}
