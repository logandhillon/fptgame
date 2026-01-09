package com.logandhillon.fptgame.entity.physics;

/**
 * A physics entity is an {@link com.logandhillon.fptgame.entity.core.Entity} that (a) is affected by gravity, (b) has
 * collisions, and (c) has internal velocity that can be modulated to more (physically) accurately move the entity.
 *
 * @author Logan Dhillon
 */
public abstract class PhysicsEntity extends CollisionEntity {
    private static final float GRAVITY = 0.391f;
    private static final float MAX_VEL = 10f; // max scalar velocity
    private static final float PROBE_EPSILON = 0.01f; // how far from the obj to check for collisions during movement

    public float vx, vy;

    /**
     * Creates a collidable entity at the specified position with the specified hitbox
     *
     * @param x x-position (from left)
     * @param y y-position (from top)
     * @param w width of hitbox
     * @param h height of hitbox
     */
    public PhysicsEntity(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    // Track if entity is on the ground for jumping logic
    private boolean grounded;

    /**
     * Updates this physics object:
     * <p>
     * 1. apply gravity;
     * 2. clamp velocities to not exceed terminal velocity;
     * 3. check for collisions (and resolve if there is one);
     * 4. handle grounding
     *
     * @param dt the delta time: change in time (seconds) since the last frame
     */
    @Override
    public void onUpdate(float dt) {
        // Apply gravity only if not grounded
        if (!grounded) vy += GRAVITY;

        // Clamp velocities
        if (vx > MAX_VEL) vx = MAX_VEL;
        if (vx < -MAX_VEL) vx = -MAX_VEL;
        if (vy > MAX_VEL) vy = MAX_VEL;
        if (vy < -MAX_VEL) vy = -MAX_VEL;

        // collision handling
        float tx = x + vx;
        float ty = y + vy;
        var coll = parent.getCollisionAt(tx, ty, w, h, this);
        if (coll == null) {
            // move normally, apply velocities
            x += vx;
            y += vy;
        } else {
            // try to resolve collision by moving to the nearest edge
            float ld = Math.abs((tx + w) - coll.getX()); // distance from left edge
            float rd = Math.abs(tx - (coll.getX() + coll.getWidth())); // right
            float td = Math.abs((ty + h) - coll.getY()); // top
            float bd = Math.abs(ty - (coll.getY() + coll.getHeight())); // bottom

            // closest axis to resolve
            if (Math.min(ld, rd) < Math.min(td, bd)) {
                // x
                if (ld < rd) x += coll.getX() - (x + w);
                else x += (coll.getX() + coll.getWidth()) - x;
                vx = 0;
                y += vy;
            } else {
                // y
                if (td < bd) y += coll.getY() - (y + h);
                else y += (coll.getY() + coll.getHeight()) - y;
                vy = 0;
                x += vx;
            }
        }

        // check if grounded and reset vy if it is
        var e = parent.getCollisionAt(x, y + PROBE_EPSILON, w, h, this);
        grounded = e != null;
        if (grounded) vy = 0;
    }

    /**
     * @return if this object is grounded
     */
    public boolean isGrounded() {
        return grounded;
    }
}
