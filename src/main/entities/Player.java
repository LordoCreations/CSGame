package main.entities;

import main.Entity;
import main.GameScene;

public class Player extends Entity {
    protected int hp;
    protected int corpseID;
    protected int weapon;
    protected GameScene scene;

    public Player(GameScene s, String r, int newX, int newY, int weapon, int hp) {
        super(r, newX, newY);
        this.hp = hp;
        this.weapon = weapon;
        this.scene = s;

        // assigns corpse id based on skin
        if (r.equals("rambo.png")) {
            corpseID = 1;
        } // if
        else {
            corpseID = 0;
        } // else
    } // Character

    public int getHp() {
        return hp;
    } // getHp

    public void setWeapon(int w) {
        weapon = w;
    } // setWeapon

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
} // class
