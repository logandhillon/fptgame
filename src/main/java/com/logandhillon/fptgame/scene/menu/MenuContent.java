package com.logandhillon.fptgame.scene.menu;
import com.logandhillon.logangamelib.entity.Entity;
import javafx.scene.canvas.GraphicsContext;

/**
 * Interface that handles each menu screen's content. Allows parent {@link MenuHandler} to use content to update
 * the master game scene
 *
 * @author Jack Ross
 */
public interface MenuContent {

    /**
     * Allows {@link MenuHandler} to access and display the entities of a menu
     * @return All Menu Entities (for that specific menu)
     */
    Entity[] getEntities();


    default void onRender(GraphicsContext g){}

    /**
     * Runs when this menu content is displayed by its parent {@link MenuHandler}
     */
    default void onShow() {}

    default void onDestroy(){}
}

