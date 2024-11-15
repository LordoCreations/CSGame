package main;

import main.utility.Button;

import java.awt.*;
import java.awt.event.MouseEvent;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

/**
 * <h1>Instructions Scene</h1>
 * <hr/>
 * Scene before game, allows players to customize player count,
 * teams, skins, and if players are AI or user controlled.
 *
 * @author Anthony and Luke
 * @see Scene
 * @since 14-11-2024
 */

public class InstructionsScene extends Scene {
    private final Button menuButton;
    private final Sprite background;

    /**
     * Constructor for scene
     *
     * @param game Game object
     */
    InstructionsScene(Game game) {
        super(game);

        menuButton = new Button("buttons/menu.png", 40, HEIGHT - 136, this::goToMenu);
        background = SpriteStore.get().getSprite("tutorial.png");
    } // InstructionsScene


    /**
     * The instructions scene does not need to initialize anything
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
        menuButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
    } // handleMouseEvent

    /**
     * Updates the scene by drawing the entities and background
     * Updates game data if carousels are clicked
     */
    @Override
    public void update() {
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        background.draw(g, 0, 0);

        menuButton.draw(g);

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    } // update

    /**
     * Button action called by menuButton, returns to menu
     */
    private void goToMenu() {
        game.setScene(new MenuScene(game));
    } // gotToMenu
} // InstructionsScene