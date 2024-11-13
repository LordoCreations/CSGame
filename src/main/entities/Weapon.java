package main.entities;

import main.AudioManager;
import main.Entity;
import main.GameScene;

import java.util.ArrayList;

/**
 * <h1>Weapon</h1>
 * <hr/>
 * Weapon held by players
 *
 * @author Anthony and Luke
 * @since 012-11-2024
 * @see Entity
 */

public class Weapon extends Entity {
    private final int[] offsets;
    private final Player following;
    protected int id;
    private final int bulletSpeed;
    private final int firingInterval;
    private long lastFired;
    private final int bulletLife;
    private final int bulletSpread;
    private final int bulletDamage;
    private final int[] bulletOffsets;
    private int ammo;
    private int recoil;
    private int weight;
    private int knockback;

    // weapon stats based on id
    // m9, mp5, ak47, honeybadger, defriender, barrettm82, rpg16, knife
    private final static int[] FIRING_INTERVALS = {300, 60, 150, 90, 600, 1200, 2000, 200};
    private final static int[] BULLET_SPEED = {1800, 1400, 1600, 1200, 2000, 4000, 1400, 10};
    private final static int[] BULLET_LIFE = {550, 500, 1000, 700, 80, 2000, 5000, 200};
    private final static int[] BULLET_SPREAD = {35, 150, 35, 80, 800, 0, 0, 0};
    private final static int[] BULLET_DAMAGE = {20, 15, 25, 18, 5, 100, 70, 40};
    private final static int[][] BULLET_OFFSETS = {{12, 5}, {40, 2}, {48, 6}, {48, 5}, {48, 7}, {68, 6}, {21, 0}, {13, -1}};
    private final static int[] MAX_AMMO = {1, 60, 30, 45, 8, 5, 3, 1};
    private final static int[] RECOIL = {320, 160, 500, 80, 1280, 1600, 1280, -400};
    private final static int[] KNOCKBACK = {700, 700, 1200, 500, 1000, 2000, 500, 200};
    private final static int[] WEIGHT = {0, 15, 35, 25, 30, 40, 55, 0};

    private final GameScene scene;

    /**
     * Constructor for a new Weapon
     * @param id id of the weapon
     * @param p player holding the weapon
     * @param scene scene weapon is created in
     */
    public Weapon(int id, Player p, GameScene scene) {
        super();
        super.setSprite(getWeaponURL(id));
        this.id = id;
        this.scene = scene;

        following = p;
        lastFired = System.currentTimeMillis() - 5000;
        offsets = getOffsets(id);
        firingInterval = FIRING_INTERVALS[id];
        bulletSpeed = BULLET_SPEED[id];
        bulletLife = BULLET_LIFE[id];
        bulletSpread = BULLET_SPREAD[id];
        bulletDamage = BULLET_DAMAGE[id];
        bulletOffsets = BULLET_OFFSETS[id];
        recoil = RECOIL[id];
        weight = WEIGHT[id];
        knockback = KNOCKBACK[id];
        ammo = getMaxAmmo();

    } // Weapon

    // follows the player
    public void move() {
        x = following.getX() + 28 + (sprite.getDirection() ? -offsets[0] - sprite.getWidth() : offsets[0]);
        y = following.getY() + offsets[1];
    } // move

    /* Getters and setters */
    public int getAmmo() {
        return ammo;
    } // getHp

    public int getMaxAmmo() {
        return MAX_AMMO[id];
    } // getMaxAmmo

    public int getFiringDistance() {
        return bulletLife * bulletSpeed / 1000;
    } // getFiringDistance

    private String getWeaponURL(int id) {
        return switch (id) {
            case 1 -> "weapons/mp5.png";
            case 2 -> "weapons/ak47.png";
            case 3 -> "weapons/honeybadger.png";
            case 4 -> "weapons/defriender.png";
            case 5 -> "weapons/barrettm82.png";
            case 6 -> "weapons/rpg16.png";
            case 7 -> "weapons/knife.png";
            default -> "weapons/m9.png";
        }; // switch
    } // getWeaponURL

    private int[] getOffsets(int id) {
        return switch (id) {
            case 1 -> new int[]{-28, 34};
            case 2 -> new int[]{-26, 35};
            case 3 -> new int[]{-23, 32};
            case 4 -> new int[]{-11, 28};
            case 5 -> new int[]{-17, 25};
            case 6 -> new int[]{-43, 26};
            case 7 -> new int[]{7, 21};
            default -> new int[]{8, 32};
        }; // switch
    } // getOffsets

    public void setDirection(boolean direction) {
        this.sprite.setDirection(direction);
    } // setDirection

    public int getWeight() {
        return weight;
    } // getWeight

    public int getLength() {
        return bulletOffsets[0];
    } // getLength

    private String getBulletName() {
        return switch (id) {
            case 2 -> "762";
            case 3 -> "300blk";
            case 4 -> "buckshot";
            case 5 -> "50bmg";
            case 6 -> "rocket";
            case 7 -> "swing";
            default -> "9mm";
        }; // switch
    } // getBulletName

    /**
     * Fires a bullet if possible
     * @param entities entities arraylist to add the shot to
     */
    public void tryShoot(ArrayList<Entity> entities) {
        if ((System.currentTimeMillis() - lastFired) < firingInterval) {
            return;
        } // if

        // otherwise add a shot
        lastFired = System.currentTimeMillis();

        if (id != 0 && id != 7) ammo -= 1; // m9 and knife have infinite ammo

        following.setRecoilDx(following.getDirection() ? recoil : -recoil); // Player takes recoil

        String bulletName = getBulletName();
        AudioManager.playSound(bulletName + ".wav", false); // TODO update sounds

        if (id == 4) {
            // Shotgun has multiple bullets
            for (int i = 0; i < 28; i++) {
                int randomSpeed = (int) ((Math.random() * 1.1 * bulletSpeed) + 0.9 * bulletSpeed);
                createBullet("buckshot", randomSpeed, entities);
            } // for
        } else if (id == 6) {
            createBullet(bulletName, bulletSpeed, entities, true); // RPG-16 rocket explodes
        } else {
            createBullet(bulletName, bulletSpeed, entities);
        } // if else
    }

    /**
     * Creates a new bullet based on weapon stats
     * @param r sprite image reference of bullet
     * @param speed bullet speed
     * @param entities arraylist of entities the bullet is added to
     * @param explosive whether the bullet explodes or not
     */
    private void createBullet(String r, int speed, ArrayList<Entity> entities, boolean explosive) {
        int randomSpread = (int) (Math.random() * 2 * bulletSpread + 1) - bulletSpread;
        Bullet bullet = new Bullet(r, 0, (int) y + bulletOffsets[1], following.getTeam(), bulletLife,
                (sprite.getDirection() ? -speed : speed), randomSpread, scene, bulletDamage, knockback);
        bullet.setX((int) x + sprite.getWidth() / 2 + (sprite.getDirection() ? -bulletOffsets[0] - bullet.getWidth() : bulletOffsets[0]));
        bullet.setExplosive(explosive);
        entities.add(bullet);
    } // createBullet

    /**
     * Overloaded for non-explosive bullets
     * @param r sprite image reference of bullet
     * @param speed bullet speed
     * @param entities arraylist of entities the bullet is added to
     */
    private void createBullet(String r, int speed, ArrayList<Entity> entities) {
        createBullet(r, speed, entities, false);
    } // createBullet

    /**
     * Collision detection - unused
     * @param o Object the bullet collided with
     */
    @Override
    public void collidedWith(Entity o) {} // collidedWith

} // class
