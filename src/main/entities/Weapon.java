package main.entities;
import main.Entity;

public class Weapon extends Entity {
    private int firingInterval;
    private int capacity;
    private int bulletspeed;
    private int xOffset;
    private int yOffset;
    private Character c;

    public Weapon(String r, int newX, int newY, int firingInterval, int capacity, int bulletspeed, Character c){
        super(r, newX, newY);
        this.firingInterval = firingInterval;
        this.capacity = capacity;
        this.bulletspeed = bulletspeed;
        this.c = c;

        int[] offsets = getOffsets(r);
        xOffset = offsets[0];
        yOffset = offsets[1];
    } // Weapon


    // follows the player
    public void move() {
        x = c.getX() + xOffset;
        y = c.getY() + yOffset;
    } // move

    // get x and y offsets based on the weapon
    private int[] getOffsets(String s){
        switch(s){
            case "weapon0":
                return new int[]{0, 0};
            case "weapon1":
                return new int[]{0, 0};
        }
        return new int[]{0, 0};
    } // getOffsets

    @Override
    public void collidedWith(Entity o){

    } // collidedWith
}
