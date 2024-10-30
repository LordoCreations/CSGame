package main.entities;

import main.Entity;
import main.GameScene;

import java.awt.*;

public class Player extends Entity {
    protected double hp;
    private double maxHp;
    protected int corpseID;
    protected int weaponID;
    protected Weapon weapon;
    protected GameScene scene;

    // TODO replace with weapon ammo and stuff
    private int ammo;
    private int maxAmmo;

    public Player(GameScene s, String r, int newX, int newY, int hp, int team) {
        super(r, newX, newY);
        this.hp = hp;
        this.maxHp = hp + 20;
        this.weaponID = 0;
        this.weapon = new Weapon(0, this);
        this.scene = s;

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
        weapon = new Weapon(w, this);
    } // setWeapon

    @Override public void draw(Graphics g) {
        super.draw(g);
        weapon.move();
        weapon.draw(g);
    }

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
            this.weapon.setDirection(false);
            dx = 300;
        } else if (scene.keysDown.contains('a')) {
            this.sprite.setDirection(true);
            this.weapon.setDirection(true);
            dx = -300;
        } else {
            dx = 0;
        }

        if (scene.keysDown.contains('s')) {
            this.weapon.tryShoot(scene.entities);
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
