package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.core.Entity;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

public class MenuHandler extends UIScene {
    private final GameHandler mgr;

    private MenuContent content;

    public MenuHandler(GameHandler mgr) {
        this.content = new MainMenuScene(this);
        this.mgr = mgr;
    }

    @Override
    public void onBuild(Scene scene) {
        super.onBuild(scene);
        setContent(content); // set the content to the default content
    }

    public void setContent(MenuContent content) {
        this.content = content; // store ptr to content for future reference
        this.clearEntities(true, (e) -> true);
        this.clearAllHandlers();

        for (Entity e: content.getEntities()) addEntity(e);
        this.addMouseEvents(true); // re-bind the mouse events (they were just removed)
    }

    @Override
    protected void render(GraphicsContext g) {
        // bg
        g.setFill(Color.LIGHTGRAY); // TODO: use image once available by engine
        g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        super.render(g);
    }

    public void createLobby(String roomName) {
        this.mgr.createLobby(roomName);
    }

    public void startGame() {
        this.mgr.startGame();
    }

    public MenuContent getContent() {
        return content;
    }
}