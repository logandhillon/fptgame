package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.MenuController;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.ui.SkinOptionsEntity;
import com.logandhillon.fptgame.entity.ui.component.InputBoxEntity;
import com.logandhillon.fptgame.entity.ui.component.MenuButton;
import com.logandhillon.fptgame.entity.ui.component.ModalEntity;
import com.logandhillon.fptgame.entity.ui.component.TextEntity;
import com.logandhillon.fptgame.networking.proto.ConfigProto;
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
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon
 */
public class MainMenuScene extends UIScene {
    private final InputBoxEntity userInput;
    private final SkinOptionsEntity[] skins;

    private static final int[][] SKIN_OPTION_POSITIONS = new int[][]{
            { 638, 320, 730, 328, 806, 328, 882, 328 },
            { 638, 328, 714, 320, 806, 328, 882, 328 },
            { 638, 328, 714, 328, 790, 320, 882, 328 },
            { 638, 328, 714, 328, 790, 328, 866, 320 }, };

    /**
     * Creates a new main menu
     *
     * @param game the main class that can switch scenes, manage connections, etc.
     */
    public MainMenuScene(GameHandler game) {
        int defaultColor = 0;
        float x = (CANVAS_WIDTH - 652) / 2f;
        int dy = 48 + 16; // ∆y per button height
        int y = 176;

        game.setInMenu(true);

        MenuController controller = new MenuController(
                new MenuButton("Host Game", x, y, 256, 48, () -> game.setScene(new HostGameScene(game))),
                new MenuButton("Join Game", x, y + dy, 256, 48, game::showJoinGameMenu),
                new MenuButton("Settings", x, y + 2 * dy, 256, 48, () -> {}),
                new MenuButton("Credits", x, y + 3 * dy, 256, 48, () -> game.setScene(new CreditsMenuScene(game))),
                new MenuButton("Quit", x, y + 4 * dy, 256, 48, () -> System.exit(0))
        );

        userInput = new InputBoxEntity(16, 47, 316, "YOUR NAME", "YOUR NAME", 20);
        userInput.setInput("PLACEHOLDER");
        userInput.setOnBlur(() -> System.out.println("should set name now"));

        TextEntity skinLabel = new TextEntity("CHOOSE SKIN", Font.font(Fonts.PIXELIFY_SANS, FontWeight.MEDIUM, 18),
                                              Colors.FOREGROUND, TextAlignment.LEFT, VPos.TOP, 16, 113);

        skins = new SkinOptionsEntity[4];

        for (int i = 0; i < skins.length; i++) {
            int idx = i; // final for use in lambda
            // create at 0,0, since they are updates once the default color is selected.
            skins[i] = new SkinOptionsEntity(0, 0, Colors.PLAYER_SKINS.get(i), () -> handleSkinClick(idx));
        }

        addEntity(new ModalEntity(618, y, 348, 368 - dy, userInput, skinLabel, skins[0], skins[1], skins[2], skins[3]));
        addEntity(controller); // add the controller AFTER so input in the player configurator has priority
        skins[defaultColor].onPress(); // select the default color
    }

    @Override
    protected void render(GraphicsContext g) {
        // background
        g.setFill(Colors.GENERIC_BG);
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // render all other entities
        super.render(g);
    }

    /**
     * Handles the click event of the {@link SkinOptionsEntity} in the main menu player configuration modal.
     *
     * @param clickedSkin the index of the skin that was clicked.
     */
    private void handleSkinClick(int clickedSkin) {
        if (skins[clickedSkin].isClicked()) return;

        for (int i = 0; i < skins.length; i++) {
            skins[i].setSize(i == clickedSkin);
            skins[i].setPosition(
                    SKIN_OPTION_POSITIONS[clickedSkin][(i * 2)],
                    SKIN_OPTION_POSITIONS[clickedSkin][(i * 2) + 1]);
            skins[i].setClicked(i == clickedSkin);
        }

        // save the new color to the disk
        GameHandler.updateUserConfig(ConfigProto.UserConfig.newBuilder().buildPartial());
    }
}