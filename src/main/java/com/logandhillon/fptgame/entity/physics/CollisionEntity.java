package com.logandhillon.fptgame.entity.physics;

import com.logandhillon.logangamelib.entity.Entity;

/**
 * A collision entity is a static {@link Entity} that interacts with other collision entities, but does not move.
 * You may think of it like a static physics entity.
 *
 * @author Logan Dhillon
 */
public abstract class CollisionEntity extends Entity {
    protected final float w;
    protected final float h;

    /**
     * Creates a collidable entity at the specified position with the specified hitbox
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of hitbox
     * @param h height of hitbox
     */
    public CollisionEntity(float x, float y, float w, float h) {
        super(x, y);
        this.w = w;
        this.h = h;
    }

    /**
     * Checks if this entity is colliding with another entity
     *
     * @param e the entity to check against
     * @return is collision happening
     */
    public boolean checkCollision(CollisionEntity e) {
        return parent.checkCollision(this, e);
    }

    /**
     * Checks if this is colliding with ANY other entity
     *
     * @return entity that this is colliding with, or null
     */
    public CollisionEntity getCollision() {
        return parent.getEntityCollision(this);
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }
}
