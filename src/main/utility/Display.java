package main.utility;

import main.Entity;

public class Display extends Entity {
    String r;

    public Display(String r, int x, int y){
        super(r, x, y);
        this.r = r;
    } // Display

    @Override
    public void move(long delta){
        this.setSprite(r);
    } // move

    public void setRef(String r){
        this.r = r;
    }

    @Override
    public void collidedWith(Entity other) {

    }
}
