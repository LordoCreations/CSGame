package main;

import java.awt.*;

import static main.Game.HEIGHT;

/* main.Entity.java
 * An entity is any object that appears in the game.
 * It is responsible for resolving collisions and movement.
 * Derived from Space Invaders
 */


public abstract class Entity {

    // Java Note: the visibility modifier "protected"
    // allows the variable to be seen by this class,
    // any classes in the same package, and any subclasses
    // "private" - this class only
    // "public" - any class can see it

    private final Rectangle me = new Rectangle(); // bounding rectangle of this entity
    private final Rectangle him = new Rectangle(); // bounding rect. of other entities
    protected double x;   // current x location
    protected double y;   // current y location
    protected Sprite sprite; // this entity's sprite
    protected double dx; // horizontal speed (px/s)  + -> right
    protected double dy; // vertical speed (px/s) + -> down
    protected Rectangle hitbox = new Rectangle(); // hitbox of entity

    /* Constructor
     * input: reference to the image for this entity,
     *        initial x and y location to be drawn at
     */
    public Entity(String r, int newX, int newY) {
        x = newX;
        y = newY;
        sprite = (SpriteStore.get()).getSprite(r);
        hitbox.setRect(newX, newY, sprite.getWidth(), sprite.getHeight());
    } // constructor

    public Entity() {
        x = 0;
        y = 0;
    } // constructor

    /**
     * Sets the sprite image
     *
     * @param r Path to new sprite image
     */
    public void setSprite(String r) {
        this.sprite = (SpriteStore.get()).getSprite(r);
        hitbox.setRect(x, y, sprite.getWidth(), sprite.getHeight());
    } // setSprite

    /* move
     * input: delta - the amount of time passed in ms
     * output: none
     * purpose: after a certain amount of time has passed,
     *          update the location
     */
    public void move(long delta) {
        // update location of entity based ov move speeds
        moveX(delta);
        moveY(delta);
    } // move

    /**
     * X component of moving
     *
     * @param delta milliseconds since last tick
     */
    public void moveX(double delta) {
        x += (delta * dx) / 1000;
        update();
    } // moveX

    /**
     * Y component of moving
     *
     * @param delta milliseconds since last tick
     */
    public void moveY(double delta) {
        y += (delta * dy) / 1000;
        update();
    } // moveY

    /**
     * Update hitbox of sprite
     * By default is the image bounding rectangle, can be overridden (e.g. in Player.java)
     */
    public void update() {
        hitbox.setRect(x, y, sprite.getWidth(), sprite.getHeight());
    } // update

    // send entity to the top if it falls through the map
    protected void fallThrough() {
        if (y > HEIGHT) {
            y = 0;
            x = Math.max(Math.min(x, 650), 950 - sprite.getWidth());
        } // if
    } // fallThrough

    // get position and size
    public int getX() {
        return (int) x;
    } // getX

    public int getY() {
        return (int) y;
    } // getY

    public int getWidth() {
        return sprite.getWidth();
    } // getWidth

    public int getHeight() {
        return sprite.getHeight();
    } // getHeight

    /*
     * Draw this entity to the graphics object provided at (x,y)
     */
    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }  // draw


    /* collidesWith
     * input: the other entity to check collision against
     * output: true if entities collide
     * purpose: check if this entity collides with the other.
     */
    public boolean collidesWith(Entity other) {
        me.setBounds((int) x, (int) y, (int) hitbox.getWidth(), (int) hitbox.getHeight());
        him.setBounds(other.getX(), other.getY(), other.sprite.getWidth(), other.sprite.getHeight());
        return me.intersects(him);
    } // collidesWith

    /* collidedWith
     * input: the entity with which this has collided
     * purpose: notification that this entity collided with another
     * Note: abstract methods must be implemented by any class
     *       that extends this class
     */
    public abstract void collidedWith(Entity other);
} // main.Entity class