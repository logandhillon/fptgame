package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.component.DarkMenuButton;
import com.logandhillon.fptgame.entity.ui.component.GameButton;
import com.logandhillon.fptgame.entity.ui.component.ModalEntity;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.io.TextResource;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * @author Logan Dhillon, Jack Ross
 */
public class CreditsMenuContent implements MenuContent {
    private final Entity[] entities;

    private static final String CREDITS;

    public CreditsMenuContent(MenuHandler menu) {
        var text = new TextEntity.Builder(CANVAS_WIDTH / 2f, 229)
                          .setText(CREDITS)
                          .setColor(Colors.ACTIVE)
                          .setFontSize(18)
                          .setAlign(TextAlignment.CENTER)
                          .setBaseline(VPos.TOP)
                          .build();

        entities = new Entity[]{ new ModalEntity(349, 213, 583, 294,
                                 new DarkMenuButton("BACK TO MENU", 16, 230, 551, 48, menu::goToMainMenu)),
                                 text};
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
