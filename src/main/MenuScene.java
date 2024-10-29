package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import main.utility.Button;

import static main.Game.height;
import static main.Game.width;

public class MenuScene extends Scene {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private Button startButton;
    private Button settingsButton;
    private Sprite background;

    MenuScene(Game game) {
        super(game);
        startButton = new Button("start.png", 136, 507, this::startGame);
        settingsButton = new Button("settings.png", 136, 661, this::openSettings);
        background = SpriteStore.get().getSprite("background.png");
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
        // calc. time since last update, will be used to calculate
        // entities movement
        // get graphics context for the accelerated surface and make it black
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        background.draw(g, 0, 0);
        g.setColor(Color.white);

        startButton.render(g);
        settingsButton.render(g);

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    }

    private void startGame() {
        System.out.println("Starting main game");
        game.setScene(new GameScene(game));
    }

    private void openSettings() {
        System.out.println("Openings Settings");
    }
}