package com.logandhillon.fptgame.entity.ui.component;

import com.logandhillon.fptgame.engine.GameScene;
import com.logandhillon.fptgame.entity.core.Clickable;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Fonts;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * This is an input field, which handles user input in a box and can be retrieved as a string.
 * <p>
 * This entity exposes onKeyPressed and onKeyTyped events that should be registered to the parent scene.
 *
 * @author Logan Dhillon
 * @see InputBoxEntity#onKeyPressed(KeyEvent)
 * @see InputBoxEntity#onKeyTyped(KeyEvent)
 */
public class InputBoxEntity extends Clickable {
    private static final Logger LOG = LoggerContext.getContext().getLogger(InputBoxEntity.class);

    private static final int  INPUT_FONT_SIZE  = 20;
    private static final int  INPUT_CHAR_WIDTH = 12;
    private static final int  CORNER_RADIUS    = 16;
    private static final int  MARGIN_X         = 16;
    private static final int  MARGIN_Y         = 12;
    private static final Font INPUT_FONT       = Font.font(Fonts.PIXELIFY_SANS, INPUT_FONT_SIZE);
    private static final Font LABEL_FONT       = Font.font(Fonts.PIXELIFY_SANS, FontWeight.MEDIUM, 18);

    private final   float  maxWidth;
    private final   String placeholder;
    private final   String label;
    protected final int    charLimit;

    protected StringBuilder input;
    protected boolean       isActive;

    private Runnable onBlur;

    /**
     * Creates an input field at the specified position. THe height will be calculated from a fixed y-margin of 12px and
     * the font size.
     *
     * @param x           x-position (from left)
     * @param y           y-position (from top)
     * @param w           width of the input box
     * @param placeholder placeholder text (shown when box is blank)
     * @param label       the label to show above the input box, will use the same font size.
     * @param charLimit   maximum allowed characters in this field
     */
    public InputBoxEntity(float x, float y, float w, String placeholder, String label, int charLimit) {
        super(x, y, w, INPUT_FONT_SIZE * 1.3f + 2 * MARGIN_Y); // calc height of box

        this.maxWidth = w - 2 * MARGIN_X;
        this.placeholder = placeholder;
        this.label = label;
        this.charLimit = charLimit;
        this.isActive = false;

        this.input = new StringBuilder();
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Colors.ACTIVE);
        g.fillRoundRect(x, y, w, h, CORNER_RADIUS, CORNER_RADIUS);

        g.setTextAlign(TextAlignment.LEFT);
        g.setTextBaseline(VPos.TOP);
        g.setFont(LABEL_FONT);
        g.setFill(Colors.FOREGROUND);
        g.fillText(label, x, y - 31);

        // when active, show a blinking cursor for 500 ms every 1000 ms
        if (isActive && System.currentTimeMillis() % 1000 > 500) {
            g.setStroke(Colors.FOREGROUND);
            g.setLineWidth(2);

            float cursorX = x + input.length() * INPUT_CHAR_WIDTH + MARGIN_X;
            g.strokeLine(cursorX, y + MARGIN_Y, cursorX, y + h - MARGIN_Y);
        }

        g.setFont(INPUT_FONT);
        if (input.isEmpty()) {
            // render placeholder
            g.setFill(Color.hsb(0, 0, 1, 0.2));
            g.fillText(placeholder, x + MARGIN_X, y + MARGIN_Y, maxWidth);
        } else {
            // render input (font is already white)
            g.fillText(input.toString(), x + MARGIN_X, y + MARGIN_Y, maxWidth);
        }
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * Handles input by attaching to the key press event (for backspacing)
     */
    public void onKeyPressed(KeyEvent e) {
        if (isActive) {
            if (e.getCode() == KeyCode.BACK_SPACE && !input.isEmpty()) {
                input.deleteCharAt(input.length() - 1);
            }
            // blur on enter or esc
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                this.onBlur();
            }
            e.consume(); // consume if active
        }
    }

    /**
     * Handles input by attaching to the key typed event (for entering input)
     */
    public void onKeyTyped(KeyEvent e) {
        if (!isActive) return;

        String c = e.getCharacter();

        // ignore blank/control characters
        if (c.isEmpty() || Character.isISOControl(c.charAt(0)) || input.length() >= charLimit) return;

        input.append(c);
        e.consume();
    }

    @Override
    public void onAttach(GameScene parent) {
        super.onAttach(parent);
        parent.addHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        parent.addHandler(KeyEvent.KEY_TYPED, this::onKeyTyped);
    }

    /**
     * Gets the current input buffer.
     *
     * @return content of input buffer as a string
     */
    public String getInput() {
        return input.toString();
    }

    /**
     * Sets the input buffer to a new one with predetermined text
     *
     * @param input the text to fill the new input buffer with
     */

    public void setInput(String input) {
        this.input = new StringBuilder(input);
    }

    @Override
    public void onClick(MouseEvent e) {
        LOG.debug("Input box clicked");
        this.isActive = true;
    }

    @Override
    public void onBlur() {
        LOG.debug("Input box blurred");
        this.isActive = false;
        if (onBlur != null) onBlur.run();
    }

    /**
     * Sets the event that will run when the enter key is pressed.
     */
    public void setOnBlur(Runnable onBlur) {
        this.onBlur = onBlur;
    }
}
