package com.logandhillon.fptgame.scene.component;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.ui.component.DarkMenuButton;
import com.logandhillon.fptgame.entity.ui.component.ModalEntity;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * This scene simply shows a modal with a message, and a button to return to the main menu.
 *
 * @author Logan Dhillon
 */
public class MenuAlertScene extends UIScene {
    private static final Font TITLE_FONT = Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 20);
    private static final Font BODY_FONT  = Font.font(Fonts.DOGICA, 16);

    public MenuAlertScene(String title, String msg, GameHandler game) {
        String t = title.toUpperCase();
        String m = msg.toUpperCase();

        addEntity(new ModalEntity(
                375, 255, 530, 212,
                new TextEntity.Builder(256, 16).setText(() -> t)
                                               .setFont(TITLE_FONT)
                                               .setAlign(TextAlignment.CENTER)
                                               .setBaseline(VPos.TOP)
                                               .build(),
                new TextEntity.Builder(265, 74).setText(() -> m)
                                               .setFont(BODY_FONT)
                                               .setAlign(TextAlignment.CENTER)
                                               .setBaseline(VPos.TOP)
                                               .build(),
                new DarkMenuButton("OK", 16, 151, 498, 48, game::goToMainMenu)));
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.GENERIC_BG);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all other entities
        super.render(g);
    }
}
