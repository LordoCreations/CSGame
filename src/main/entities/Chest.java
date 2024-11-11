package main.entities;

import main.Entity;
import main.Game;
import main.GameScene;


import static main.Game.HEIGHT;

public class Chest extends Corpse {

    public Chest(GameScene s, int x, int y) {
        super(s, "chest.png", x, y);
        while (scene.touchingWall(this)) {
            y += 1;
            if (y > HEIGHT) break;
        }
    }

    @Override
    public void move(long delta) {
        super.applyGravity(delta);
        if (y > HEIGHT) scene.removeEntity(this);

        // TODO fix repeated code from Corpse
        if (System.currentTimeMillis() >= spawntime + 9000) scene.removeEntity(this);
        else if (System.currentTimeMillis() >= spawntime + 7000) { sprite.setOpacity((float) (0.5 - 0.3 * Math.sin((System.currentTimeMillis() - spawntime) / 150.0))); }
    }

    @Override
    public void collidedWith(Entity o) {
        if (o instanceof Player) {
            ((Player) o).setWeapon((int) (Math.random() * Game.weaponCount - 1) + 1);
        } // if

        scene.removeEntity(this);
    } // collidedWith

    @Override
    public void update() {
        hitbox.setRect(x, y, 50, 45);
    }
} // class
