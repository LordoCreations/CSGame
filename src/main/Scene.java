package main;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * <h1>Scene</h1>
 * <hr/>
 * A scene in Game (i.e. menu, customization)
 *
 * @author Anthony and Luke
 * @see SceneManager
 * @since 13-11-2024
 */

public abstract class Scene {
    protected Game game;

    /**
     * Constructor for Scene
     * @param game Game
     */
    Scene(Game game) {
        this.game = game;
    } // Scene

    /*
    The following four functions are called to handle keyboard and mouse input,
    they are overridden by each scene depending on what is needed
     */
    protected void handleMouseEvent(MouseEvent e) {} // handleMouseEvent
    protected void handleKeyPressed(KeyEvent e) {} // handleKeyPressed
    protected void handleKeyReleased(KeyEvent e) {} // handleKeyReleased
    protected void handleKeyTyped(KeyEvent e) {} // handleKeyTyped

    /*
    The following two functions are called by SceneManager upon initialization and during each tick
     */
    public abstract void init();
    public abstract void update();
} // Scene