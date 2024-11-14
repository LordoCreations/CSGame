package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * <h1>Game</h1>
 * <hr/>
 * Main Game class, derived from Space Invaders
 *
 * @author Anthony and Luke
 * @see SceneManager
 * @see <a href="https://mdinfotech.net/index.php?course=compsci12&unit=3#id37">Space Invaders Exercise</a>
 * @since 13-11-2024
 */

public class Game extends Canvas {
    BufferStrategy strategy;
    public static int WIDTH = 1600;
    public static int HEIGHT = 900;
    public int[] skins = new int[4];
    public boolean[] types = new boolean[4];
    public int[] teams = new int[4];
    public int playerCount;
    public final static int weaponCount = 8;

    private final SceneManager sceneManager;

    /*
     * Construct our game and set it running.
     */
    public Game() {
        JFrame container = new JFrame("Cube Force");

        JPanel panel = (JPanel) container.getContentPane();

        // set up the resolution of the game
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        // set up canvas size (this) and add to frame
        setBounds(0, 0, WIDTH, HEIGHT);
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
        sceneManager = new SceneManager(this, new MenuScene(this));

        // start the game
        gameLoop();
    } // constructor

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
        while (true) {
            sceneManager.update();
        } // while
    } // gameLoop

    /**
     * Sets the scene of the scene manager
     *
     * @param scene Scene game is set to
     */
    public void setScene(Scene scene) {
        sceneManager.setScene(scene);
    } // setScene


    /**
     * Main Program
     */
    public static void main(String[] args) {

        // instantiate this object
        new Game();
    } // main
} // main.Game
