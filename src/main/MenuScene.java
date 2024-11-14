package main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
    private final Button settingsButton;
    private final Sprite background;

    /**
     * Menu Scene Constructor
     *
     * @param game Game object
     */
    MenuScene(Game game) {
        super(game);
        startButton = new Button("buttons/play.png", 23, 664, this::startGame);
        settingsButton = new Button("buttons/settings.png", 23, 782, this::openSettings);
        background = SpriteStore.get().getSprite("menubg.png");
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
        settingsButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
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
        settingsButton.draw(g);

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

    // TODO change to tutorial
    private void openSettings() {
        System.out.println("Openings Settings");
    }
} // MenuScene