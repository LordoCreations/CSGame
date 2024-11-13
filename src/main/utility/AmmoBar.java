package main.utility;

import main.Entity;
import main.entities.Player;
import java.awt.*;

/**
 * <h1>Ammo Bar</h1>
 * <hr/>
 * Bar showing how much ammo a player has left
 *
 * @author Anthony and Luke
 * @since 013-11-2024
 * @see Bar
 */

public class AmmoBar extends Bar {

    /**
     * Constructor for a new Ammo Bar
     * @param follow Player the ammo bar is attached to
     */
    public AmmoBar(Player follow) {
        super(follow);
    } // AmmoBar

    /**
     * Draws the ammo bar filled to the proper amount
     * @param g display graphics
     */
    public void draw(Graphics g) {
        g.setColor(new Color(113, 76, 22));
        g.fillRect(x, y, width, 4);
        g.setColor(new Color(228, 184, 99));
        g.fillRect(x, y, (int) (follow.getAmmo() / follow.getMaxAmmo() * width), 4);
        g.setColor(new Color(0, 0, 0, 100));
        g.drawRect(x, y, width, 4);
    } // draw

    /**
     * Follows the player
     * @param delta milliseconds since last update
     */
    @Override public void move(long delta) {
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 20;
    } // move
} // class
