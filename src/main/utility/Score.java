package main.utility;

import main.Entity;
import main.GameScene;

import java.awt.*;

/**
 * <h1>Score</h1>
 * <hr/>
 * Displays the score of the leading team
 *
 * @author Anthony and Luke
 * @since 14-11-2024
 */

public class Score extends Entity {
    private final int[] scores;
    private final Font font;
    private int leader;

    /**
     * Class constructor
     *
     * @param scores array that holds scores
     * @param x      x position of scoreboard
     * @param y      y position of scoreboard
     */
    public Score(int[] scores, int x, int y) {
        super("displays/contested.png", x, y);

        font = TextManager.getFont(24);
        this.scores = scores;
    } // Score

    /**
     * Unused logic for collisions
     *
     * @param other entity that collided with the object
     */
    @Override
    public void collidedWith(Entity other) {
    } // collidedWith

    /**
     * Draws the scoreboard
     *
     * @param g Graphics object
     */
    @Override
    public void draw(Graphics g) {
        int lastLeader = leader;
        String display;

        getLeader();

        if (leader == -1) display = "displays/contested.png";
        else display = "displays/team" + leader + ".png";

        if (lastLeader != leader) super.setSprite(display);

        super.draw(g);

        String message;
        if (leader == -1) message = "Contested!";
        else
            message = String.format("Team %d is leading with %d/%d Kills", leader + 1, scores[leader], GameScene.KILLS_TO_WIN);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (int) x + 20, (int) y + 40);
    } // draw

    /**
     * Update the scoreboard's leading team and their score
     */
    private void getLeader() {
        int leaderScore = 0;
        leader = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == leaderScore) leader = -1;
            else if (scores[i] > leaderScore) {
                leader = i;
                leaderScore = scores[i];
            } // else
        } // for
    } // getLeader
} // Score
