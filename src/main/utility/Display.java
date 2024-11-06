package main.utility;

import main.Entity;

public class Display extends Entity {
    private String r;
    private int id;
    private int choice;

    public Display(int x, int y, int id){
        super(getSkinURL(id), x, y);
        this.r = getSkinURL(id);
        this.id = id;
    } // Display


    public void update(int i) {
        this.r = getSkinURL(i);
        this.setSprite(r);
    }

    public static String getSkinURL(int i) {
        return switch (i) {
            case 1 -> "skins/locked.png";
            case 2 -> "skins/globey.png";
            case 3 -> "skins/soldier.png";
            case 4 -> "skins/rambo.png";
            default -> "skins/default.png";
        };
    }


    public String getR() {
        return r;
    }

    public int getId() {
        return id;
    }

    @Override
    public void collidedWith(Entity other) {}
}
