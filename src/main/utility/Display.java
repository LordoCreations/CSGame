package main.utility;

import main.*;

import java.awt.*;

/**
 * <h1>Display</h1>
 * <hr/>
 * Displays skin previews to the screen
 *
 * @author Anthony and Luke
 * @see Entity
 * @since 013-11-2024
 */

public class Display extends Entity {
    private final int id;
    private final Game game;
    private final Sprite unused;
    private String r;
    private boolean inUse;

    /**
     * Constructor for a new Display
     *
     * @param game the game the image is displayed in
     * @param x    x position
     * @param y    y position
     * @param id   id of the display
     */
    public Display(Game game, int x, int y, int id) {
        super(getSkinURL(id), x, y);
        unused = SpriteStore.get().getSprite("displays/notInUse.png");
        this.game = game;
        this.r = getSkinURL(id);
        this.id = id;
        this.inUse = true;
    } // Display

    /**
     * Get skin URL given skin ID
     *
     * @param i skin ID
     * @return skin URL
     */
    public static String getSkinURL(int i) {
        return switch (i) {
            case 1 -> "skins/locked.png";
            case 2 -> "skins/globey.png";
            case 3 -> "skins/soldier.png";
            case 4 -> "skins/rambo.png";
            default -> "skins/default.png";
        }; // switch
    } // getSkinURL

    /**
     * update the display image
     *
     * @param i id of new skin
     */
    public void update(int i) {
        this.r = getSkinURL(i);
        this.setSprite(r);
    } // update

    /**
     * Draws the display
     *
     * @param g display graphics
     */
    @Override
    public void draw(Graphics g) {
        Color[] colors = {Colors.getTeamColors(game.teams[id])[0], new Color(11, 12, 15, 0)};

        ((Graphics2D) g).setPaint(new LinearGradientPaint(0, 0, 0, 600, new float[]{0.0f, 1.0f}, colors));

        g.fillRect((int) (x - 15), (int) (y - 40), 310, 600);
        g.drawImage(this.sprite.image, (int) x + 59, (int) y + 59, sprite.getWidth() * 3, sprite.getHeight() * 3, null);
        if (!inUse) unused.draw(g, (int) x + 31, (int) y + 31);
    } // draw

    /* Getters and setters */
    public void setIfUsing(boolean inUse) {
        this.inUse = inUse;
    } // setIfUsing

    public String getR() {
        return r;
    } // getR

    public int getId() {
        return id;
    } // getID

    /**
     * Collision detection - unused
     *
     * @param other object the display collided with
     */
    @Override
    public void collidedWith(Entity other) {
    } // collidedWith
}
