package main;

import main.utility.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;
import static main.utility.TextManager.drawCenteredString;
import static main.utility.TextManager.getFont;

/**
 * <h1>Menu Scene</h1>
 * <hr/>
 * Scene after a player wins; displays scores and
 * offers the option to rematch or go to menu
 *
 * @author Anthony and Luke
 * @see Scene
 * @since 13-11-2024
 */

public class EndScene extends Scene {
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final Button rematchButton;
    private final Button menuButton;
    private final Sprite background;
    private final String winMessage;
    private final int[] score;
    private final Integer[] rank;

    EndScene(Game game, int winner, int[] killCount) {
        super(game);
        score = killCount;
        winMessage = "Team " + (winner + 1) + " wins";
        rematchButton = new Button("buttons/rematch.png", 136, 507, this::rematch);
        menuButton = new Button("buttons/menu.png", 136, 661, this::goToMenu);
        background = SpriteStore.get().getSprite("background.png");

        rank = rank(score);
    }


    @Override
    protected void handleMouseEvent(MouseEvent e) {
        rematchButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
        menuButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        background.draw(g, 0, 0);
        g.setColor(Color.white);

        g.setFont(getFont(48));
        drawCenteredString(g, (winMessage), 800, 100);

        g.setFont(getFont(24));
        for (int i = 0; i < rank.length; i++) {
            drawCenteredString(g, String.format("Rank %d: Team %d, %d/30", i + 1, rank[i] + 1, score[rank[i]]), 800, 200 + 50 * i);
        }
        rematchButton.draw(g);
        menuButton.draw(g);

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    }

    private void rematch() {
        game.setScene(new GameScene(game));
    }

    private void goToMenu() {
        game.setScene(new MenuScene(game));
    } // goToMenu

    private Integer[] rank(int[] scores) {
        Integer[] indexes = new Integer[scores.length];
        for (int i = 0; i < scores.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, (i, j) -> Integer.compare(scores[j], scores[i]));
        return indexes;
    }

}