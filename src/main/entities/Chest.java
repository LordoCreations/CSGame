package main.entities;

import main.Entity;
import main.Game;
import main.GameScene;

/**
 * <h1>Chest</h1>
 * <hr/>
 * Loot chests that give a randomized weapon - cannot be pistol
 *
 * @author Anthony and Luke
 * @since 012-11-2024
 * @see Corpse
 */

import static main.Game.HEIGHT;

public class Chest extends Corpse {

    /**
     * Constructor for a loot Chest
     * @param s scene the Chest is created in
     * @param x x position
     * @param y y position
     */
    public Chest(GameScene s, int x, int y) {
        super(s, "chest.png", x, y);
        while (scene.touchingWall(this)) {
            y += 1;
            if (y > HEIGHT) break;
        } // while
    } // Chest

    /**
     * Moves the chest
     * @param delta milliseconds since last call
     */
    @Override
    public void move(long delta) {
        super.applyGravity(delta);
        if (y > HEIGHT) scene.removeEntity(this);

        // TODO fix repeated code from Corpse
        if (System.currentTimeMillis() >= spawntime + 9000) scene.removeEntity(this);
        else if (System.currentTimeMillis() >= spawntime + 7000) { sprite.setOpacity((float) (0.5 - 0.3 * Math.sin((System.currentTimeMillis() - spawntime) / 150.0))); }
    } // move

    /**
     * give weapons to player
     * @param o object that the chest collided with
     */
    @Override
    public void collidedWith(Entity o) {
        ((Player) o).setWeapon((int) (Math.random() * Game.weaponCount - 1) + 1);
        scene.removeEntity(this);
    } // collidedWith

    /**
     * Updates the position of the hitbox
     */
    @Override
    public void update() {
        hitbox.setRect(x, y, 50, 45);
    } // update
} // class
