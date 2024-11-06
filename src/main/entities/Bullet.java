package main.entities;

import main.Entity;
import main.GameScene;

public class Bullet extends Entity {
    private final int team;
    private final int lifeTime;
    private final long spawnTime = System.currentTimeMillis();
    private final int damage;
    private boolean explosive;
    private final int direction;
    private final GameScene scene;

    public Bullet(String r, int x, int y, int team, int lifeTime, int speed, int spread, GameScene scene, int damage) {
        super(r, x, y);
        this.team = team;
        this.lifeTime = lifeTime;
        dx = speed;
        dy = spread;
        direction = (speed > 0) ? 1 : -1;

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
            for (int i = 0; i < 30; i++) {
                double angle = Math.random() * 2 * Math.PI; // Random angle in radians
                double speed = 100 * (5 + Math.random() * 5); // Random speed within a range
                System.out.printf("X: %.2f Y: %.2f, Speed: %.2f, Angle: %.2f%n", x-this.dx, y, speed, angle);
                Bullet explosion = new Bullet("weapons/explosion.png", (int) (x - direction * sprite.getWidth()), (int) y, team, 100,
                        (int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle)), scene, 10);
                scene.entities.add(explosion);
            } // for
        } // if
        scene.removeEntity(this);

    }  // collidedWith

} // class


