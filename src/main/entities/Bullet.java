package main.entities;

import main.Entity;
import main.Game;

public class Bullet extends Entity {
    private int team;
    private int lifeTime;
    private long spawnTime = System.currentTimeMillis();
    // TODO set damage to weapon dmg

    private int damage = 5;

    public Bullet(String r, int x, int y, int team, int lifeTime, int speed, int spread) {
        super(r, x, y);
        this.team = team;
        this.lifeTime = lifeTime;
        dx = speed;
        dy = spread;
    } // Bullet

    public int getTeam() {
        return team;
    }

    @Override
    public void move(long delta){
        moveX(delta);
        moveY(delta);
        if(System.currentTimeMillis() > spawnTime + lifeTime){
//            Game.removeEntity(this);
        } // if
    } // move

    @Override
    public void collidedWith(Entity o){
        ((Player) o).hp -= damage;
    }  // collidedWith
} // class


