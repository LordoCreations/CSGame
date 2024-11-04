package main.entities;

import main.Entity;
import main.GameScene;

public class Corpse extends Entity {
    protected long spawntime;
    protected GameScene scene;

    public Corpse(GameScene s, String r, int x, int y) {
        super(r, x, y);
        spawntime = System.currentTimeMillis();
        scene = s;
    } // Corpse

    @Override
    public void move(long delta){
        if (System.currentTimeMillis() >= spawntime + 3000) { scene.removeEntity(this); }
        else if (System.currentTimeMillis() >= spawntime + 2000) { sprite.setOpacity((float) (0.5 - 0.3 * Math.sin((System.currentTimeMillis() - spawntime) / 150.0))); }

        applyGravity(delta);
        takeKnockback();

    } // move
    
    protected void takeKnockback() {

    }

    protected void applyGravity(long delta) {
        update();
        dy += 1.75 * delta;

        moveY(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                y -= dy / 999;
                update();
            } // while
            dy = 0;
        } // if
    } // applyGravity

    public void setDx(double dx) {
        this.dx = dx;
    } // setDx

    @Override
    public void collidedWith(Entity o){} // collidedWith
} // class
