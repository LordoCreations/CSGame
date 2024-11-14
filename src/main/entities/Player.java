package main.entities;

import main.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

/**
 * <h1>Player</h1>
 * <hr/>
 * The player
 *
 * @author Anthony and Luke
 * @since 012-11-2024
 * @see Entity
 */

public class Player extends Entity {
    public double hp;
    private final double maxHp;
    private int corpseID;
    protected int weaponID;
    protected Weapon weapon;
    protected GameScene scene;
    protected int team;
    private final int id;
    private long respawnTime;
    private long spawntime;
    public boolean spawnProt;
    private String skin;
    public boolean isDead;
    private int speed;
    private double recoilDx;
    protected Set<Integer> input;
    private double kbDx;

    // TODO replace with weapon ammo and stuff
    private int ammo;
    private int maxAmmo;

    protected int[] controls = new int[4];

    /**
     * Constructor for a Player
     * @param s scene the player is created in
     * @param r reference to sprite image
     * @param newX x position
     * @param newY y position
     * @param hp health points
     * @param team team of the player
     * @param id id of the player
     */
    public Player(GameScene s, String r, int newX, int newY, int hp, int team, int id) {
        super(r, newX, newY);
        skin = r;
        this.hp = hp;
        this.maxHp = hp;
        this.weaponID = 0;
        this.scene = s;
        this.weapon = new Weapon(weaponID, this, scene);
        this.team = team;
        this.id = id;
        spawntime = GameTime.getTime();
        respawnTime = GameTime.getTime();
        setControls(id);
        input = scene.keysDown;
        isDead = false;

        // assigns corpse id based on skin
        if (r.equals("rambo.png")) {
            corpseID = 4;
        } else {
            corpseID = 0;
        } // if else
    } // Character

    /* Getters and setters */
    public double getHp() {
        return hp;
    } // getHp

    public double getMaxHp() {
        return maxHp;
    } // getHp

    public double getAmmo() {
        return ammo;
    } // getAmmo

    public double getMaxAmmo() {
        return maxAmmo;
    } // getMaxAmmo

    public int getWidth() {
        return sprite.getWidth();
    } // getWidth

    public int getHeight() {
        return sprite.getHeight();
    } // getHeight

    public int getTeam() {
        return team;
    } // getTeam

    public int getID() {
        return id;
    } // getID

    public int getCorpseID() {
        return corpseID;
    } // getCorpseID

    public void setSpawntime(long spawntime) {
        this.spawntime = spawntime;
    } // setSpawntime

    public void setRespawnTime(long respawnTime) {
        this.respawnTime = respawnTime;
    } // setRespawnTime

    public long getRespawnTime() {
        return respawnTime;
    } // getRespawnTime

    public void setRecoilDx(double dx) {
        recoilDx = dx;
    } // setRecoilDx

    public void setKbDx(double dx) {
        kbDx = dx;
    } // setKbDx

    public void setCoord(int[] coord) {
        this.x = coord[0];
        this.y = coord[1];
    } // setCoord

    public void setDirection(boolean dir) {
        this.sprite.setDirection(dir);
        this.weapon.setDirection(dir);
    } // setDirection

    public boolean getDirection() {
        return sprite.getDirection();
    } // getDirection

    protected void setControls(int id) {
        switch (id) {
            case 1:
                controls = new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN};
                break;
            case 2:
                controls = new int[]{KeyEvent.VK_J, KeyEvent.VK_I, KeyEvent.VK_L, KeyEvent.VK_K};
                break;
            case 3:
                controls = new int[]{KeyEvent.VK_F, KeyEvent.VK_T, KeyEvent.VK_H, KeyEvent.VK_G};
                break;
            default:
                controls = new int[]{KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S};

        } // switch
    } // setControls

    public void setWeapon(int w) {
        weaponID = w;
        weapon = new Weapon(w, this, scene);

        speed = 300 - weapon.getWeight();
        this.maxAmmo = weapon.getMaxAmmo();
        this.ammo = maxAmmo;
        setDirection(sprite.getDirection());
    } // setWeapon

    /**
     * Draws the player
     * @param g display graphics
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        ammo = weapon.getAmmo();
        weapon.move();
        weapon.draw(g);
    }

    /**
     * 
     * @param o
     */
    @Override
    public void collidedWith(Entity o) {
        if (o instanceof Bullet) {
            if (hp <= 0) {
                scene.playerDied(this, ((Bullet) o).getTeam());
            } // if
        } // if
    } // collidedWith

    @Override
    public void move(long delta) {
        if (isDead) {
            spawnProt = true; // players can't be hurt if dead
            return;
        } else if (GameTime.getTime() <= spawntime + 1000) {
            // flash when under spawn prot
            sprite.setOpacity((float) (0.5 - 0.3 * Math.sin((System.currentTimeMillis() - spawntime) / 150.0)));
            spawnProt = true;
        } else {
            sprite.setOpacity(1);
            spawnProt = false;
        } // if else
        hp = Math.min(maxHp, hp + 0.005 * delta);

        // move player based on keyboard input
        if (input.contains(controls[2])) {
            setDirection(false);
            dx = speed + recoilDx + kbDx;
        } else if (input.contains(controls[0])) {
            setDirection(true);
            dx = -speed + recoilDx + kbDx;
        } else {
            dx = 0 + recoilDx + kbDx;
        } // if else

        // adjust position from recoil and knockback
        recoilDx = adjustSpeed(recoilDx, delta);
        kbDx = adjustSpeed(kbDx, delta);

        // players can't go into walls
        moveX(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                x -= dx / 999;
                update();
            } // while
            dx = 0;
        } // if

        // fire the weapon
        if (!spawnProt && input.contains(controls[3])) {
            weapon.tryShoot(scene.entities);
            if (weapon.getAmmo() <= 0) {
                setWeapon(0);
            } // if
        } // if

        // realistic gravity
        if (!onGround()) {
            dy += 1.75 * delta;
        } else if (input.contains(controls[1])) {
            dy = -900;
        } // if else

        // players can't fall through floors
        moveY(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                y -= dy / 999;
                update();
            } // if
            dy = 0;
        } // if else

        fallThrough();
    } // move

    /**
     *
     * @param s
     * @param delta
     * @return the reduced speed
     */
    private double adjustSpeed(double s, long delta) {
        s *= Math.pow(0.95, (delta/3.0));
        return s > 0 ? Math.max(0, s - 1) : Math.min(0, s + 1);
    } // adjustSpeed

    /**
     * checks if the player is on the ground
     * @return if player is on the ground
     */
    protected boolean onGround() {
        boolean condition;
        y += 1;
        update();
        condition = scene.touchingWall(this);
        y -= 1;
        update();
        return condition;
    } // inAir

    /**
     * updates the position of the hitbox
     */
    @Override
    public void update() {
        hitbox.setRect(x + 4, y + 8, 48, 48);
    } // update
} // class
