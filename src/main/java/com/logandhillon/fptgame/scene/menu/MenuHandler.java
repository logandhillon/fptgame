package com.logandhillon.fptgame.scene.menu;

import com.logandhillon.fptgame.GameHandler;
import com.logandhillon.fptgame.engine.UIScene;
import com.logandhillon.fptgame.entity.core.Entity;

import java.util.function.Predicate;

public class MenuHandler extends UIScene {
    private final GameHandler mgr;

    public MenuHandler(GameHandler mgr) {
        this.updateContent(new MainMenuScene(this));
        this.mgr = mgr;
    }

    public void updateContent(MenuContent content) {
        this.clearEntities(true, (e)-> true);

        for(Entity e: content.getEntities()){
            addEntity(e);
        }
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
}