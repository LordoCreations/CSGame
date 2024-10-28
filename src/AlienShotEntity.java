/* AlienShotEntity.java
 * October 21, 2024
 * Represents enemy's shot
 */
public class AlienShotEntity extends ShotEntity {
    private boolean used = false; // true if shot hits something

    /* construct the shot
     * input: game - the game in which the shot is being created
     *        ref - a string with the name of the image associated to
     *              the sprite for the shot
     *        x, y - initial location of shot
     */
    public AlienShotEntity(Game g, String r, int newX, int newY) {
        super(g, r, newX, newY);  // calls the constructor in ShotEntity
        moveSpeed = 250;
        dy = moveSpeed;
    } // constructor
    

    /* collidedWith
     * input: other - the entity with which the shot has collided
     * purpose: notification that the shot has collided
     *          with something
     */
    public void collidedWith(Entity other) {
        // prevents double kills
        if (used) {
            return;
        } // if

        if (other instanceof ShieldEntity) {
            game.removeEntity(this);
        }

        // if it has hit the ship, kill it!
        if (other instanceof ShipEntity) {
            // remove affect entities from the Entity list
            game.removeEntity(this);
            game.notifyDeath();

            used = true;
        } // if

    } // collidedWith

} // ShipEntity class