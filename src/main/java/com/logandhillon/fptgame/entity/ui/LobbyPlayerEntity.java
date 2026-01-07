package com.logandhillon.fptgame.entity.ui;

import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.core.BoundEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.fptgame.scene.menu.LobbyGameScene;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * An entity used in {@link LobbyGameScene} to show a graphical representation of a
 * user in the lobby's name, colour, and latency
 *
 * @author Jack Ross, Logan Dhillon
 */
public class LobbyPlayerEntity extends BoundEntity<UIScene> {
    private static final Font LABEL_FONT = Font.font(Fonts.PIXELIFY_SANS, FontWeight.MEDIUM, 16);
    private final Color color;
    private final String playerName;

    /**
     *
     * @param color the color of the given player's skin
     * @param playerName the given player's chosen name
     */
    public LobbyPlayerEntity(Color color, String playerName){
        super(0, 0);
        this.color = color;
        this.playerName = playerName;
    }
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFont(LABEL_FONT);
        g.setFill(color);

        // render player skin
        g.fillRect(x, y, 32, 32);

        g.setTextAlign(TextAlignment.LEFT);
        g.setTextBaseline(VPos.CENTER);
        g.setFill(Colors.FOREGROUND);

        // render player name
        g.fillText(this.playerName, x + 48, y + 16);

        g.setTextAlign(TextAlignment.RIGHT);
        g.setFill(Color.GREY);
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }
}
