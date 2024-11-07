package main.entities;

import main.Entity;
import main.GameScene;

public class Bullet extends Entity {
    private final int team;
    private final int lifeTime;
    private final long spawnTime = System.currentTimeMillis();
    private final int damage;
    private boolean explosive;
    private final GameScene scene;
    private boolean ignoreWalls = false;

    public Bullet(String r, int x, int y, int team, int lifeTime, int speed, int spread, GameScene scene, int damage) {
        super(r, x, y);
        this.team = team;
        this.lifeTime = lifeTime;
        dx = speed;
        dy = spread;

        explosive = false;
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

    public double getDx() {
        return dx;
    } // getDx

    public void setX(int x) {
        this.x = x;
    } // setX

    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    } // setExplosive

    public int getDamage() {
        return damage;
    } // getDamage

    public void canGoThroughWalls(boolean value) {
        ignoreWalls = value;
    }

    @Override
    public void move(long delta) {
        super.move(delta);
        if (System.currentTimeMillis() > spawnTime + lifeTime) {
            scene.removeEntity(this);
        } // if
    } // move

    @Override
    public void collidedWith(Entity o) {
        if (!((Player) o).spawnProt) {
            ((Player) o).hp -= damage;
        }
        collidedWith();
    }  // collidedWith

    public void collidedWith() {
        if (explosive) {
            for (int i = 0; i < 8; i++) {
                double angle = Math.random() * 2 * Math.PI; // Random angle in radians
                double speed = 100 * (Math.random() * 1.5 + 1); // Random speed within a range
                Bullet explosion = new Bullet("weapons/explosion.png", (int) x, (int) y, team, 100,
                        (int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle)), scene, 10);
                explosion.canGoThroughWalls(true);
                scene.entities.add(explosion);
            } // for
        } // if

        if (!ignoreWalls) scene.removeEntity(this);

    }  // collidedWith

} // class


