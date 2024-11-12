package main.utility;

import main.Entity;
import main.GameScene;

import java.awt.*;
import java.io.File;

public class Score extends Entity {
    private final int[] scores;
    private int leader;
    private Font font;
    private String display;

    public Score(int[] scores, int x, int y) {
        super("displays/contested.png", x, y);

        font = TextManager.getFont(24);
        this.scores = scores;
    }

    @Override
    public void collidedWith(Entity other) {
    }

    @Override
    public void draw(Graphics g) {
        int lastLeader = leader;
        getLeader();

        if (leader == -1) {
            display = "displays/contested.png";
        } else {
            display = "displays/team" + leader + ".png";
        }

        if (lastLeader != leader) super.setSprite(display);

        super.draw(g);

        String message;
        if (leader == -1) message = "Contested!";
        else
            message = String.format("Team %d is leading with %d/%d Kills", leader + 1, scores[leader], GameScene.KILLS_TO_WIN);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (int) x + 20, (int) y + 40);

    }

    private void getLeader() {
        int leaderScore = 0;
        leader = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == leaderScore) leader = -1;
            else if (scores[i] > leaderScore) {
                leader = i;
                leaderScore = scores[i];
            }
        }
    }
}
