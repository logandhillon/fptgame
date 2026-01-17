package com.logandhillon.fptgame.entity.game;

import com.logandhillon.fptgame.level.LevelObject;
import com.logandhillon.fptgame.networking.proto.LevelProto;
import com.logandhillon.fptgame.resource.Colors;
import com.logandhillon.logangamelib.gfx.AtlasTile;
import javafx.scene.canvas.GraphicsContext;

import static com.logandhillon.fptgame.resource.Textures.TEXTURE_SCALE;

/**
 * @author Logan Dhillon, Jack Ross
 */
public class PlatformEntity extends LevelObject {
    private final        int shadowOffset;
    private final        AtlasTile texture;

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

        if (w % TEXTURE_SCALE != 0 || h % TEXTURE_SCALE != 0) {
            throw new IllegalArgumentException("Platform must have a width and height divisible by " + TEXTURE_SCALE);
        }
        this.texture = texture;
        this.shadowOffset = 0;
    }

//    /**
//     * Collidable entity with shadow
//     *
//     * @param shadowOffset distance shadow is from platform
//     */
//    public PlatformEntity(TextureAtlas theme, float x, float y, float w, float h, int row, int col, int
//    shadowOffset) {
//        super(x, y, w, h);
//
//        if (w % TEXTURE_SCALE != 0 || h % TEXTURE_SCALE != 0) {
//            throw new IllegalArgumentException("Platform must have a width and height divisible by " + TEXTURE_SCALE);
//        }
//
//        this.theme = theme;
//        this.themebg = null;
//        this.row = row;
//        this.col = col;
//        this.shadowOffset = shadowOffset;
//        this.bgRow = -1;
//        this.bgCol = -1;
//    }

    @Override
    protected void onRender(GraphicsContext g, float x, float y) {
        g.setFill(Colors.FOREGROUND_TRANS_40);
        int tiles;
        if (w > h) { // render right
            tiles = (int)(w / TEXTURE_SCALE);
            if (shadowOffset != 0) {
                for (int i = 0; i < tiles; i++) {
                    g.fillRect(
                            x + (i * TEXTURE_SCALE) - shadowOffset, y + shadowOffset, TEXTURE_SCALE,
                            TEXTURE_SCALE); // render shadow right
                }
            }
            for (int i = 0; i < tiles; i++) {
                texture.draw(g, x + (i * TEXTURE_SCALE), y, TEXTURE_SCALE, TEXTURE_SCALE);
            }
        } else { // render down
            tiles = (int)(h / TEXTURE_SCALE);
            if (shadowOffset != 0) {
                for (int i = 0; i < tiles; i++) {
                    g.fillRect(
                            x - shadowOffset, y + (i * TEXTURE_SCALE) + shadowOffset, TEXTURE_SCALE,
                            TEXTURE_SCALE); //render shadow down
                }
            }
            for (int i = 0; i < tiles; i++) {
                texture.draw(g, x, y + (i * TEXTURE_SCALE), TEXTURE_SCALE, TEXTURE_SCALE);
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
