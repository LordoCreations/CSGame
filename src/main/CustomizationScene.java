package main;

import main.utility.Button;
import main.utility.Carousel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static main.Game.height;
import static main.Game.width;

public class CustomizationScene extends Scene {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final Button startButton;
    private final Button menuButton;
    private final Sprite background;


    private final String[] skins = {"1/5", "2/5", "3/5", "4/5", "5/5"};

    CustomizationScene(Game game) {
        super(game);

        startButton = new Button("start.png", 136, 735, this::enterGame);
        menuButton = new Button("menu.png", 895, 735, this::goToMenu);

        for (int i = 0; i < game.skins.length; i++) {
            Carousel c = new Carousel(137 + 360 * i, 600, 280, i, skins);
            c.setChoice(i);
            entities.add(c);
        }

        background = SpriteStore.get().getSprite("background.png");
    }

    @Override
    public void init() {
        entities.add(startButton);
        entities.add(menuButton);

        // add mouse
        game.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseEvent(e);
            }
        });

        game.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseEvent(e);
            }
        });
    }

    private void handleMouseEvent(MouseEvent e) {
        for (Entity entity : entities) {
            if (entity instanceof Carousel) {
                ((Carousel) entity).update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
            }
            if (entity instanceof Button) {
                ((Button) entity).update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
            }
        }
    }

    @Override
    public void update() {

        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        background.draw(g, 0, 0);
        g.setColor(Color.white);

        for (Entity entity : entities) {
            entity.draw(g);
            if (entity instanceof Carousel) {
                game.skins[((Carousel) entity).getID()] = ((Carousel) entity).getChoice();
            }
        }

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    }

    private void enterGame() {
        game.setScene(new GameScene(game));
    }

    private void goToMenu() {
        game.setScene(new MenuScene(game));
    }
}