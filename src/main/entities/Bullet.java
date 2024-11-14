package main.entities;

import main.Game;
import main.GameTime;
import main.utility.AudioManager;
import main.Entity;
import main.GameScene;

/**
 * <h1>Bullet</h1>
 * <hr/>
 * Weapon bullet
 *
 * @author Anthony and Luke
 * @since 12-11-2024
 * @see Entity
 */

public class Bullet extends Entity {
    private final int team;
    private final int lifeTime;
    private long spawnTime;
    private final int damage;
    private boolean explosive;
    private final GameScene scene;
    private boolean ignoreWalls = false;
    private final int knockback;

    /**
     * Constructor for a Bullet
     * @param r Sprite image reference
     * @param x x position
     * @param y y position
     * @param team team of the player firing the bullet
     * @param lifeTime time until bullet naturally despawns
     * @param speed speed of the bullet
     * @param spread y movement of the bullet
     * @param scene scene the bullet is created in
     * @param damage damage inflicted on enemy players
     * @param knockback knockback inflicted on enemy players
     */
    public Bullet(String r, int x, int y, int team, int lifeTime, int speed, int spread, GameScene scene, int damage, int knockback) {
        super("weapons/" + r + ".png", x, y);
        this.team = team;
        this.lifeTime = lifeTime;
        spawnTime = GameTime.getTime();
        dx = speed;
        dy = spread;

        explosive = false;
        this.damage = damage;
        this.scene = scene;
        this.knockback = knockback;

        if (speed < 0) {
            sprite.setDirection(true);
        } // if
    } // Bullet

    /* Getters and Setters */
    public int getTeam() {
        return team;
    } // getTeam

    public int getWidth() {
        return sprite.getWidth();
    } // getTeam

    public void setX(int x) {
        this.x = x;
    } // setX

    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    } // setExplosive

    public void setIgnoreWalls(boolean value) {
        ignoreWalls = value;
    } // setIgnoreWalls

    /**
     * Moves the bullet
     * @param delta milliseconds since last call
     */
    @Override
    public void move(long delta) {
        if (explosive) {
            dx *= Math.pow(0.95, (delta / 3.0));
            dx = dx > 0 ? Math.max(dx, 700) : Math.min(dx, -700); // Rockets gradually slow down
        } // if
        super.move(delta);
        if (GameTime.getTime() > spawnTime + lifeTime) {
            scene.removeEntity(this);
        } // if
    } // move

    /**
     * damage and knockback if collided with player
     * @param o object that the bullet collided with
     */
    @Override
    public void collidedWith(Entity o) {
        if (o != null && !((Player) o).spawnProt && collidesWith(o) && team != ((Player) o).getTeam()) {
            ((Player) o).hp -= damage;
            ((Player) o).setKbDx(dx > 0 ? knockback : -knockback);
            collidedWith();
            o.collidedWith(this);
        } // if
    }  // collidedWith

    /**
     * Overloaded to explode on impact if bullet is explosive
     * Allows for collisions with non-entities such as walls
     */
    public void collidedWith() {
        if (explosive) {
            AudioManager.playSound("explosion.wav", false);
            for (int i = 0; i < 16; i++) {
                double angle = Math.random() * 2 * Math.PI; // Random angle in radians
                double speed = 300 * (Math.random() * 1.5 + 1); // Random speed within a range
                Bullet explosion = new Bullet("explosion", (int) x, (int) y, team, 100,
                        (int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle)), scene, 2, 500);
                explosion.setIgnoreWalls(true);
                scene.entities.add(explosion);
            } // for
        } // if

        if (!ignoreWalls) scene.removeEntity(this);

    }  // collidedWith

} // class


