package main.entities;
import main.Entity;
import main.GameScene;

import java.util.ArrayList;

public class Weapon extends Entity {
    private final int[] offsets;
    private final Player following;
    protected int id;
    private final int bulletSpeed;
    private final int firingInterval;
    private long lastFired = -5000;
    private final int bulletLife;
    private final int bulletSpread;
    private final int bulletDamage;
    private final int[] bulletOffsets;
    private int ammo;
    private int recoil;
    private int weight;

    // weapon stats based on id
    // m9, mp5, ak47, honeybadger, defriender, barrettm82
    private final static int[] FIRING_INTERVALS = {400, 80, 160, 90, 600, 1500};
    private final static int[] BULLET_SPEED = {1400, 1400, 1600, 1200, 2000, 2800};
    private final static int[] BULLET_LIFE = {550, 500, 1000, 650, 80, 2000};
    private final static int[] BULLET_SPREAD = {35, 60, 35, 45, 600, 0};
    private final static int[] BULLET_DAMAGE = {20, 15, 25, 10, 5, 100};
    private final static int[][] BULLET_OFFSETS = {{12, 5}, {40, 2}, {48, 6}, {48, 6}, {48, 7}, {68, 6}};
    private final static int[] MAX_AMMO = {1, 30, 30, 30, 10, 5};
    private final static int[] RECOIL = {320, 320, 720, 160, 1280, 1600};
    private final static int[] WEIGHT = {0, 20, 50, 30, 40, 80};

    private final GameScene scene;

    public Weapon(int id, Player p, GameScene scene){
        super();

        super.setSprite(getWeaponURL(id));
        following = p;
        this.id = id;
        offsets = getOffsets(id);
        firingInterval = FIRING_INTERVALS[id];
        bulletSpeed = BULLET_SPEED[id];
        bulletLife = BULLET_LIFE[id];
        bulletSpread = BULLET_SPREAD[id];
        bulletDamage = BULLET_DAMAGE[id];
        bulletOffsets = BULLET_OFFSETS[id];
        recoil = RECOIL[id];
        weight = WEIGHT[id];

        this.scene = scene;
        ammo = getMaxAmmo();

    } // Weapon

    public int getAmmo() {
        return ammo;
    } // getHp

    public int getMaxAmmo() {
        return MAX_AMMO[id];
    } // getMaxAmmo

    public int getFiringDistance() {
        return bulletLife * bulletSpeed;
    }

    public int getWeaponSpread() {
        return 50 + 30*bulletSpread/bulletSpeed;
    }

    // follows the player
    public void move() {
        x = following.getX() + 28 +(sprite.getDirection() ? -offsets[0]-sprite.getWidth() : offsets[0]);
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
            case 5:
                return "barrettm82.png";
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
                return new int[]{-28, 34};
            case 2:
                return new int[]{-26, 35};
            case 3:
                return new int[]{-23, 32};
            case 4:
                return new int[]{-11, 28};
            case 5:
                return new int[]{-17, 25};
        }
        return new int[]{0, 0};
    } // getOffsets

    public void setDirection(boolean direction) {
        this.sprite.setDirection(direction);
    }

    public int getWeight() { return weight; }

    public void tryShoot(ArrayList<Entity> entities) {
        if ((System.currentTimeMillis() - lastFired) < firingInterval) {
            return;
        } // if

        if (id != 0) ammo -= 1;

        following.setRecoilDx(following.getDirection() ? recoil : - recoil);

        // otherwise add a shot
        lastFired = System.currentTimeMillis();
        
        // TODO get actual weapon data
        switch (id) {
            case 2:
                createBullet("762.png", bulletSpeed, entities);
                break;
            case 3:
                createBullet("300blk.png", bulletSpeed, entities);
                break;
            case 4: // Shotgun has multiple bullets
                for(int i = 0; i < 28; i++) {
                    int randomSpeed = (int)((Math.random() * 1.1 * bulletSpeed) + 0.9 * bulletSpeed);
                    createBullet("buckshot.png", randomSpeed, entities);
                } // for
                break;
            case 5:
                createBullet("50bmg.png", bulletSpeed, entities);
                break;
            default:
                createBullet("9mm.png", bulletSpeed, entities);
        }
    }

    private void createBullet(String r, int speed, ArrayList<Entity> entities) {
        int randomSpread = (int)(Math.random() * 2 * bulletSpread + 1) - bulletSpread;
        Bullet bullet = new Bullet(r, 0, (int) y + bulletOffsets[1], following.getTeam(), bulletLife,
                (sprite.getDirection() ? -speed : speed), randomSpread, scene, bulletDamage);
        bullet.setX((int) x + sprite.getWidth()/2 +(sprite.getDirection() ? -bulletOffsets[0]-bullet.getWidth() : bulletOffsets[0]));
        entities.add(bullet);
    }

    @Override
    public void collidedWith(Entity o){} // collidedWith
}
