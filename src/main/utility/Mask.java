package main.utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mask {
    private final BufferedImage image;
    private final byte pixels[][];

    public Mask(BufferedImage image) {
        this.image = image;

        pixels = new byte[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixels[x][y] = -1;
            }
        }

    }

    public boolean overlaps(Rectangle hitbox) {

        // Iterate over the sprite's hitbox
        for (int x = hitbox.x; x < hitbox.x + hitbox.width; x++) {
            for (int y = hitbox.y; y < hitbox.y + hitbox.height; y++) {

                // Check if the coordinates are within the image bounds
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {

                    if (pixels[x][y] == -1) {
                        pixels[x][y] = (byte) ((image.getRGB(x, y) >> 24) & 0xff); // Extract alpha channel
                    }


                    // Get the pixel's color
                    if (pixels[x][y] != 0) { // Solid pixel detected
                        return true; // Collision detected
                    }
                }
            }
        }
        return false; // No collision
    }
}