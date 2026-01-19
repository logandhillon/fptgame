package com.logandhillon.fptgame.entity.game;

import com.logandhillon.fptgame.networking.proto.LevelProto;
import com.logandhillon.logangamelib.gfx.AtlasTile;

import static com.logandhillon.logangamelib.entity.physics.PhysicsEntity.PX_PER_METER;

/**
 * @author Logan Dhillon
 */
public class MovingPlatformEntity extends PlatformEntity {
    private final float destX;
    private final float destY;
    private final float originX;
    private final float originY;
    private final float speed;

    /**
     * true=going towards destination; false=going towards original pos
     */
    private boolean goingTowardsDest;
    private float   tx;
    private float   ty;

    /**
     * Creates a moving platform
     *
     * @param texture texture to tile over this platform
     * @param x       x-position (from left)
     * @param y       y-position (from top)
     * @param w       width of hitbox
     * @param h       height of hitbox
     * @param destX   x-pos of destination that the platform will move to
     * @param destY   y-pos of destination that the platform will move to
     * @param speed   speed of platform in m/s
     * @param color   (optional) color of platform: changes which player can interact with it.
     */
    public MovingPlatformEntity(AtlasTile texture, float x, float y, float w, float h, float destX, float destY,
                                float speed, boolean goingTowardsDest, LevelProto.Color color) {
        super(texture, x, y, w, h, color);
        this.originX = x;
        this.originY = y;
        this.destX = destX;
        this.destY = destY;
        this.speed = speed * PX_PER_METER;
        setGoingTowardsDest(goingTowardsDest);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);

        float dx = tx - x;
        float dy = ty - y;
        float delta = speed * dt; // meters to travel
        float dist = (float)Math.sqrt(dx * dx + dy * dy);

        // if already at destination, return
        if (dist == 0f) return;

        if (dist <= delta) setPosition(tx, ty);
        else translate(dx / dist * delta, dy / dist * delta);
    }

    public void setGoingTowardsDest(boolean goingTowardsDest) {
        this.goingTowardsDest = goingTowardsDest;
        tx = goingTowardsDest ? destX : originX;
        ty = goingTowardsDest ? destY : originY;
    }

    public void invertGoingTowardsDest() {
        setGoingTowardsDest(!goingTowardsDest);
    }

    @Override
    public LevelProto.LevelObject serialize() {
        return LevelProto.LevelObject
                .newBuilder()
                .setX(x).setY(y)
                .setMovingPlatform(LevelProto.MovingPlatform
                                           .newBuilder()
                                           .setH(h)
                                           .setW(w)
                                           .setTexture(texture.serialize())
                                           .setColor(color)
                                           .setDestX(destX)
                                           .setDestY(destY)
                                           .setSpeed(speed)
                                           .build())
                .build();
    }

    public static MovingPlatformEntity load(LevelProto.LevelObject msg) {
        return new MovingPlatformEntity(
                AtlasTile.load(msg.getMovingPlatform().getTexture()),
                msg.getX(),
                msg.getY(),
                msg.getMovingPlatform().getW(),
                msg.getMovingPlatform().getH(),
                msg.getMovingPlatform().getDestX(),
                msg.getMovingPlatform().getDestY(),
                msg.getMovingPlatform().getSpeed(),
                msg.getMovingPlatform().getGoingTowardsDest(),
                msg.getMovingPlatform().getColor());
    }
}
