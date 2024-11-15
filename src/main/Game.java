package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Objects;

/**
 * <h1>Game</h1>
 * <hr/>
 * A 2D platformer-shooter game where players can compete with AI or friends to be the first to reach 30 kills.
 * Weapons can be obtained from crates and the game supports up to 4 players.
 * Main Game class, derived from Space Invaders
 *
 * @author Anthony and Luke
 * @see SceneManager
 * @see <a href="https://mdinfotech.net/index.php?course=compsci12&unit=3#id37">Space Invaders Exercise</a>
 * @since 13-11-2024
 */

public class Game extends Canvas {
    public final static int weaponCount = 8;
    public static int WIDTH = 1600;
    public static int HEIGHT = 900;
    private final SceneManager sceneManager;
    public int[] skins = new int[4];
    public boolean[] types = new boolean[4];
    public int[] teams = new int[4];
    public int playerCount;
    BufferStrategy strategy;

    /*
     * Construct our game and set it running.
     */
    public Game() {
        JFrame container = new JFrame("Cube Force");

        // Set the window icon
        try {
            Image icon = ImageIO.read(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResource("main/sprites/game.ico")));
            container.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Icon not found: " + e.getMessage());
        }

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

    /**
     * Main Program
     */
    public static void main(String[] args) {

        // instantiate this object
        new Game();
    } // main

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
} // main.Game
