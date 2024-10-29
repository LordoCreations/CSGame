package main.entities;
import main.Entity;
import main.Scene;

import java.util.ArrayList;

public class Character extends Entity {
    protected int hp;
    protected int corpseID;
    protected int weapon;
    protected Scene scene;

    public Character(Scene s, String r, int newX, int newY, int weapon, int hp) {
        super(r,newX, newY);
        this.hp = hp;
        this.weapon = weapon;
        this.scene = s;

        // assigns corpse id based on skin
        if(r.equals("rambo.png")){
            corpseID = 1;
        } // if
        else{
            corpseID = 0;
        } // else
    } // Character

    public int getHp(){
        return hp;
    } // getHp

    public void setWeapon(int w){
        weapon = w;
    } // setWeapon

    @Override
    public void collidedWith(Entity o){

    } // collidedWith

    public boolean touchingWalls(ArrayList<Entity> walls) {

        return false;
    }
} // class
