package main.entities;

import main.Entity;
import main.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;

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

    // TODO replace with weapon ammo and stuff
    private int ammo;
    private int maxAmmo;

    private final int[] controls = new int[4];

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
        spawntime = System.currentTimeMillis();
        respawnTime = System.currentTimeMillis();
        setControls(id);
        isDead = false;

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
    } // getAmmo

    public double getMaxAmmo() { return maxAmmo; } // getMaxAmmo

    public int getWidth() {
        return sprite.getWidth();
    }

    public int getTeam() {
        return team;
    }

    public int getID() {
        return id;
    }

    public int getCorpseID() { return corpseID; }

    public void setSpawntime(long spawntime) { this.spawntime = spawntime; }

    public void setRespawnTime(long respawnTime) { this.respawnTime = respawnTime; }

    public long getRespawnTime() { return respawnTime; }

    public void setSkin(String r){
        skin = r; // TODO remove cuz its useless?
        this.setSprite(r);
    }

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

    public void setWeapon(int w) {
        weaponID = w;
        weapon = new Weapon(w, this, scene);

        // TODO get ammo from weapon
        this.maxAmmo = weapon.getMaxAmmo();
        this.ammo = maxAmmo;
        setDirection(sprite.getDirection());
    } // setWeapon

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        ammo = weapon.getAmmo();
        weapon.move();
        weapon.draw(g);
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
        if (isDead) {
            spawnProt = true; // players can't be hurt if dead
        } else if (System.currentTimeMillis() <= spawntime + 3000) {
            // TODO overlay shield sprite or other spawn protection effect
            // current implementation is flashing
            sprite.setOpacity((float) (0.5 - 0.3 * Math.sin((System.currentTimeMillis() - spawntime) / 150.0)));
            spawnProt = true;
        } else {
            sprite.setOpacity(1);
            spawnProt = false;
        } // if else

        moveX(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                x -= dx / 999;
                update();
            }
            dx = 0;
        }

        if (scene.keysDown.contains(controls[2])) {
            setDirection(false);
            dx = 300;
        } else if (scene.keysDown.contains(controls[0])) {
            setDirection(true);
            dx = -300;
        } else {
            dx = 0;
        }

        if (!isDead && !spawnProt && scene.keysDown.contains(controls[3])) {
            weapon.tryShoot(scene.entities);
            if (weapon.getAmmo() <= 0) {
                setWeapon(0);
            }
        }

        y += 1;
        update();
        if (scene.keysDown.contains(controls[1]) && scene.touchingWall(this)) dy = -900;

        y -= 1;
        update();
        dy += 1.75 * delta;

        moveY(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                y -= dy / 999;
                update();
            }
            dy = 0;
        }

    }

    @Override
    public void update() {
        hitbox.setRect(x + 4, y + 8, 48, 48);
    } // update
} // class
