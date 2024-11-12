package main;

import java.awt.*;

/**
 * <h1>Sprite</h1>
 * <hr/>
 * Stores the image of a character which has direction and opacity. The remaining code is from Sprite.java
 * in the Space Invaders Template Code
 *
 * @author Anthony and Luke
 * @see Image
 * @see <a href="https://mdinfotech.net/index.php?course=compsci12&unit=3#id37">Space Invaders Exercise</a>
 * @since 12-11-2024
 */

public class Sprite {
    private boolean flip;
    public Image image;  // the image to be drawn for this sprite
    private float opacity = 1f;

    // constructor
    public Sprite(Image i) {
        image = i;
    } // constructor

    // return width of image in pixels
    public int getWidth() {
        return image.getWidth(null);
    } // getWidth

    // return height of image in pixels
    public int getHeight() {
        return image.getHeight(null);
    } // getHeight

    // draw the sprite in the graphics object provided at location (x,y)
    public void draw(Graphics g, int x, int y) {
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if (flip) {
            g.drawImage(image, x + image.getWidth(null), y, -image.getWidth(null), image.getHeight(null), null);
        } else {
            g.drawImage(image, x, y, null);
        } // if else
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    } // draw

    /* Getters and Setters*/

    public void setDirection(boolean flip) {
        this.flip = flip;
    } // setDirection

    public boolean getDirection() {
        return flip;
    } // getDirection

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    } // setOpacity
} // main.Sprite