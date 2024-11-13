package main.utility;

import main.Colors;
import main.Entity;
import main.entities.Player;

import java.awt.*;

/**
 * <h1>Bar</h1>
 * <hr/>
 * Player's health bar
 *
 * @author Anthony and Luke
 * @since 013-11-2024
 * @see Entity
 */

public class Bar extends Entity {
    protected int x;
    protected int y;
    protected final Player follow;
    protected final int width = 40;
    private final Color[] barColor;

    /**
     * Constructor for a new Bar
     * @param follow Player the bar is attached to
     */
    public Bar(Player follow) {
        super("test.png", 0, 0);
        this.follow = follow;
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 10;

        barColor = Colors.getTeamColors(follow.getTeam());
    } // Bar

    /**
     * Draws the bar filled to the proper amount
     * @param g display graphics
     */
    public void draw(Graphics g) {
        g.setColor(barColor[1]);
        g.fillRect(x, y, width, 4);
        g.setColor(barColor[0]);
        g.fillRect(x, y, (int) (follow.getHp() / follow.getMaxHp() * width), 4);
        g.setColor(new Color(0, 0, 0, 100));
        g.drawRect(x, y, width, 4);
    } // draw

    /**
     * Collision detection - unused
     * @param other Object the bar collided with
     */
    @Override
    public void collidedWith(Entity other) {}

    /**
     * Follows the player
     * @param delta milliseconds since last update
     */
    @Override public void move(long delta) {
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 10;
    } // move
} // class
