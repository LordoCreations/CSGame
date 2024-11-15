package main.entities;

import main.Entity;
import main.GameScene;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
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
    private final Player[] players;
    private final ArrayList<Chest> chests;
    private final double[] myCoord = new double[2];
    private final double[] theirCoord = new double[2];
    private Entity target;
    private double minDistance;

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
    public AIPlayer(GameScene s, String r, int newX, int newY, int hp, int team, int id, Player[] players, ArrayList<Chest> chests) {
        super(s, r, newX, newY, hp, team, id);

        // Set player input to be controlled via computer instead of via keyboard input
        input = new HashSet<>();
        this.players = players;
        this.chests = chests;
    } // AIPlayer

    /**
     * Move the AI Player
     *
     * @param delta time (ms) since last call
     */
    @Override
    public void move(long delta) {
        if (Math.random() < 0.005 * delta) findNearestTarget();
        if (target != null) getCenter(target, theirCoord);

        double verticalDistance = theirCoord[1] - myCoord[1];
        boolean allowJump = false;

        // Avoid being stuck in corners
        if (Math.abs(verticalDistance) > 100 && Math.abs(myCoord[1] - 525) <= 75) {
            allowJump = true;
            theirCoord[0] = Math.round(theirCoord[0] / WIDTH) * WIDTH;
            theirCoord[1] = HEIGHT;
            target = null;
        } else if (Math.abs(verticalDistance) > 300 -
                (target != null && target instanceof Player && ((Player) target).onGround() ? 0 : 250) && onGround()) {
            theirCoord[0] = WIDTH / 2d;
            theirCoord[1] = HEIGHT;
            target = null;
        } else {
            allowJump = !onGround() || Math.abs(verticalDistance) > 30 || Math.random() < 0.5 * delta;
        } // if else

        // Jump if target is above player, or randomly
        if (allowJump && (Math.random() < 0.005 * delta || verticalDistance < -100 && Math.random() < 0.003 * delta)) {
            input.add(KeyEvent.VK_UP);
        } else {
            input.remove(KeyEvent.VK_UP);
        } // if else

        // How close to move to the player
        double horizontalThreshold = target instanceof Player ?
                Math.min(Math.abs(verticalDistance) < 70 ? 400 * Math.random() : 50, weapon.getFiringDistance() / 3.0) : 0;
        double horizontalDistance = theirCoord[0] - myCoord[0];

        // Left/Right input
        if (target instanceof Player &&
                Math.abs(horizontalDistance) < horizontalThreshold && Math.abs(verticalDistance) < 30) {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
        } else if (horizontalDistance > horizontalThreshold) {
            moveInDirection(true);
        } else if (horizontalDistance < -horizontalThreshold) {
            moveInDirection(false);
        } // if else

        // don't move if no targets
        if (minDistance >= Double.MAX_VALUE) {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
            input.remove(KeyEvent.VK_UP);
        } // if else

        // Shoot if in range
        if (target instanceof Player && Math.abs(verticalDistance) < sprite.getHeight() &&
                Math.random() < 0.05 * delta && horizontalDistance < weapon.getFiringDistance()) {
            if (Math.random() < 0.05 * delta) moveInDirection(horizontalDistance > 0);
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
     * Find nearest target and set their coordinates to the center of their sprite
     */
    private void findNearestTarget() {
        minDistance = Double.MAX_VALUE;
        getCenter(this, myCoord);
        for (Player p : players) {
            if (p != null && p.team != this.team && !p.spawnProt && !p.isDead) {
                evalTarget(p, 1);
            } // if
        } // for

        for (Chest c : chests) {
            evalTarget(c, 1.2);
        } // for
    } // findNearestTarget

    /**
     * Evaluate if a target is better than current target
     *
     * @param target Target Entity
     * @param weight Target importance weighting
     */
    private void evalTarget(Entity target, double weight) {
        double currentDistance = distance(target) * weight;
        if (currentDistance < minDistance) {
            minDistance = currentDistance;
            this.target = target;
        } // if
    } // evalTarget

    /**
     * Find weighted distance to a coordinate (Down < Up, Left/Right < Up/Down in distance)
     *
     * @param other Target Player
     * @return Weighted distance to other
     */
    private double distance(Entity other) {
        getCenter(other, theirCoord);
        return Math.sqrt(Math.pow(myCoord[0] - theirCoord[0], 2) + Math.abs(Math.pow(myCoord[1] - theirCoord[1], 3)));
    } // distance

    /**
     * Get the center of the player
     *
     * @param other       player center
     * @param destination integer array to write to
     */
    private void getCenter(Entity other, double[] destination) {
        destination[0] = other.getX() + other.getWidth() / 2.0;
        destination[1] = other.getY() + other.getHeight() / 2.0;
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
