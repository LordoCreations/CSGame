package main.entities;

import main.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;


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

    // TODO replace with weapon ammo and stuff
    private int ammo;
    private int maxAmmo;

    protected final int[] controls = new int[4];

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
        input = scene.keysDown;
        isDead = false;

        // assigns corpse id based on skin
        if (r.equals("rambo.png")) {
            corpseID = 4;
        } else {
            corpseID = 0;
        } // if else
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

    public int getHeight() {
        return sprite.getHeight();
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

    public void setRecoilDx (double dx) { recoilDx = dx; }

    public void setCoord(int[] coord) {
        this.x = coord[0];
        this.y = coord[1];
    }

    public void setDirection(boolean dir) {
        this.sprite.setDirection(dir);
        this.weapon.setDirection(dir);
    }

    public boolean getDirection() { return sprite.getDirection(); }

    protected void setControls(int id) {
        switch (id) {
            case 1:
                controls[0] = KeyEvent.VK_A;
                controls[1] = KeyEvent.VK_W;
                controls[2] = KeyEvent.VK_D;
                controls[3] = KeyEvent.VK_S;
                break;
            default:
                controls[0] = KeyEvent.VK_LEFT;
                controls[1] = KeyEvent.VK_UP;
                controls[2] = KeyEvent.VK_RIGHT;
                controls[3] = KeyEvent.VK_DOWN;
        }
    }

    public void setWeapon(int w) {
        weaponID = w;
        weapon = new Weapon(w, this, scene);

        speed = 300 - weapon.getWeight();
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
                scene.playerDied(this, ((Bullet) o).getTeam(), ((Bullet) o).getDx());
            } // if
        } // if
    } // collidedWith

    @Override
    public void move(long delta) {
        if (isDead) {
            spawnProt = true; // players can't be hurt if dead
            return;
        } else if (System.currentTimeMillis() <= spawntime + 3000) {

            // current implementation is flashing
            sprite.setOpacity((float) (0.5 - 0.3 * Math.sin((System.currentTimeMillis() - spawntime) / 150.0)));
            spawnProt = true;

        } else {
            sprite.setOpacity(1);
            spawnProt = false;
        } // if else
        hp = Math.min(maxHp, hp + 0.005 * delta);

        if (input.contains(controls[2])) {
            setDirection(false);
            dx = speed + recoilDx;
        } else if (input.contains(controls[0])) {
            setDirection(true);
            dx = -speed + recoilDx;
        } else {
            dx = 0 + recoilDx;
        } // if else

        recoilDx *= 0.9;
        if(recoilDx > 0) {
            recoilDx = Math.max(0, recoilDx - 1);
        } // if

        moveX(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                x -= dx / 999;
                update();
            }
            dx = 0;
        }

        if (!spawnProt && input.contains(controls[3])) {
            weapon.tryShoot(scene.entities);
            if (weapon.getAmmo() <= 0) {
                setWeapon(0);
            }
        }

        y += 1;
        update();
        if (!scene.touchingWall(this)) {
            dy += 1.75 * delta;
        } else if (input.contains(controls[1]) ) {
            dy = -900;
        }
        y -= 1;

        moveY(delta);
        if (scene.touchingWall(this)) {
            while (scene.touchingWall(this)) {
                y -= dy / 999;
                update();
            }
            dy = 0;
        }

        if (y > HEIGHT ) {
            y = -10;
            x = Math.min(Math.max(650, x), 950-sprite.getWidth());
        }
    }

    @Override
    public void update() {
        hitbox.setRect(x + 4, y + 8, 48, 48);
    } // update
} // class
