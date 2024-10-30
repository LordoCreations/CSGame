package main.entities;
import main.Entity;

import java.util.ArrayList;

public class Weapon extends Entity {
    private int[] offsets;
    private Player c;
    protected int id;
    private int speed = 100;
    private int firingInterval = 100;
    private long lastFired = -5000;

    private final static int[] FIRING_INTERVALS = {200, 80, 100, 90, 400, 1000};

    public Weapon(int id, Player c){
        super();

        super.setSprite(getWeaponURL(id));
        this.c = c;
        this.id = id;

        offsets = getOffsets(id);
    } // Weapon

    // follows the player
    public void move() {
        x = c.getX() + offsets[0] + (sprite.getDirection() ? -38 : 28);
        y = c.getY() + offsets[1];
    } // move

    private String getWeaponURL(int id) {
        switch (id) {
            case 1:
                return "smg.png";
            default:
                return "m9.png";
        }
    }

    // get x and y offsets based on the weapon
    private int[] getOffsets(int id){
        switch(id){
            case 0:
                return new int[]{8, 32};
            case 1:
                return new int[]{0, 0};
        }
        return new int[]{0, 0};
    } // getOffsets

    public void setDirection(boolean direction) {
        this.sprite.setDirection(direction);
    }

    public void tryShoot(ArrayList<Entity> entities) {
        if ((System.currentTimeMillis() - lastFired) < firingInterval) {
            return;
        } // if

        // otherwise add a shot
        lastFired = System.currentTimeMillis();
        
        // TODO get actual weapon data, fix direction
        Bullet bullet = new Bullet("test.png", (int) x, (int) y, (sprite.getDirection() ? -speed : speed), 100, 100);
        entities.add(bullet);
    }

    @Override
    public void collidedWith(Entity o){} // collidedWith
}
