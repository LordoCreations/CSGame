package main;

import java.awt.*;
import java.awt.event.MouseEvent;

import main.utility.Button;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

/**
 * <h1>Menu Scene</h1>
 * <hr/>
 * Menu Scene of the game that allows user to start the game
 *
 * @author Anthony and Luke
 * @see Scene
 * @since 13-11-2024
 */

public class MenuScene extends Scene {
    private final Button startButton;
    private final Button tutorialButton;
    private final Button quitButton;
    private final Sprite background;

    /**
     * Menu Scene Constructor
     *
     * @param game Game object
     */
    MenuScene(Game game) {
        super(game);
        startButton = new Button("buttons/play.png", 23, 546, this::startGame);
        tutorialButton = new Button("buttons/instructions.png", 23, 664, this::openInstructions);
        quitButton = new Button("buttons/quit.png", 23, 782, this::quitGame);
        background = SpriteStore.get().getSprite("menuBG.png");
    } // MenuScene

    /**
     * The menu scene does not need to initialize anything
     */
    @Override
    public void init() {
    } // init

    /**
     * Handles the mouse input for clicking on buttons
     *
     * @param e the mouse action (i.e. click, move)
     */
    @Override
    protected void handleMouseEvent(MouseEvent e) {
        startButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
        tutorialButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
        quitButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
    } // handleMouseEvent

    /**
     * Updates the scene by drawing the buttons and background
     */
    @Override
    public void update() {
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        background.draw(g, 0, 0);
        g.setColor(Color.white);

        startButton.draw(g);
        tutorialButton.draw(g);
        quitButton.draw(g);

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    } // update

    /**
     * Button action called by startButton, starts the game
     */
    private void startGame() {
        game.setScene(new CustomizationScene(game));
    } // startGame

    /**
     * Button action called by tutorialButton, opens instructions
     */
    private void openInstructions() {
        game.setScene(new InstructionsScene(game));
    } // openInstructions

    /**
     * Button action called by quitButton, quits game
     */
    private void quitGame() {
        System.exit(0);
    } // quitGame
} // MenuScene