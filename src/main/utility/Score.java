package main.utility;

import main.Entity;
import main.GameScene;

import java.awt.*;
import java.io.File;

public class Score extends Entity {
    private int[] scores;
    private int leader;
    private int leaderScore;
    private boolean contested;
    private Font font;
    private String message;
    private int lastLeader;
    private boolean lastContested;

    public Score(int[] scores, int x, int y) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/utility/font.ttf")).deriveFont(24f);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        super.setSprite("contested.png");
        this.scores = scores;
        this.x = x;
        this.y = y;
    }

    @Override
    public void collidedWith(Entity other) {

    }

    @Override public void draw(Graphics g) {
        lastLeader = leader;
        lastContested = contested;
        getLeader();

        if (contested != lastContested) {
            super.setSprite("contested.png");
        }
        if (leader != lastLeader && !contested) super.setSprite("team" + leader + ".png");

        super.draw(g);

        if (contested) message = "Contested!";
        else message = String.format("Team %d is leading with %d/%d Kills", leader + 1, scores[leader], GameScene.KILLS_TO_WIN);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (int) x + 20, (int) y + 40);

    }

    private void getLeader() {
        leaderScore = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == leaderScore) contested = true;
            else if (scores[i] > leaderScore) {
                leader = i;
                leaderScore = scores[i];
                contested = false;
            }
        }
    }
}
