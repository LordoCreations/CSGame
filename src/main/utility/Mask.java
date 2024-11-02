package main.utility;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mask {
    private final BufferedImage image;

    public Mask(BufferedImage image) {
        this.image = image;
    }

    public boolean overlaps(Rectangle spriteRect) {

        // Iterate over the sprite's rectangle
        for (int x = spriteRect.x; x < spriteRect.x + spriteRect.width; x++) {
            for (int y = spriteRect.y; y < spriteRect.y + spriteRect.height; y++) {

                // Check if the coordinates are within the image bounds
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {

                    // Get the pixel's color
                    int pixel = image.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff; // Extract alpha channel
                    if (alpha != 0) { // Solid pixel detected
                        return true; // Collision detected
                    }
                }
            }
        }
        return false; // No collision
    }
}