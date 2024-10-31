package main.entities;
import main.Entity;

import java.util.ArrayList;

public class Weapon extends Entity {
    private int[] offsets;
    private Player following;
    protected int id;
    private int bulletSpeed;
    private int firingInterval;
    private long lastFired = -5000;
    private int bulletLife;
    private int bulletSpread;

    // weapon stats based on id
    private final static int[] FIRING_INTERVALS = {400, 80, 100, 90, 600, 1000};
    private final static int[] BULLET_SPEED = {800, 800, 1600, 1000, 1000};
    private final static int[] BULLET_LIFE = {500, 500, 1000, 500, 500};
    private final static int[] BULLET_SPREAD = {25, 50, 25, 40, 100};

    public Weapon(int id, Player p){
        super();

        super.setSprite(getWeaponURL(id));
        following = p;
        this.id = id;
        offsets = getOffsets(id);
        firingInterval = FIRING_INTERVALS[id];
        bulletSpeed = BULLET_SPEED[id];
        bulletLife = BULLET_LIFE[id];
        bulletSpread = BULLET_SPREAD[id];

    } // Weapon

    // follows the player
    public void move() {
        x = following.getX() + offsets[0] + (sprite.getDirection() ? -38 : 28);
        y = following.getY() + offsets[1];
    } // move

    private String getWeaponURL(int id) {
        switch (id) {
            case 1:
                return "mp5.png";
            case 2:
                return "ak47.png";
            case 3:
                return "honeybadger.png";
            case 4:
                return "defriender.png";
            default:
                return "m9.png";
        }
    }

    // get x and y offsets based on the weapon
    private int[] getOffsets(int id){
        switch(id){
            case 0:
                return new int[]{8, 32};
            case 1:
                return new int[]{0, 0};
        }
        return new int[]{0, 0};
    } // getOffsets

    public void setDirection(boolean direction) {
        this.sprite.setDirection(direction);
    }

    public void tryShoot(ArrayList<Entity> entities) {
        if ((System.currentTimeMillis() - lastFired) < firingInterval) {
            return;
        } // if

        // otherwise add a shot
        lastFired = System.currentTimeMillis();
        
        // TODO get actual weapon data, fix direction
        int randomSpread = (int)(Math.random() * 2 * bulletSpread + 1) - bulletSpread;
        switch (id) {
            case 4: // TODO shotgun has multple bullets
                for(int i = 0; i < 12; i++) {
                    int randomSpeed = (int)((Math.random() * 1.1 * bulletSpeed) + 0.9 * bulletSpeed);
                    Bullet bullet = new Bullet("bullet.png", (int) x, (int) y, following.getTeam(), bulletLife, (sprite.getDirection() ? -randomSpeed : randomSpeed), randomSpread);
                    entities.add(bullet);
                    randomSpread = (int)(Math.random() * 2 * bulletSpread + 1) - bulletSpread;
                } // for
                break;
            default:
                Bullet bullet = new Bullet("bullet.png", (int) x, (int) y, following.getTeam(), bulletLife, (sprite.getDirection() ? -bulletSpeed : bulletSpeed), randomSpread);
                entities.add(bullet);
        }
    }

    @Override
    public void collidedWith(Entity o){} // collidedWith
}
