package main.entities;

import main.Entity;
import main.GameScene;

import static main.Game.HEIGHT;

public class Chest extends Corpse {

    public Chest(GameScene s, String r, int x, int y) {
        super(s, r, x, y);

        while (scene.touchingWall(this)) {
            y += 1;
            if (y > HEIGHT) break;
        }
    }

    @Override
    public void move(long delta) {
        super.applyGravity(delta);
        if (y > HEIGHT) scene.removeEntity(this);
        if (System.currentTimeMillis() >= spawntime + 9000) scene.removeEntity(this);
    }

    @Override
    public void collidedWith(Entity o) {
        if (o instanceof Player) {
            ((Player) o).setWeapon((int) (Math.random() * 6));
        }

    }
}
