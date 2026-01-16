package com.logandhillon.fptgame.entity.game;

import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.fptgame.resource.Textures;
import com.logandhillon.logangamelib.entity.physics.CollisionEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Text;

/**
 * @author Logan Dhillon
 */
public class PlatformEntity extends CollisionEntity {

    private final int TEXTURE_SCALE = 40;
    private final int SHADOW_OFFSET = 5;
    /**
     * Creates a collidable entity at the specified position with the specified hitbox
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of hitbox
     * @param h height of hitbox
     */
    public PlatformEntity(float x, float y, float w, float h) {
        super(x, y, w, h);

        if(w % TEXTURE_SCALE != 0 || h % TEXTURE_SCALE != 0) {
            throw new IllegalArgumentException("Platform must have a widths and heights divisible by " + TEXTURE_SCALE);
        }
    }
    @Override
    protected void onRender(GraphicsContext g, float x, float y) {

        g.setFill(Colors.FOREGROUND_TRANS);
        int v;
        if(w > h) { // render right
            v = (int)(w / TEXTURE_SCALE);
            for(int i = 0; i < v; i++) {
                g.fillRect(x + (i * TEXTURE_SCALE) - SHADOW_OFFSET, y + SHADOW_OFFSET, TEXTURE_SCALE, TEXTURE_SCALE); // render shadow right
                Textures.UNDERGROUND.draw(g, 13, 3, x + (i * TEXTURE_SCALE), y, TEXTURE_SCALE, TEXTURE_SCALE);
            }
        }
        else { // render down
            v = (int)(h / TEXTURE_SCALE);
            for(int i = 0; i < v; i++) {
                g.fillRect(x - SHADOW_OFFSET, y + (i * TEXTURE_SCALE) + SHADOW_OFFSET, TEXTURE_SCALE, TEXTURE_SCALE); //render shadow down
                Textures.UNDERGROUND.draw(g, 13, 3, x, y + (i * TEXTURE_SCALE), TEXTURE_SCALE, TEXTURE_SCALE);
            }
        }
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }
}
