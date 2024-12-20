package main;

import main.utility.AudioManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <h1>Scene Manager</h1>
 * <hr/>
 * Finite State Machine that manages the various scenes in the game (ie. menu, game)
 *
 * @author Anthony and Luke
 * @see Game
 * @since 12-11-2024
 */

public class SceneManager {
    private Scene currentScene;

    /**
     * Constructor
     *
     * @param game Game object that everything is drawn from
     */
    SceneManager(Game game, Scene s) {
        setScene(s);

        // add Keyboard and Mouse Listeners
        game.addKeyListener(new KeyInputHandler());
        game.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentScene.handleMouseEvent(e);
            } // mousePressed
        });
        game.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                currentScene.handleMouseEvent(e);
            } // mouseMoved
        });
    } // SceneManager

    /**
     * Set the Game Scene
     *
     * @param scene scene to set to
     */
    public void setScene(Scene scene) {
        if (scene instanceof GameScene || currentScene instanceof EndScene || currentScene instanceof GameScene && scene instanceof MenuScene) {
            AudioManager.stopAllSounds();
        } // if

        if (!(scene instanceof GameScene) && AudioManager.music.isEmpty()) {
            AudioManager.playSound("relentlessrage.wav", true);
        } // if

        currentScene = scene;
        currentScene.init();
    } // setScene

    /**
     * Update the current scene (aka tick), called continuously by Game.java
     */
    public synchronized void update() {
        if (currentScene != null) currentScene.update();
        AudioManager.clearRemovedSounds();
    } // update

    /* inner class KeyInputHandler
     * handles keyboard input from the user
     */
    protected class KeyInputHandler extends KeyAdapter {

        /* The following methods are required
         * for any class that extends the abstract
         * class KeyAdapter.  They handle keyPressed,
         * keyReleased and keyTyped events.
         */
        @Override
        public void keyPressed(KeyEvent e) {
            currentScene.handleKeyPressed(e);
        } // keyPressed

        @Override
        public void keyReleased(KeyEvent e) {
            currentScene.handleKeyReleased(e);
        } // keyReleased

        @Override
        public void keyTyped(KeyEvent e) {
            currentScene.handleKeyTyped(e);
        } // keyTyped
    } // class KeyInputHandler
} // SceneManager