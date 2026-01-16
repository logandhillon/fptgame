package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.engine.GameScene;
import com.logandhillon.logangamelib.entity.Entity;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import com.logandhillon.fptgame.scene.menu.MenuHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
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
    private static final Font  HEADER_FONT    = Font.font(Fonts.TREMOLO, FontWeight.MEDIUM, 24);

    private static final int   MARGIN         = 16;

    private final String      header;
    private final MenuHandler menu;

    /**
     * Creates an entity at the specified position.
     * <p>
     * All entities passed to this modal will be translated such that (0, 0) is the top-left corner of this modal.
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of modal
     * @param h height of modal
     *
     * @deprecated this is a leftover from the old engine, use {@link MenuModalEntity}
     */
    @Deprecated
    public LabeledModalEntity(float x, float y, float w, float h, String header, MenuHandler menu,
                              Entity... entities) {
        super(x, y, w, h, entities);
        this.header = header;
        this.menu = menu;

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
        parent.addEntity(new BackButtonEntity(x + MARGIN, y + 20, menu));
    }
}
