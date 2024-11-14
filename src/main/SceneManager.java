package main;

import main.utility.AudioManager;

import java.awt.event.*;

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
    private final Game game;

    /**
     * Constructor
     *
     * @param game Game object that everything is drawn from
     */
    SceneManager(Game game, Scene s) {
        this.game = game;
        setScene(s);

        game.addKeyListener(new KeyInputHandler());
        game.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentScene.handleMouseEvent(e);
            }
        });
        game.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                currentScene.handleMouseEvent(e);
            }
        });
    } // SceneManager

    /**
     * Set the Game Scene
     *
     * @param scene scene to set to
     */
    public void setScene(Scene scene) {
        if (currentScene != null && scene instanceof GameScene || currentScene instanceof GameScene) {
            AudioManager.stopAllSounds();
            System.out.println(AudioManager.music.size());
        } else if (AudioManager.music.isEmpty()) AudioManager.playSound("relentlessrage.wav", true);

        currentScene = scene;
        currentScene.init();
    } // setScene

    /**
     * Update the current scene (aka tick), called continuously by Game.java
     */
    public void update() {
        if (currentScene != null) currentScene.update();
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