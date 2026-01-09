package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.engine.GameScene;
import com.logandhillon.fptgame.entity.core.Clickable;
import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.fptgame.scene.menu.MainMenuContent;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
/**
 * This is a stylized version of the {@link ModalEntity} with a header in the top-right and a MENU button on the
 * top-left. (0,0) on this custom modal is not the top-left of the modal itself, but the top-left of the modal content.
 *
 * @author Logan Dhillon
 * @apiNote Do not attach entities inside this modal, just the modal itself.
 */
public class LabeledModalEntity extends ModalEntity {
    private static final Font  HEADER_FONT    = Font.font(Fonts.DOGICA, FontWeight.MEDIUM, 24);
    private static final Font  BACK_BTN_FONT  = Font.font(Fonts.DOGICA, 17);
    private static final Color BACK_BTN_COLOR = Color.rgb(6, 147, 255);
    private static final int   MARGIN         = 16;

    private final String      header;
    private final MenuHandler mgr;

    /**
     * Creates an entity at the specified position.
     * <p>
     * All entities passed to this modal will be translated such that (0, 0) is the top-left corner of this modal.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of modal
     * @param h height of modal
     */
    public LabeledModalEntity(float x, float y, float w, float h, String header, MenuHandler mgr,
                              Entity... entities) {
        super(x, y, w, h, entities);
        this.header = header;
        this.mgr = mgr;

        // after super (which moves entities to relative 0,0), move them below the header
        for (Entity e: entities) e.translate(0, 64);
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        super.onRender(g, x, y);

        g.setTextBaseline(VPos.CENTER);
        g.setFont(HEADER_FONT);
        g.setFill(Colors.FOREGROUND);
        g.setTextAlign(TextAlignment.RIGHT);
        g.fillText(header, x + w - MARGIN, y + 32);

        g.setStroke(Colors.ACTIVE);
        g.setLineWidth(2);
        g.strokeLine(x + MARGIN, y + 63, x + w - MARGIN, y + 63);
    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        // add back button AFTER moving the other entities
        parent.addEntity(new BackButtonEntity(x + MARGIN, y + 20, mgr));
    }

    /**
     * The back button in the top-left of the {@link LabeledModalEntity} that returns to the main menu.
     */
    private static final class BackButtonEntity extends Clickable {
        private final MenuHandler game;

        /**
         * Creates a new back button entity
         *
         * @param game game scene manger that can set the scene
         */
        public BackButtonEntity(float x, float y, MenuHandler game) {
            super(x, y, 62, 22);
            this.game = game;
        }

        /**
         * Goes to the main menu scene
         *
         * @param e the mouse event provided by JavaFX
         */
        @Override
        public void onClick(MouseEvent e) {
            game.setContent(new MainMenuContent(game));
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
}
