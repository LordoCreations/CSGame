package main.entities;

import main.Entity;
import main.GameScene;
import main.GameTime;

/**
 * <h1>Corpse</h1>
 * <hr/>
 * Corpse that spawns on player death
 *
 * @author Anthony and Luke
 * @since 012-11-2024
 * @see Entity
 */

public class Corpse extends Entity {
    protected long spawntime;
    protected GameScene scene;

    /**
     * Constructor for a new Corpse
     * @param s scene the corpse is spawned in
     * @param r reference to the sprite image
     * @param x x position
     * @param y y position
     */
    public Corpse(GameScene s, String r, int x, int y) {
        super(r, x, y);
        spawntime = GameTime.getTime();
        scene = s;
    } // Corpse

    /**
     * Moves the corpse
     * @param delta milliseconds since last call
     */
    @Override
    public void move(long delta){
        if (GameTime.getTime() >= spawntime + 3000) { scene.removeEntity(this); }
        else if (GameTime.getTime() >= spawntime + 2000) { sprite.setOpacity((float) (0.5 - 0.3 * Math.sin((GameTime.getTime() - spawntime) / 150.0))); }

        applyGravity(delta);
        fallThrough();
    } // move

    /**
     * Moves the corpse
     */
    @Override
    public void update() {
        hitbox.setRect(x + 4, y + 8, 48, 48);
    } // update

    /**
     * Applies gravity to the corpse
     * @param delta milliseconds since last call
     */
    protected void applyGravity(long delta) {
        update();
        dy += 1.75 * delta;

        moveY(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                y += (dy > 0 ? -1 : 1) ;
                update();
            } // while
            dy = 0;
        } // if
    } // applyGravity

    /**
     * Collision detection - unused
     * @param o Object the corpse collided with
     */
    @Override
    public void collidedWith(Entity o){} // collidedWith
} // class
