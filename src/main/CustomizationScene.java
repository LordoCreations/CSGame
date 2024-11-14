package main;

import main.utility.Button;
import main.utility.Carousel;
import main.utility.Display;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

/**
 * <h1>End Scene</h1>
 * <hr/>
 * Scene before game, allows players to customize player count,
 * teams, skins, and if players are AI or user controlled.
 *
 * @author Anthony and Luke
 * @see Scene
 * @since 14-11-2024
 */

public class CustomizationScene extends Scene {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final Button startButton;
    private final Button menuButton;
    private final Sprite background;

    private final String[] skins = {"Default", "Locked In", "Stormtrooper", "Soldier", "Rambo"};
    private final String[] teams = {"Team Blue", "Team Red", "Team Purple", "Team Green"};
    private final String[] types = {"Player", "AI"};
    private final String[] players = {"Players: 2", "Players: 3", "Players: 4"};

    /**
     * Constructor for scene
     * @param game Game object
     */
    CustomizationScene(Game game) {
        super(game);

        startButton = new Button("buttons/start.png", WIDTH - 458, 735, this::enterGame);
        menuButton = new Button("buttons/menu.png", 136, 735, this::goToMenu);

        // Create a carousel for skin, team, control-type selection
        for (int i = 0; i < game.skins.length; i++) {
            Display d = new Display(game, 137 + 360 * i, 100, i);
            Carousel skin = new Carousel(137 + 360 * i, 400, 280, i, 0, skins);
            Carousel type = new Carousel(137 + 360 * i, 500, 280, i, 1, types);
            Carousel team = new Carousel(137 + 360 * i, 600, 280, i, 2, teams);

            skin.setChoice(i);
            team.setChoice(i);
            type.setChoice(i >= 2 ? 1 : 0);
            entities.add(d);
            entities.add(skin);
            entities.add(type);
            entities.add(team);
        } // for

        // Create carousel for player count selection
        Carousel playerCountSelector = new Carousel(WIDTH / 2 - 140, 700, 280, 0, 3, players);
        playerCountSelector.setChoice(2);
        entities.add(playerCountSelector);

        background = SpriteStore.get().getSprite("background.png");
    } // CustomizationScene


    /**
     * Since Customization Scene has so many entities,
     * they are added to an entities array here
     */
    @Override
    public void init() {
        entities.add(startButton);
        entities.add(menuButton);
    } // init

    /**
     * Handles the mouse input for clicking on buttons
     *
     * @param e the mouse action (i.e. click, move)
     */
    @Override
    protected void handleMouseEvent(MouseEvent e) {
        for (Entity entity : entities) {
            if (entity instanceof Carousel) {
                ((Carousel) entity).update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
            } // if
            if (entity instanceof Button) {
                ((Button) entity).update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
            } // if
        } // for
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
        g.setColor(Color.white);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        for (Entity entity : entities) {
            entity.draw(g);
            if (entity instanceof Carousel) {
                switch (((Carousel) entity).getPurpose()) {
                    case 1:
                        game.types[((Carousel) entity).getID()] = ((Carousel) entity).getChoice() == 0;
                        break;
                    case 2:
                        game.teams[((Carousel) entity).getID()] = ((Carousel) entity).getChoice();
                        break;
                    case 3:
                        game.playerCount = ((Carousel) entity).getChoice() + 2;
                        break;
                    default:
                        game.skins[((Carousel) entity).getID()] = ((Carousel) entity).getChoice();
                } // switch
            } // if

            if (entity instanceof Display) {
                if (!((Display) entity).getR().equals(Display.getSkinURL(game.skins[((Display) entity).getId()]))) {
                    ((Display) entity).update(game.skins[((Display) entity).getId()]);
                } // if
                ((Display) entity).setIfUsing(game.playerCount > ((Display) entity).getId());
            } // if
        } // for

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    } // update

    /**
     * Button action called by startButton, starts game
     */
    private void enterGame() {
        game.setScene(new GameScene(game));
    } // enterGame

    /**
     * Button action called by menuButton, returns to menu
     */
    private void goToMenu() {
        game.setScene(new MenuScene(game));
    } // gotToMenu
} // CustomizationScene