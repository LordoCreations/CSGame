package main.utility;

import main.Entity;

public class Display extends Entity {
    private String r;

    public Display(String r, int x, int y){
        super(r, x, y);
        this.r = r;
    } // Display


    public void update(int i) {
        this.r = getSkinURL(i);
        this.setSprite(r);
    }

    public static String getSkinURL(int i) {
        switch (i) {
            case 1:
                return "locked.png";
            case 2:
                return "globey.png";
            case 3:
                return "soldier.png";
            case 4:
                return "rambo.png";
            default:
                return "default.png";
        }
    }

    @Override
    public void collidedWith(Entity other) {}
}
