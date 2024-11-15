package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/* main.SpriteStore.java
 * Manages the main.sprites in the game.
 * Caches them for future use.
 * Derived from Space Invaders
 */

public class SpriteStore {

    // one instance of this class will exist
    // this instance will be accessed by main.Game.java
    private static final SpriteStore single = new SpriteStore();
    private final HashMap<String, Image> images = new HashMap<>();  // key,value pairs that stores
    // the sprites

    // returns the single instance of this class
    public static SpriteStore get() {
        return single;
    } // get

    /* getSprite
     * input: a string specifying which sprite image is required
     * output: a sprite instance containing an accelerated image
     *         of the requested image
     * purpose: to return a specific sprite
     */
    public Sprite getSprite(String ref) {
        ref = "main/sprites/" + ref;

        // if the sprite is already in the HashMap
        // then return it
        // Note:
        if (images.get(ref) != null) {
            return new Sprite(images.get(ref));
        } // if

        // else, load the image into the HashMap off the
        // hard drive (and hence, into memory)

        BufferedImage sourceImage = null;

        try {
            // get the image location
            URL url = this.getClass().getClassLoader().getResource(ref);
            if (url == null) {
                System.out.println("Failed to load: " + ref);
                System.exit(0); // exit program if file not found
            }
            sourceImage = ImageIO.read(url); // get image
        } catch (IOException e) {
            System.out.println("Failed to load: " + ref);
            System.exit(0); // exit program if file not loaded
        } // catch

        // create an accelerated image (correct size) to store our sprite in
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.TRANSLUCENT);

        // draw our source image into the accelerated image
        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        // create a sprite, add it to the cache and return it
        Sprite sprite = new Sprite(image);
        images.put(ref, image);

        return sprite;
    } // getSprite
} // main.SpriteStore