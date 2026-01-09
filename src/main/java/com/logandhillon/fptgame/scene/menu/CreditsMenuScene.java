package com.logandhillon.fptgame.scene.menu;


import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.component.GameButton;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.scene.text.Font;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * @author Logan Dhillon
 */
public class CreditsMenuScene implements MenuContent {

    private final Entity[] entities;
    /**
     * Original credits text from resources, split by newlines.
     */
    private static final String[] CREDITS;
    private static final Font     FONT        = Font.font(Fonts.DOGICA, 18);
    private static final int      TEXT_Y      = 150;
    private static final int      TEXT_X      = CANVAS_WIDTH / 2;
    private static final int      LINE_HEIGHT = (int)(1.5 * 18);

    public CreditsMenuScene(MenuHandler menu) {
//        for (int i = 0; i < CREDITS.length; i++) {
//            addEntity(new TextEntity(CREDITS[i], FONT, Colors.FOREGROUND, TextAlignment.CENTER, VPos.TOP, TEXT_X,
//                                     TEXT_Y + i * LINE_HEIGHT));
//        }

        entities = new Entity[]{new GameButton("BACK TO MENU", 481, 613, 318, 45, menu::goToMainMenu)};
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

    /**
     * Allows {@link MenuHandler} to access content for this menu
     *
     * @return entity list
     */
    @Override
    public Entity[] getEntities() {
        return entities;
    }
}
