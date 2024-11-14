package main;

import main.entities.Chest;
import main.entities.Player;

/**
 * <h1>GameScene</h1>
 * <hr/>
 * Manages Game time to allow complete pausing
 *
 * @author Anthony and Luke
 * @since 13-11-2024
 */

public class GameTime {
    private static long time;

    /**
     * Increments the time by delta
     * @param delta time in milliseconds to add
     */
    public static void update(long delta) {
        time += delta;
    } // update

    /* Getters and Setters */

    public static void setTime(long newTime) {
        time = newTime;
    } // setTime

    public static long getTime() {
        return time;
    } // getTime
} // GameTime
