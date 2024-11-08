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
 * @author Anthony
 * @since 07-09-2024
 * @see Player
 */

public class AIPlayer extends Player {
    private Player target;
    private final Player[] players;
    private double minDistance;
    private final double[] myCoord = new double[2];
    private final double[] theirCoord = new double[2];
    private boolean allowJump = true;

    public AIPlayer(GameScene s, String r, int newX, int newY, int hp, int team, int id, Player[] players) {
        super(s, r, newX, newY, hp, team, id);

        // Set player input to be controlled via computer instead of via keyboard input
        input = new HashSet<>();
        this.players = players;
    } // AIPlayer

    /**
     * Move the AI Player
     * @param delta time (ms) since last call
     */
    @Override
    public void move(long delta) {
        findNearestTarget();

        if (target != null) { // TODO remove after debugging
            //System.out.printf("Tracking %d, Distance %.2f, Delta X: %.2f Delta Y: %.2f%n", target.getID(), Math.min(minDistance, 9999), theirCoord[0] - myCoord[0], theirCoord[1] - myCoord[1]);
        } // if


        double verticalDistance = theirCoord[1] - myCoord[1];
        allowJump = true;

        // Avoid being stuck in top/bottom corner
        if (theirCoord[1] < 400 && myCoord[1] > 600 || theirCoord[1] > 600 && myCoord[1] < 400) {
            allowJump = false;
            theirCoord[0] = WIDTH/2d;
            theirCoord[1] = HEIGHT;
        } // if

        // Jump if target is above player, or randomly
        if (allowJump && (Math.random() < 0.005 * delta || verticalDistance > 50 && Math.random() < 0.05)) {
            input.add(KeyEvent.VK_UP);
        } else {
            input.remove(KeyEvent.VK_UP);
        } // if else

        // How close to move to the player
        double horizontalThreshold = Math.min(input.contains(KeyEvent.VK_UP) ||
                Math.abs(theirCoord[1] - myCoord[1]) > 100 ? 30 : 300, weapon.getFiringDistance() / 2.0);

        double horizontalDistance = theirCoord[0] - myCoord[0];

        // Left/Right input
        if (Math.random() < 0.0001 * delta || horizontalDistance > horizontalThreshold) {
            input.remove(KeyEvent.VK_LEFT);
            input.add(KeyEvent.VK_RIGHT);
        } else if (Math.random() < 0.0001 * delta || horizontalDistance < -horizontalThreshold) {
            input.remove(KeyEvent.VK_RIGHT);
            input.add(KeyEvent.VK_LEFT);
        } else {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
        } // if else

        // don't move if no targets
        if (minDistance >= Double.MAX_VALUE) {
            input.remove(KeyEvent.VK_RIGHT);
            input.remove(KeyEvent.VK_LEFT);
            input.remove(KeyEvent.VK_UP);
        } // if-else

        // Shoot if in range
        // TODO reduce probability that bot shoots with sniper and shotgun
        if (Math.abs(verticalDistance) < sprite.getHeight() && Math.random() < 0.05 * delta && horizontalDistance < weapon.getFiringDistance()) {
            input.add(KeyEvent.VK_DOWN);
        } else {
            input.remove(KeyEvent.VK_DOWN);
        }

        // Move player
        super.move(delta);
    } // move

    /**
     * Override controls
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
     * Find nearest target and set theirCoord to the center of their sprite
     */
    private void findNearestTarget() {
        double currentDistance;

        minDistance = Double.MAX_VALUE;
        getCenter(this, myCoord);
        for (Player p : players) {
            if (p == null) continue;
            if (p.team != this.team && !p.spawnProt && !p.isDead) {
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
     * @param other Target Player
     * @return Weighted distance to other
     */
    private double distance(Player other) {
        getCenter(other, theirCoord);
        return Math.abs(Math.sqrt(Math.abs(Math.pow(myCoord[0] - theirCoord[0], 2) + Math.pow(myCoord[1] - theirCoord[1], 3))));
    } // distance

    /**
     * Get the center of the player
     * @param player player center
     * @param destination integer array to write to
     */
    private void getCenter(Player player, double[] destination) {
        destination[0] = player.getX() + player.getWidth() / 2.0;
        destination[1] = player.getY() + player.getHeight() / 2.0;
    }
}
