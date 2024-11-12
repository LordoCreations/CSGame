package main.entities;

import main.AudioManager;
import main.Entity;
import main.GameScene;

import java.util.ArrayList;

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

    public Weapon(int id, Player p, GameScene scene) {
        super();
        super.setSprite(getWeaponURL(id));
        this.id = id;
        this.scene = scene;

        following = p;
        lastFired = System.currentTimeMillis();
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

    public int getAmmo() {
        return ammo;
    } // getHp

    public int getMaxAmmo() {
        return MAX_AMMO[id];
    } // getMaxAmmo

    public int getFiringDistance() {
        return bulletLife * bulletSpeed / 1000;
    }

    // follows the player
    public void move() {
        x = following.getX() + 28 + (sprite.getDirection() ? -offsets[0] - sprite.getWidth() : offsets[0]);
        y = following.getY() + offsets[1];
    } // move

    private String getWeaponURL(int id) {
        switch (id) {
            case 1:
                return "weapons/mp5.png";
            case 2:
                return "weapons/ak47.png";
            case 3:
                return "weapons/honeybadger.png";
            case 4:
                return "weapons/defriender.png";
            case 5:
                return "weapons/barrettm82.png";
            case 6:
                return "weapons/rpg16.png";
            case 7:
                return "weapons/knife.png";
            default:
                return "weapons/m9.png";
        }
    }

    // get x and y offsets based on the weapon
    private int[] getOffsets(int id) {
        switch (id) {
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
            case 6:
                return new int[]{-43, 26};
            case 7:
                return new int[]{7, 21};
        }
        return new int[]{0, 0};
    } // getOffsets

    public void setDirection(boolean direction) {
        this.sprite.setDirection(direction);
    }

    public int getWeight() {
        return weight;
    }

    public void tryShoot(ArrayList<Entity> entities) {
        if ((System.currentTimeMillis() - lastFired) < firingInterval) {
            return;
        } // if

        if (id != 0 && id != 7) ammo -= 1;

        following.setRecoilDx(following.getDirection() ? recoil : -recoil);

        // otherwise add a shot
        lastFired = System.currentTimeMillis();

        String bulletName = getBulletName();
        AudioManager.playSound(bulletName + ".wav", false); // TODO update sounds

        if (id == 4) {

            // Shotgun has multiple bullets
            for (int i = 0; i < 28; i++) {
                int randomSpeed = (int) ((Math.random() * 1.1 * bulletSpeed) + 0.9 * bulletSpeed);
                createBullet("buckshot", randomSpeed, entities);
            } // for
        } else if (id == 6) {
            createBullet(bulletName, bulletSpeed, entities, true);
        } else {
            createBullet(bulletName, bulletSpeed, entities);
        } // if else
    }

    private String getBulletName() {
        return switch (id) {
            case 2 -> "762";
            case 3 -> "300blk";
            case 4 -> "buckshot";
            case 5 -> "50bmg";
            case 6 -> "rocket";
            case 7 -> "swing";
            default -> "9mm";
        };
    }

    private void createBullet(String r, int speed, ArrayList<Entity> entities, boolean explosive) {
        int randomSpread = (int) (Math.random() * 2 * bulletSpread + 1) - bulletSpread;
        Bullet bullet = new Bullet(r, 0, (int) y + bulletOffsets[1], following.getTeam(), bulletLife,
                (sprite.getDirection() ? -speed : speed), randomSpread, scene, bulletDamage, knockback);
        bullet.setX((int) x + sprite.getWidth() / 2 + (sprite.getDirection() ? -bulletOffsets[0] - bullet.getWidth() : bulletOffsets[0]));
        bullet.setExplosive(explosive);
        entities.add(bullet);
    } // createBullet

    private void createBullet(String r, int speed, ArrayList<Entity> entities) {
        createBullet(r, speed, entities, false);
    } // createBullet

    @Override
    public void collidedWith(Entity o) {} // collidedWith

    public int getLength() {
        return bulletOffsets[0];
    }
}
