package main.entities;

import main.Entity;
import main.GameScene;

public class Player extends Entity {
    protected double hp;
    private double maxHp;
    protected int corpseID;
    protected int weaponID;
    protected GameScene scene;

    // TODO replace with weapon ammo and stuff
    private int ammo;
    private int maxAmmo;

    public Player(GameScene s, String r, int newX, int newY, int weapon, int hp) {
        super(r, newX, newY);
        this.hp = hp;
        this.maxHp = hp + 20;
        this.weaponID = weapon;
        this.scene = s;

        // TODO replace with weapon ammo and stuff
        this.maxAmmo = 90;
        this.ammo = 15;

        // assigns corpse id based on skin
        if (r.equals("rambo.png")) {
            corpseID = 1;
        } // if
        else {
            corpseID = 0;
        } // else
    } // Character

    public double getHp() {
        return hp;
    } // getHp

    public double getMaxHp() {
        return maxHp;
    } // getHp

    public double getAmmo() {
        return ammo;
    } // getHp

    public double getMaxAmmo() {
        return maxAmmo;
    } // getHp

    public void setWeapon(int w) {
        weaponID = w;
    } // setWeapon

    public int getWidth() {
        return sprite.getWidth();
    }

    @Override
    public void collidedWith(Entity o) {

    } // collidedWith

    @Override
    public void move(long delta) {
        moveX(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                x -= dx/999;
                update();
            }
            dx = 0;
        }

        if (scene.keysDown.contains('d')) {
            this.sprite.setDirection(false);
            dx = 300;
        } else if (scene.keysDown.contains('a')) {
            this.sprite.setDirection(true);
            dx = -300;
        } else {
            dx = 0;
        }

        y += 1;
        update();
        if (scene.keysDown.contains('w') && scene.touchingWall(this)) {
            dy = -900;
        }
        y -= 1;
        update();
        dy += 30;

        moveY(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                y -= dy/999;
                update();
            }
            dy = 0;
        }

    }

    @Override
    public void update() {
        hitbox.setRect(x+4, y+8, 48, 48);
    }
} // class
