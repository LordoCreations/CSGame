package main;/* main.Game.java
 * Space Invaders Main Program
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Game extends Canvas {
    BufferStrategy strategy;
    private final boolean gameRunning = true;
    private final ArrayList removeEntities = new ArrayList();
    private final String message = "";
    private final boolean logicRequiredThisLoop = false;
    static int width =  1600;
    static int height = 900;
    public int[] skins = new int[4];

    private final SceneManager sceneManager;

    /*
     * Construct our game and set it running.
     */
    public Game() {
        JFrame container = new JFrame("Cube Force");

        JPanel panel = (JPanel) container.getContentPane();

        // set up the resolution of the game
        panel.setPreferredSize(new Dimension(width, height));
        panel.setLayout(null);

        // set up canvas size (this) and add to frame
        setBounds(0, 0, width, height);
        panel.add(this);

        // Tell AWT not to bother repainting canvas since that will
        // be done using graphics acceleration
        setIgnoreRepaint(true);

        // make the window visible
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // if user closes window, shutdown game and jre
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            } // windowClosing
        });


        // request focus so key events are handled by this canvas
        requestFocus();

        // create buffer strategy to take advantage of accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialize Scenes
        sceneManager = new SceneManager(this);
        sceneManager.setScene(new MenuScene(this));

        // start the game
        gameLoop();
    } // constructor

    public void setScene(Scene scene) {
        sceneManager.setScene(scene);
    }

    /*
     * gameLoop
     * input: none
     * output: none
     * purpose: Main game loop. Runs throughout game play.
     *          Responsible for the following activities:
     *           - calculates speed of the game loop to update moves
     *           - moves the game entities
     *           - draws the screen contents (entities, text)
     *           - updates game events
     *           - checks input
     */
    public void gameLoop() {

        // keep loop running until game ends
        while (gameRunning) {
            sceneManager.update();

            // pause
            try {
                Thread.sleep(0);
            } catch (Exception e) {}
        } // while
    } // gameLoop




    /**
     * Main Program
     */
    public static void main(String[] args) {
        // instantiate this object
        new Game();
    } // main
} // main.Game
