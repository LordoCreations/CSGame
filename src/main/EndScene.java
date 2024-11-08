package main;

import main.utility.Button;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;
import static main.utility.Carousel.drawCenteredString;

public class EndScene extends Scene {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final Button rematchButton;
    private final Button menuButton;
    private final Sprite background;
    private String winMessage;

    EndScene(Game game, int winner) {
        super(game);
        winMessage = new String("Team " + (winner + 1) + " wins");
        rematchButton = new Button("buttons/rematch.png", 136, 507, this::startGame);
        menuButton = new Button("buttons/menu.png", 136, 661, this::goToMenu);
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
        rematchButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
        menuButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);

    }

    @Override
    public void update() {
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        background.draw(g, 0, 0);
        g.setColor(Color.white);

        drawCenteredString(g, (winMessage), 800, 400);
        rematchButton.draw(g);
        menuButton.draw(g);

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    }

    private void startGame() {
        game.setScene(new GameScene(game));
    }

    private void goToMenu() { game.setScene(new MenuScene(game)); }

    private void openSettings() {
        System.out.println("Openings Settings");
    }
}