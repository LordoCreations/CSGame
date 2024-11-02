package main.entities;

import main.Entity;
import main.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {
    public double hp;
    private double maxHp;
    protected int corpseID;
    protected int weaponID;
    protected Weapon weapon;
    protected GameScene scene;
    protected int team;
    private int id;
    private long spawntime;
    public boolean spawnProt;

    // TODO replace with weapon ammo and stuff
    private int ammo;
    private int maxAmmo;

    private int[] controls = new int[4];

    public Player(GameScene s, String r, int newX, int newY, int hp, int team, int id) {
        super(r, newX, newY);
        this.hp = hp;
        this.maxHp = hp;
        this.weaponID = 0;
        this.scene = s;
        this.weapon = new Weapon(0, this, scene);
        this.team = team;
        this.id = id;
        spawntime = System.currentTimeMillis();
        setControls(id);

        // TODO get ammo from weapon
        this.maxAmmo = 90;
        this.ammo = this.maxAmmo;

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
        weapon = new Weapon(w, this, scene);

        // TODO get ammo from weapon
    } // setWeapon

    @Override public void draw(Graphics g) {
        super.draw(g);
        weapon.move();
        weapon.draw(g);
    }

    public int getWidth() {
        return sprite.getWidth();
    }

    public int getTeam() {
        return team;
    }

    public int getID() {
        return id;
    }

    public void setSpawntime(long spawntime) { this.spawntime = spawntime; }

    public void setCoord(int[] coord) {
        this.x = coord[0];
        this.y = coord[1];
    }

    public void setDirection(boolean dir) {
        this.sprite.setDirection(dir);
        this.weapon.setDirection(dir);
    }

    private void setControls(int id) {
        switch (id) {
            case 1:
                controls[0] = KeyEvent.VK_A;
                controls[1] = KeyEvent.VK_W;
                controls[2] = KeyEvent.VK_D;
                controls[3] = KeyEvent.VK_S;
                break;
            default:
                controls[0] = 37;
                controls[1] = 38;
                controls[2] = 39;
                controls[3] = 40;
        }
    }

    @Override
    public void collidedWith(Entity o) {
        if (o instanceof Bullet) {
            if (hp <= 0) {
                scene.playerDied(this, ((Bullet) o).getTeam());
            }
        }
    } // collidedWith

    @Override
    public void move(long delta) {
        if (System.currentTimeMillis() <= spawntime + 3000){
            // TODO overlay shield sprite or other spawn protection effect
            spawnProt = true;
        } else {
            spawnProt = false;
        } // if else

        moveX(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                x -= dx/999;
                update();
            }
            dx = 0;
        }

        if (scene.keysDown.contains(controls[2])) {
            this.sprite.setDirection(false);
            this.weapon.setDirection(false);
            dx = 300;
        } else if (scene.keysDown.contains(controls[0])) {
            this.sprite.setDirection(true);
            this.weapon.setDirection(true);
            dx = -300;
        } else {
            dx = 0;
        }

        if (scene.keysDown.contains(controls[3])) {
            spawntime = -3000;
            this.weapon.tryShoot(scene.entities);
        }

        y += 1;
        update();
        if (scene.keysDown.contains(controls[1]) && scene.touchingWall(this)) {
            dy = -900;
        }
        y -= 1;
        update();
        dy += 1.75*delta;

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
    } // update
} // class
