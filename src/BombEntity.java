/* AlienShotEntity.java
 * October 21, 2024
 * Represents enemy's shot
 */
public class BombEntity extends ShotEntity {
    private int toDie = -1;
    /* construct the shot
     * input: game - the game in which the shot is being created
     *        ref - a string with the name of the image associated to
     *              the sprite for the shot
     *        x, y - initial location of shot
     */
    public BombEntity(Game g, String r, int newX, int newY) {
        super(g, r, newX, newY);  // calls the constructor in ShotEntity
        moveSpeed *= 2;
        dy = moveSpeed;
    } // constructor

    /* move
     * input: delta - time elapsed since last move (ms)
     * purpose: move shot
     */
    public void move(long delta) {
        super.move(delta);  // calls the move method in Entity

        // if shot moves off top of screen, remove it from entity list
        if (y < -100) {
            game.removeEntity(this);
        } // if

        if (--toDie == 0) game.removeEntity(this);

    } // move


    /* collidedWith
     * input: other - the entity with which the shot has collided
     * purpose: notification that the shot has collided
     *          with something
     */
    public void collidedWith(Entity other) {
        // if it has hit an alien, kill it!
        if (other instanceof AlienEntity) {
            super.sprite = (SpriteStore.get()).getSprite("sprites/explosion.png");
            // remove affect entities from the Entity list
            game.removeEntity(other);
            toDie = 5;
            dy /= 2;
            // notify the game that the alien is dead
            game.notifyAlienKilled();
        } // if

    } // collidedWith

} // ShipEntity class