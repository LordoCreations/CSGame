package main;/* main.Sprite.java
 * March 23, 2006
 * Store no state information, this allows the image to be stored only
 * once, but to be used in many different places.  For example, one
 * copy of alien.gif can be used over and over.
 */

import java.awt.*;

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
        }
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    } // draw

    public void setDirection(boolean flip) {
        this.flip = flip;
    }
    public boolean getDirection() {
        return flip;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public float getOpacity() {
        return opacity;
    }
} // main.Sprite