package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.core.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.function.Predicate;

import static com.logandhillon.fptgame.GameHandler.CANVAS_HEIGHT;
import static com.logandhillon.fptgame.GameHandler.CANVAS_WIDTH;

public class MenuHandler extends UIScene {
    private final GameHandler mgr;

    private MenuContent content;

    public MenuHandler(GameHandler mgr) {
        this.updateContent(new MainMenuScene(this));
        this.mgr = mgr;
    }

    public void updateContent(MenuContent content) {
        this.content = content; // store ptr to content for future reference
        this.clearEntities(true, (e)-> true);

        for(Entity e: content.getEntities()){
            addEntity(e);
        }
    }

    @Override
    protected void render(GraphicsContext g) {
        // bg
        g.setFill(Color.LIGHTGRAY); // TODO: use image once available by engine
        g.fillRect(0,0, CANVAS_WIDTH, CANVAS_HEIGHT);

        super.render(g);
    }

    public void createLobby(String roomName){
        this.mgr.createLobby(roomName);
    }

    public void startGame(){
        this.mgr.startGame();
    }

    public void clear(boolean discard, Predicate<Entity> predicate){
        clearEntities(discard, predicate);
    }

    public MenuContent getContent() {
        return content;
    }
}