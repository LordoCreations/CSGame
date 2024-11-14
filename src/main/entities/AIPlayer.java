package main.entities;

import main.GameScene;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

/**
 * <h1>AI Player</h1>
 * <hr/>
 * Controls the Computer Controlled players
 *
 * @author Anthony and Luke
 * @see Player
 * @since 11-12-2024
 */

public class AIPlayer extends Player {
    private Player target;
    private final Player[] players;
    private double minDistance;
    private final double[] myCoord = new double[2];
    private final double[] theirCoord = new double[2];

    /**
     * Constructor for an AI controlled player
     *
     * @param s       scene the player is in
     * @param r       reference to the sprite
     * @param newX    x position
     * @param newY    y position
     * @param hp      health points
     * @param team    team the player is on
     * @param id      id of the player
     * @param players list of all players
     */
    public AIPlayer(GameScene s, String r, int newX, int newY, int hp, int team, int id, Player[] players) {
        super(s, r, newX, newY, hp, team, id);

        // Set player input to be controlled via computer instead of via keyboard input
        input = new HashSet<>();
        this.players = players;
    } // AIPlayer

    /**
     * Move the AI Player
     *
     * @param delta time (ms) since last call
     */
    @Override
    public void move(long delta) {
        findNearestTarget();

        double verticalDistance = theirCoord[1] - myCoord[1];
        boolean allowJump = false;

        // Avoid being stuck in corners
        if (Math.abs(verticalDistance) > 100 && Math.abs(myCoord[1] - 525) < 75) {
            allowJump = true;
            theirCoord[0] = Math.round(theirCoord[0] / WIDTH) * WIDTH;
        } else if (Math.abs(verticalDistance) > 300 && onGround()) {
            theirCoord[0] = WIDTH / 2d;
            theirCoord[1] = HEIGHT;
        } else {
            allowJump = !onGround() || Math.abs(verticalDistance) > 50;
        }

        if (target != null) { // TODO remove after debugging
            // System.out.printf("Tracking %d, Distance %.2f, Delta X: %.2f Delta Y: %.2f%n", target.getID(), Math.min(minDistance, 9999), theirCoord[0] - myCoord[0], theirCoord[1] - myCoord[1]);
        } // if

        // Jump if target is above player, or randomly
        if (allowJump && (Math.random() < 0.005 * delta || verticalDistance < -100 && Math.random() < 0.003 * delta)) {
            input.add(KeyEvent.VK_UP);
        } else {
            input.remove(KeyEvent.VK_UP);
        } // if else

        // How close to move to the player
        double horizontalThreshold = Math.min(Math.abs(verticalDistance) < 50 ? 400 + 70 * Math.random() : 50, weapon.getFiringDistance() / 3.0);
        double horizontalDistance = theirCoord[0] - myCoord[0];

        // Left/Right input
        if (Math.abs(horizontalDistance) < horizontalThreshold && Math.abs(verticalDistance) < 50) {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
        } else if (horizontalDistance > horizontalThreshold) {
            moveInDirection(true);
        } else if (horizontalDistance < -horizontalThreshold) {
            moveInDirection(false);
        }

        // don't move if no targets
        if (minDistance >= Double.MAX_VALUE) {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
            input.remove(KeyEvent.VK_UP);
        } // if else

        // Shoot if in range
        if (Math.abs(verticalDistance) < sprite.getHeight() && Math.random() < 0.05 * delta && horizontalDistance < weapon.getFiringDistance()) {
            if (Math.random() < 0.005 * delta) moveInDirection(horizontalDistance > 0);
            input.add(KeyEvent.VK_DOWN);
        } else {
            input.remove(KeyEvent.VK_DOWN);
        } // if else

        // Move player
        super.move(delta);
    } // move

    /**
     * Override controls
     *
     * @param id id of player
     */
    @Override
    protected void setControls(int id) {
        controls[0] = KeyEvent.VK_LEFT;
        controls[1] = KeyEvent.VK_UP;
        controls[2] = KeyEvent.VK_RIGHT;
        controls[3] = KeyEvent.VK_DOWN;
    } // setControls

    /**
     * Find nearest target and set their Coord to the center of their sprite
     */
    private void findNearestTarget() {
        double currentDistance;

        minDistance = Double.MAX_VALUE;
        getCenter(this, myCoord);
        for (Player p : players) {
            if (p != null && p.team != this.team && !p.spawnProt && !p.isDead) {
                currentDistance = distance(p);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    target = p;
                } // if
            } // if
        } // for

        // if no targets don't try to call on null value
        if (target != null) {
            getCenter(target, theirCoord);
        } // if
    } // findNearestTarget

    /**
     * Find weighted distance to a coordinate (Down < Up, Left/Right < Up/Down in distance)
     *
     * @param other Target Player
     * @return Weighted distance to other
     */
    private double distance(Player other) {
        getCenter(other, theirCoord);
        return Math.sqrt(Math.pow(myCoord[0] - theirCoord[0], 2) + Math.abs(Math.pow(myCoord[1] - theirCoord[1], 3)));
    } // distance

    /**
     * Get the center of the player
     *
     * @param player      player center
     * @param destination integer array to write to
     */
    private void getCenter(Player player, double[] destination) {
        destination[0] = player.getX() + player.getWidth() / 2.0;
        destination[1] = player.getY() + player.getHeight() / 2.0;
    }

    /**
     * moves player in the specified direction
     *
     * @param direction direction of the player
     */
    private void moveInDirection(boolean direction) {
        if (direction) {
            input.remove(KeyEvent.VK_LEFT);
            input.add(KeyEvent.VK_RIGHT);
        } else {
            input.remove(KeyEvent.VK_RIGHT);
            input.add(KeyEvent.VK_LEFT);
        } // if else
    } // getCenter
} // class
