package com.logandhillon.fptgame.entity.game;

import com.logandhillon.fptgame.level.LevelObject;
import com.logandhillon.fptgame.networking.proto.LevelProto;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.logangamelib.gfx.AtlasTile;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.fptgame.resource.Textures.OBJ_SCALE;

/**
 * A platform entity is a static {@link com.logandhillon.logangamelib.entity.physics.CollisionEntity} that is available
 * in {@link com.logandhillon.fptgame.networking.proto.LevelProto.LevelData} and can be added to levels dynamically.
 *
 * @author Logan Dhillon
 */
public class PlatformEntity extends LevelObject {
    private final int       shadowOffset;
    private final AtlasTile texture;

    /**
     * Creates a collidable entity at the specified position with the specified hitbox
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of hitbox
     * @param h height of hitbox
     */
    public PlatformEntity(AtlasTile texture, float x, float y, float w, float h) {
        super(x, y, w, h);

        if (w % OBJ_SCALE != 0 || h % OBJ_SCALE != 0)
            throw new IllegalArgumentException("Platform must have a width and height divisible by " + OBJ_SCALE);
        this.texture = texture;
        this.shadowOffset = 0;
    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Colors.FOREGROUND_TRANS_40);
        if (w > h) { // render right
            for (int i = 0; i < w / OBJ_SCALE; i++) {
                if (shadowOffset != 0) // render shadow right
                    g.fillRect(x + (i * OBJ_SCALE) - shadowOffset, y + shadowOffset, OBJ_SCALE, OBJ_SCALE);
                texture.draw(g, x + (i * OBJ_SCALE), y, OBJ_SCALE, OBJ_SCALE);
            }
        } else { // render down
            for (int i = 0; i < h / OBJ_SCALE; i++) {
                if (shadowOffset != 0) // render shadow down
                    g.fillRect(x - shadowOffset, y + (i * OBJ_SCALE) + shadowOffset, OBJ_SCALE, OBJ_SCALE);
                texture.draw(g, x, y + (i * OBJ_SCALE), OBJ_SCALE, OBJ_SCALE);
            }
        }
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public LevelProto.LevelObject serialize() {
        return LevelProto.LevelObject
                .newBuilder()
                .setX(x)
                .setY(y)
                .setPlatform(LevelProto.Platform
                                     .newBuilder()
                                     .setH(h)
                                     .setW(w)
                                     .setTexture(texture.serialize())
                                     .build())
                .build();
    }

    public static PlatformEntity load(LevelProto.LevelObject msg) {
        return new PlatformEntity(
                AtlasTile.load(msg.getPlatform().getTexture()),
                msg.getX(),
                msg.getY(),
                msg.getPlatform().getW(),
                msg.getPlatform().getH());
    }
}
