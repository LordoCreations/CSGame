/* Game.java
 * Space Invaders Main Program
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Game extends Canvas {


    private BufferStrategy strategy;
    private boolean gameRunning = true;
    private ArrayList entities = new ArrayList();
    private ArrayList removeEntities = new ArrayList();
    private String message = "";
    private boolean logicRequiredThisLoop = false;
    private int width =  1600;
    private int height = 900;

    /*
     * Construct our game and set it running.
     */
    public Game() {
        // create a frame to contain game
        JFrame container = new JFrame("Alien Invader");

        // get hold the content of the frame
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

        // add key listener to this canvas
        addKeyListener(new KeyInputHandler());

        // request focus so key events are handled by this canvas
        requestFocus();

        // create buffer strategy to take advantage of accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialize entities
        initEntities();

        // start the game
        gameLoop();
    } // constructor


    /* initEntities
     * input: none
     * output: none
     * purpose: Initialise the starting state of the ship and alien entities.
     *          Each entity will be added to the array of entities in the game.
     */
    private void initEntities() {
        // TODO Entities
        entities.add();

    } // initEntities

    /* Notification from a game entity that the logic of the game
     * should be run at the next opportunity
     */
    public void updateLogic() {
        logicRequiredThisLoop = true;
    } // updateLogic

    /* Remove an entity from the game.  It will no longer be
     * moved or drawn.
     */
    public void removeEntity(Entity entity) {
        removeEntities.add(entity);
    } // removeEntity

    /* Notification that the player has died.
     */

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
        long lastLoopTime = System.currentTimeMillis();

        // keep loop running until game ends
        while (gameRunning) {

            // calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, width, height);

            // move each entity
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.move(delta);
            } // for

            // draw all entities
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.draw(g);
            } // for

            // TODO Optimize
            // brute force collisions, compare every entity
            // against every other entity.  If any collisions
            // are detected notify both entities that it has
            // occurred
            for (int i = 0; i < entities.size(); i++) {
                for (int j = i + 1; j < entities.size(); j++) {
                    Entity me = (Entity) entities.get(i);
                    Entity him = (Entity) entities.get(j);

                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    } // if
                } // inner for
            } // outer for

            // remove dead entities
            entities.removeAll(removeEntities);
            removeEntities.clear();

            // TODO Optimize
            // run logic if required
            if (logicRequiredThisLoop) {
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.doLogic();
                } // for
                logicRequiredThisLoop = false;
            } // if

            // clear graphics and flip buffer
            g.dispose();
            strategy.show();

            // ship should not move without user input
            if (ship.y == height - 50) ship.setHorizontalMovement(0);

            // respond to user moving ship
            if ((leftPressed) && (!rightPressed) && ship.y == height - 50) {
                ship.setHorizontalMovement(-moveSpeed);
            } else if ((rightPressed) && (!leftPressed) && ship.y == height - 50) {
                ship.setHorizontalMovement(moveSpeed);
            } // else
            if (upPressed && Math.abs(ship.y - height + 50) < 5) {
                ship.setVerticalMovement(-300);
            } else {
                if (ship.y < height - 50) {
                    ship.setVerticalMovement(ship.getVerticalMovement() + 15);
                    ship.setHorizontalMovement(ship.getHorizontalMovement()/1.05);
                } else if (ship.y > height - 50) {
                    ship.setVerticalMovement(0);
                    ship.y = height - 50;
                }
            }

            // if spacebar pressed, try to fire
            if (firePressed && !isPaused) {
                tryToFire();
            } // if

            // if b pressed, try to fire
            if (bombPressed && !isPaused) {
                tryToBomb();
            } // if

            // pause
            try {
                Thread.sleep(17);
            } catch (Exception e) {}
        } // while
    } // gameLoop


    /* startGame
     * input: none
     * output: none
     * purpose: start a fresh game, clear old data
     */
    private void startGame() {
        // clear out any existing entities and initalize a new set
        entities.clear();

        initEntities();

        // blank out any keyboard settings that might exist

    } // startGame


    /* inner class KeyInputHandler
     * handles keyboard input from the user
     */
    private class KeyInputHandler extends KeyAdapter {

        private int pressCount = 1;  // the number of key presses since
        // waiting for 'any' key press

        /* The following methods are required
         * for any class that extends the abstract
         * class KeyAdapter.  They handle keyPressed,
         * keyReleased and keyTyped events.
         */
        public void keyPressed(KeyEvent e) {

            // if waiting for keypress to start game, do nothing
            if (waitingForKeyPress) {
                return;
            } // if

            // respond to move left, right or fire
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_B) {
                bombPressed = true;
            } // if

        } // keyPressed

        public void keyReleased(KeyEvent e) {
            // if waiting for keypress to start game, do nothing
            if (waitingForKeyPress) {
                return;
            } // if

            // respond to move left, right or fire
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_B) {
                bombPressed = false;
            } // if

        } // keyReleased

        public void keyTyped(KeyEvent e) {

        } // keyTyped

    } // class KeyInputHandler


    /**
     * Main Program
     */
    public static void main(String[] args) {
        // instantiate this object
        new Game();
    } // main
} // Game
