package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.entity.core.Clickable;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.fptgame.scene.menu.MainMenuContent;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Button in various menu screens that returns the user to the main menu
 *
 * @author Logan Dhillon
 */
public final class BackButtonEntity extends Clickable {
    private final MenuHandler menu;

    private static final Font  BACK_BTN_FONT  = Font.font(Fonts.TREMOLO, 17);
    private static final     Color BACK_BTN_COLOR = Color.rgb(6, 147, 255);

    /**
     * Creates a new back button entity
     *
     * @param menu menu scene manger that can set the scene
     */
    public BackButtonEntity(float x, float y, MenuHandler menu) {
        super(x, y, 62, 22);
        this.menu = menu;
    }

    /**
     * Goes to the main menu scene
     *
     * @param e the mouse event provided by JavaFX
     */
    @Override
    public void onClick(MouseEvent e) {
        menu.setContent(new MainMenuContent(menu));
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFont(BACK_BTN_FONT);
        g.setFill(BACK_BTN_COLOR);
        g.setTextBaseline(VPos.TOP);
        g.setTextAlign(TextAlignment.LEFT);
        g.fillText("< BACK", x, y);
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }
}