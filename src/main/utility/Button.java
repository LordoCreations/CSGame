package main.utility;

import main.Entity;

import java.awt.*;

/**
 * <h1>Button</h1>
 * <hr/>
 * Button used to take user input
 *
 * @author Anthony and Luke
 * @see Entity
 * @since 13-11-2024
 */

public class Button extends Entity {
    private final Runnable action;
    private final boolean highlight;
    private boolean hovered;
    private final Color[] hoverGradient = {new Color(255, 255, 255, 80), new Color(255, 255, 255, 0)};
    private final float[] distribution = {0f, 1f};

    /**
     * Constructor for a new Button
     *
     * @param r      sprite image reference
     * @param x      x position
     * @param y      y position
     * @param action action called when button pressed
     */
    public Button(String r, int x, int y, Runnable action) {
        super(r, x, y);
        this.x = x;
        this.y = y;
        this.action = action;

        if (r.equals("buttons/start.png")) {
            Color temp = hoverGradient[0];
            hoverGradient[0] = hoverGradient[1];
            hoverGradient[1] = temp;
        } // if

        highlight = !"buttons/left.png buttons/right.png".contains(r);
    } // Button

    /**
     * Draws the button image and adds a glow if the button is hovered
     *
     * @param g Graphics object
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if (highlight && hovered) {
            ((Graphics2D) g).setPaint(new LinearGradientPaint((float) x, (float) y, (float) (x + sprite.getWidth()), (float) y, distribution, hoverGradient));
            g.fillRect((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        } // if
    } // draw

    /**
     * Run action if button pressed
     *
     * @param mouseX       mouse x position
     * @param mouseY       mouse y position
     * @param mousePressed whether the mouse is pressed
     */
    public void update(int mouseX, int mouseY, boolean mousePressed) {
        hovered = mouseX >= x && mouseX <= x + sprite.getWidth() && mouseY >= y && mouseY <= y + sprite.getHeight();
        if (hovered && mousePressed) action.run();
    } // update

    /**
     * Collision detection - unused
     *
     * @param other object the button collided with
     */
    public void collidedWith(Entity other) {
    } // collidedWith

} // Button