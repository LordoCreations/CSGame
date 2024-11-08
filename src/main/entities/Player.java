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
    private double kbDx;

    // TODO replace with weapon ammo and stuff
    private int ammo;
    private int maxAmmo;

    protected int[] controls = new int[4];

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

    public double getMaxAmmo() {
        return maxAmmo;
    } // getMaxAmmo

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

    public int getCorpseID() {
        return corpseID;
    }

    public void setSpawntime(long spawntime) {
        this.spawntime = spawntime;
    }

    public void setRespawnTime(long respawnTime) {
        this.respawnTime = respawnTime;
    }

    public long getRespawnTime() {
        return respawnTime;
    }

    public void setRecoilDx(double dx) {
        recoilDx = dx;
    }

    public void setKbDx(double dx) {
        kbDx = dx;
    }

    public void setCoord(int[] coord) {
        this.x = coord[0];
        this.y = coord[1];
    }

    public void setDirection(boolean dir) {
        this.sprite.setDirection(dir);
        this.weapon.setDirection(dir);
    }

    public boolean getDirection() {
        return sprite.getDirection();
    }

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
            dx = speed + recoilDx + kbDx;
        } else if (input.contains(controls[0])) {
            setDirection(true);
            dx = -speed + recoilDx + kbDx;
        } else {
            dx = 0 + recoilDx + kbDx;
        } // if else

        recoilDx = adjustSpeed(recoilDx, delta);
        kbDx = adjustSpeed(kbDx, delta);

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
        } else if (input.contains(controls[1])) {
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

        if (y > HEIGHT) {
            y = 0;
            x = Math.max(Math.min(x, 650), 950 - sprite.getWidth());
        }
    }

    private double adjustSpeed(double s, long delta) {
        s *= Math.pow(0.95, (delta/3.0));
        return s > 0 ? Math.max(0, s - 1) : Math.min(0, s + 1);
    } // adjustSpeed

    @Override
    public void update() {
        hitbox.setRect(x + 4, y + 8, 48, 48);
    } // update
} // class
