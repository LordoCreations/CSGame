package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import main.utility.Button;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

public class MenuScene extends Scene {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final Button startButton;
    private final Button settingsButton;
    private final Sprite background;

    MenuScene(Game game) {
        super(game);
        startButton = new Button("buttons/play.png", 23, 664, this::startGame);
        settingsButton = new Button("buttons/settings.png", 23, 782, this::openSettings);
        background = SpriteStore.get().getSprite("menubg.png");
    }

    @Override
    public void init() {

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
        startButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
        settingsButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);

    }

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
    }

    private void startGame() {
        game.setScene(new CustomizationScene(game));
    }

    private void openSettings() {
        System.out.println("Openings Settings");
    }
}