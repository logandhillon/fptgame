package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.component.GameButton;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.io.TextResource;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * @author Logan Dhillon
 */
public class CreditsMenuContent implements MenuContent {
    private final Entity[] entities;

    private static final String CREDITS;

    public CreditsMenuContent(MenuHandler menu) {
        var text = new TextEntity.Builder(150, CANVAS_WIDTH / 2f)
                          .setText(CREDITS)
                          .setFontSize(18)
                          .setAlign(TextAlignment.CENTER)
                          .setBaseline(VPos.TOP)
                          .build();

        entities = new Entity[]{text, new GameButton("BACK TO MENU", 481, 613, 318, 45, menu::goToMainMenu)};
    }

    static {
        // read credits from resources and store them into static
        try (TextResource res = new TextResource("credits.txt")) {
            CREDITS = res.load();
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
