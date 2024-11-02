package main.entities;

import main.Entity;
import main.GameScene;

public class Corpse extends Entity {
    private long spawntime;
    private GameScene scene;

    public Corpse(GameScene s, String r, int x, int y) {
        super(r, x, y);
        spawntime = System.currentTimeMillis();
        scene = s;
    } // Corpse

    @Override
    public void move(long delta){
        if (System.currentTimeMillis() >= spawntime + 3000) { scene.removeEntity(this); };
    } // move

    @Override
    public void collidedWith(Entity o){} // collidedWith
} // class
