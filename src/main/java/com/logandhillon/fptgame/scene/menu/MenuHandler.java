package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.core.Entity;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

/**
 * The menu handler is the only {@link UIScene} in the menu screens. It allows users to switch between menus
 * without switching scenes by changing between each menu's {@link MenuContent}, allowing for easier menu management
 * and smoother transitions.
 *
 * @author Jack Ross
 * @see MenuContent
 */
public class MenuHandler extends UIScene {
    private final GameHandler mgr;

    private MenuContent content;

    public MenuHandler(GameHandler mgr) {
        this.content = new MainMenuContent(this);
        this.mgr = mgr;
    }

    /**
     * Runs when a new scene is initialized
     *
     * @param scene JAVAFX SCENE from engine
     */
    @Override
    public void onBuild(Scene scene) {
        super.onBuild(scene);
        setContent(content); // set the content to the default content
    }

    /**
     * Resets the content of the game scene to display the current menu
     *
     * @param content The content (entities) of any given menu
     */
    public void setContent(MenuContent content) {
        this.content = content; // store ptr to content for future reference
        this.clearEntities(true, (e) -> true);
        this.clearAllHandlers();

        for (Entity e: content.getEntities()) addEntity(e);
        this.addMouseEvents(true); // re-bind the mouse events (they were just removed)
    }

    /**
     * Renders the constants for all menus
     *
     * @param g the graphical context to render to.
     */
    @Override
    protected void render(GraphicsContext g) {
        // bg
        g.setFill(Color.LIGHTGRAY); // TODO: use image once available by engine
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        super.render(g);
    }

    /**
     * Creates a joinable game lobby using the context of the game engine
     *
     * @param roomName Name of the lobby
     *
     * @see GameHandler
     */
    public void createLobby(String roomName) {
        this.mgr.createLobby(roomName);
    }

    /**
     * Communicates with engine to start the game
     */
    public void startGame() {
        this.mgr.startGame();
    }

    /**
     * @return menu content that will be set or disposed of
     */
    public MenuContent getContent() {
        return content;
    }

    /**
     * Communicates with engine to return back to main menu
     *
     * @see CreditsMenuContent
     */
    public void goToMainMenu() {
        this.mgr.goToMainMenu();
    }
}