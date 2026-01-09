package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.MenuController;
import com.logandhillon.fptgame.entity.core.Entity;
import com.logandhillon.fptgame.entity.ui.component.InputBoxEntity;
import com.logandhillon.fptgame.entity.ui.component.MenuButton;
import com.logandhillon.fptgame.entity.ui.component.ModalEntity;
import com.logandhillon.fptgame.networking.proto.ConfigProto;

import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * The main menu allows the user to navigate to other submenus, play or quit the game, and view game branding.
 *
 * @author Logan Dhillon, Jack Ross
 */
public class MainMenuScene implements MenuContent {
    private static InputBoxEntity userInput;
    private final Entity[] entities;
    /**
     * Creates a new main menu
     *
     * @param menu the main class that can switch scenes, manage connections, etc.
     */
    public MainMenuScene(MenuHandler menu) {
        int defaultColor = 0;
        float x = (CANVAS_WIDTH - 652) / 2f;
        int dy = 48 + 16; // ∆y per button height
        int y = 176;


        MenuController controller = new MenuController(
                new MenuButton("Host Game", x, y, 256, 48, () -> menu.setContent(new HostGameScene(menu))),
                new MenuButton("Join Game", x, y + dy, 256, 48, () -> menu.setContent(
                        new JoinGameScene(menu, addr -> System.out.println("NOT IMPLEMENTED!")))),
                new MenuButton("Settings", x, y + 2 * dy, 256, 48, () -> {
                }),
//                new MenuButton("Credits", x, y + 3 * dy, 256, 48, () -> menu.updateContent(new CreditsMenuScene(menu))),
                new MenuButton("Quit", x, y + 4 * dy, 256, 48, () -> System.exit(0))
        );

        userInput = new InputBoxEntity(16, 47, 316, "YOUR NAME", "YOUR NAME", 20);
        userInput.setInput(GameHandler.getUserConfig().getName());
        userInput.setOnBlur(() -> GameHandler.updateUserConfig(
                ConfigProto.UserConfig.newBuilder().setName(userInput.getInput()).buildPartial()));


        entities = new Entity[]{new ModalEntity(618, y, 348, 368 - dy, userInput), controller};
    }

    @Override
    public Entity[] getEntities() {
        return entities;
    }

    public static InputBoxEntity getUserInput() {
        return userInput;
    }
}