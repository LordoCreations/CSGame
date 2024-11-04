package main.entities;

import main.Entity;
import main.GameScene;

public class Bullet extends Entity {
    private final int team;
    private final int lifeTime;
    private final long spawnTime = System.currentTimeMillis();
    private final int damage;

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
    } // getTeam

    public int getWidth() {
        return sprite.getWidth();
    } // getTeam

    public double getDx(){
        return dx;
    } // getDx

    public void setX(int x) {
        this.x = x;
    }

    public int getDamage() { return damage; } // getDamage

    @Override
    public void move(long delta){
        super.move(delta);
        if(System.currentTimeMillis() > spawnTime + lifeTime){
            scene.removeEntity(this);
        } // if
    } // move

    @Override
    public void collidedWith(Entity o){ if(!((Player) o).spawnProt) { ((Player) o).hp -= damage; }}  // collidedWith

} // class


