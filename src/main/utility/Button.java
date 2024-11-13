package main.utility;
import main.Entity;

import java.awt.Graphics;

/**
 * <h1>Button</h1>
 * <hr/>
 * Button used to take user input
 *
 * @author Anthony and Luke
 * @since 013-11-2024
 * @see Entity
 */

public class Button extends Entity {
    private final int x;
    private final int y;
    private final Runnable action;

    /**
     * Constructor for a new Button
     * @param r sprite image reference
     * @param x x position
     * @param y y position
     * @param action action called when button presse
     */
    public Button(String r, int x, int y, Runnable action) {
        super(r, x, y);
        this.x = x;
        this.y = y;
        this.action = action;
    } // Button

    /**
     * Run action if button pressed
     * @param mouseX mouse x position
     * @param mouseY mouse y position
     * @param mousePressed whether the mouse is pressed
     */
    public void update(int mouseX, int mouseY, boolean mousePressed) {
        boolean hovered = mouseX >= x && mouseX <= x + sprite.getWidth() && mouseY >= y && mouseY <= y + sprite.getHeight();
        if (hovered && mousePressed) {
            action.run();
        } // if
    } // update

    /**
     * Collision detection - unused
     * @param other object the button collided with
     */
    public void collidedWith(Entity other) {} // collidedWith

} // class