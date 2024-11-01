package main.entities;

import main.Entity;
import main.GameScene;

public class Bullet extends Entity {
    private int team;
    private int lifeTime;
    private long spawnTime = System.currentTimeMillis();
    private int damage;

    private final GameScene scene;

    public Bullet(String r, int x, int y, int team, int lifeTime, int speed, int spread, GameScene scene, int damage) {
        super(r, x, y);
        this.team = team;
        this.lifeTime = lifeTime;
        dx = speed;
        dy = spread;
        this.damage = damage;
        this.scene = scene;

        if (speed < 0) {
            sprite.setDirection(true);
        }
    } // Bullet

    public int getTeam() {
        return team;
    }

    @Override
    public void move(long delta){
        super.move(delta);
        if(System.currentTimeMillis() > spawnTime + lifeTime){
            scene.removeEntity(this);
        } // if
    } // move

    @Override
    public void collidedWith(Entity o){
        ((Player) o).hp -= damage;
    }  // collidedWith

} // class


