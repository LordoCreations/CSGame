package main.utility;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * <h1>Mask</h1>
 * <hr/>
 * Lazy-loads pixel alpha channel data into an array and
 * allows "pixel-perfect" collision between non-transparent pixels
 *
 * @author Anthony and Luke
 * @since 14-11-2024
 */

public class Mask {
    private final BufferedImage image;
    private final byte[][] pixels;

    /**
     * Creates a mask
     * @param image image to base mask off of
     */
    public Mask(BufferedImage image) {
        this.image = image;

        pixels = new byte[image.getWidth()][image.getHeight()];

        // unloaded pixels have a value of -1
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) pixels[x][y] = -1;
        } // for
    } // Mask

    /**
     * Checks if a rectangle overlaps with mask
     * @param hitbox rectangle
     * @return if collision is detected
     */
    public boolean overlaps(Rectangle hitbox) {

        // Iterate over hitbox pixels
        for (int x = hitbox.x; x < hitbox.x + hitbox.width; x++) {
            for (int y = hitbox.y; y < hitbox.y + hitbox.height; y++) {

                // check if coordinates are within image bounds
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {

                    // if pixel is not loaded get alpha channel value
                    if (pixels[x][y] == -1) {
                        pixels[x][y] = (byte) ((image.getRGB(x, y) >> 24) & 0xff);
                    }

                    // detect if pixel is solid
                    if (pixels[x][y] != 0) return true;
                } // if
            } // for
        } // for
        return false;
    } // overlaps
} // Mask