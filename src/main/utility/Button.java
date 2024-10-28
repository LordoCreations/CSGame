package main.utility;
import main.Entity;

import java.awt.Graphics;


public class Button extends Entity {
    private int x;
    private int y;
    private String text;
    private boolean hovered = false;
    private Runnable action;

    public Button(String r, int x, int y, Runnable action) {
        super(r, x, y);
        this.x = x;
        this.y = y;
        this.action = action;
    }

    public void render(Graphics g) {
        super.draw(g);
    }

    public void update(int mouseX, int mouseY, boolean mousePressed) {
        hovered = mouseX >= x && mouseX <= x + sprite.getWidth() && mouseY >= y && mouseY <= y + sprite.getHeight();
        if (hovered && mousePressed) {
            action.run();
        }
    }

    public void collidedWith(Entity other) {
        // collisions with aliens are handled in ShotEntity and ShipEntity
    } // collidedWith

}