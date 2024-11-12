package main.utility;
import main.Entity;

import java.awt.Graphics;


public class Button extends Entity {
    private final int x;
    private final int y;
    private final Runnable action;

    public Button(String r, int x, int y, Runnable action) {
        super(r, x, y);
        this.x = x;
        this.y = y;
        this.action = action;
    }

    public void update(int mouseX, int mouseY, boolean mousePressed) {
        boolean hovered = mouseX >= x && mouseX <= x + sprite.getWidth() && mouseY >= y && mouseY <= y + sprite.getHeight();
        if (hovered && mousePressed) {
            action.run();
        }
    }

    public void collidedWith(Entity other) {
        // collisions with aliens are handled in ShotEntity and ShipEntity
    } // collidedWith

}