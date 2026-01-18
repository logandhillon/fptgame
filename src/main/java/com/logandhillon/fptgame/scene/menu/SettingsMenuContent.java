package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.ui.component.MenuModalEntity;
import com.logandhillon.fptgame.entity.ui.component.SliderEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.logangamelib.entity.Entity;
import com.logandhillon.logangamelib.entity.ui.TextEntity;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SettingsMenuContent implements MenuContent{
    private final        String   HEADER      = "SETTINGS";
    private static final Font     HEADER_FONT = Font.font(Fonts.TREMOLO, FontWeight.MEDIUM, 32);
    private final        Entity[] entities;

    public SettingsMenuContent(MenuHandler menu){

        SliderEntity master = new SliderEntity(32, 227, 327, 6, 190);

        SliderEntity music = new SliderEntity(32, 296, 327, 6, 190);

        SliderEntity sfx = new SliderEntity(32, 365, 327, 6, 190);

        entities = new Entity[]{
                new MenuModalEntity(
                        0, 0, 442, GameHandler.CANVAS_HEIGHT, true, menu),
                new TextEntity.Builder(32, 66)
                        .setColor(Colors.ACTIVE)
                        .setText(HEADER.toUpperCase())
                        .setFont(HEADER_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),
                master, music, sfx
        };
    }
    @Override
    public Entity[] getEntities() {
        return entities;
    }
}
