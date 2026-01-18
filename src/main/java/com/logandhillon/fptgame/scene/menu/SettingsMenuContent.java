package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.entity.ui.component.MenuButton;
import com.logandhillon.fptgame.entity.ui.component.MenuModalEntity;
import com.logandhillon.fptgame.entity.ui.component.SliderEntity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.logangamelib.entity.Entity;
import com.logandhillon.logangamelib.entity.Renderable;
import com.logandhillon.logangamelib.entity.ui.TextEntity;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * The settings menu allows users to customize audio and gameplay settings
 *
 * @author Jack Ross
 */
public class SettingsMenuContent implements MenuContent{
    private final        String   HEADER      = "SETTINGS";
    private static final Font     HEADER_FONT = Font.font(Fonts.TREMOLO, FontWeight.MEDIUM, 32);
    private static final Font     SUBHEADER_FONT = Font.font(Fonts.TREMOLO, FontWeight.MEDIUM, 24);
    private static final Font     CONTROLS_FONT = Font.font(Fonts.TREMOLO, FontWeight.MEDIUM, 19);
    private static final Font    INSTRUCTION_FONT = Font.font(Fonts.TREMOLO, FontWeight.MEDIUM, 20);

    private final        MenuHandler menu;
    private final        Entity[] entities;
    private              Controls currentKeyBind;
    private              KeyCode  moveLeft;
    private              KeyCode  moveRight;
    private              KeyCode  jump;
    private              KeyCode  interact;

    /**
     * Creates content for settings menu
     *
     * @param menu the {@link MenuHandler} responsible for switching active scenes.
     */
    public SettingsMenuContent(MenuHandler menu) {

        this.menu = menu;
        // volume sliders
        SliderEntity master = new SliderEntity(32, 227, 327, 6, 190);

        SliderEntity music = new SliderEntity(32, 296, 327, 6, 190);

        SliderEntity sfx = new SliderEntity(32, 365, 327, 6, 190);

        entities = new Entity[]{
                new MenuModalEntity(
                        0, 0, 442, GameHandler.CANVAS_HEIGHT, true, menu),

                // labels/menu headers
                new TextEntity.Builder(32, 66)
                        .setColor(Colors.ACTIVE)
                        .setText(HEADER.toUpperCase())
                        .setFont(HEADER_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 140)
                        .setColor(Colors.ACTIVE)
                        .setText("AUDIO")
                        .setFont(SUBHEADER_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 410)
                        .setColor(Colors.ACTIVE)
                        .setText("CONTROLS")
                        .setFont(SUBHEADER_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 187)
                        .setColor(Colors.ACTIVE)
                        .setText("MASTER VOLUME")
                        .setFont(CONTROLS_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 256)
                        .setColor(Colors.ACTIVE)
                        .setText("MUSIC VOLUME")
                        .setFont(CONTROLS_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 325)
                        .setColor(Colors.ACTIVE)
                        .setText("SFX VOLUME")
                        .setFont(CONTROLS_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 464.5f)
                        .setColor(Colors.ACTIVE)
                        .setText("MOVE LEFT")
                        .setFont(CONTROLS_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 520.5f)
                        .setColor(Colors.ACTIVE)
                        .setText("MOVE RIGHT")
                        .setFont(CONTROLS_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 576.5f)
                        .setColor(Colors.ACTIVE)
                        .setText("JUMP")
                        .setFont(CONTROLS_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                new TextEntity.Builder(32, 632.5f)
                        .setColor(Colors.ACTIVE)
                        .setText("INTERACT")
                        .setFont(CONTROLS_FONT)
                        .setBaseline(VPos.TOP)
                        .build(),

                // underlines
                new Renderable(
                        32, 168, (g, x, y) -> {
                    g.setStroke(Colors.ACTIVE);
                    g.setLineWidth(2);
                    g.strokeLine(x, y, x + 70, y);
                }),

                new Renderable(
                        32, 439, (g, x, y) -> {
                    g.setStroke(Colors.ACTIVE);
                    g.setLineWidth(2);
                    g.strokeLine(x, y, x + 113, y);
                }),

                // control buttons

                //TODO: Implement default values via protobuf
                new MenuButton(
                        "A", 279, 457, 80, 40, () -> {
                    currentKeyBind = Controls.LEFT;
                }),

                new MenuButton(
                        "D", 279, 513, 80, 40, () -> {
                    currentKeyBind = Controls.RIGHT;
                }),

                new MenuButton(
                        "SPACE", 279, 569, 80, 40, () -> {
                    currentKeyBind = Controls.JUMP;
                }),

                new MenuButton(
                        "E", 279, 625, 80, 40, () -> {
                    currentKeyBind = Controls.INTERACT;
                }),

                master, music, sfx };
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

    @Override
    public void onShow() {
        menu.bindHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
    }

    @Override
    public void onRender(GraphicsContext g) {
        if (currentKeyBind != null) {
            g.setFill(Colors.FOREGROUND_TRANS_40);
            g.fillRect(0 ,0, GameHandler.CANVAS_WIDTH, GameHandler.CANVAS_HEIGHT);

            g.setFill(Colors.ACTIVE);
            g.setFont(INSTRUCTION_FONT);
            g.setTextAlign(TextAlignment.CENTER);
            g.setTextBaseline(VPos.CENTER);
            g.fillText("PRESS ANY BUTTON", GameHandler.CANVAS_WIDTH / 2f,GameHandler.CANVAS_HEIGHT / 2f);
        }
    }

    private enum Controls {
        LEFT, RIGHT, JUMP, INTERACT
    }

    private void onKeyPressed(KeyEvent e){
        if (!(e.getCode().isLetterKey())) return;

        switch (currentKeyBind) {
            case LEFT -> {
                moveLeft = e.getCode();
                currentKeyBind = null;
            }
            case RIGHT -> {
                moveRight = e.getCode();
                currentKeyBind = null;
            }
            case JUMP-> {
                jump = e.getCode();
                currentKeyBind = null;
            }
            case INTERACT -> {
                interact = e.getCode();
                currentKeyBind = null;
            }
        }
    }


    public KeyCode getMoveLeft() {
        return moveLeft;
    }

    public KeyCode getMoveRight() {
        return moveRight;
    }

    public KeyCode getJump() {
        return jump;
    }

    public KeyCode getInteract() {
        return interact;
    }
}
