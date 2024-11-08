package main.utility;

import main.Colors;
import main.Entity;
import main.Game;

import java.awt.*;

public class Display extends Entity {
    private String r;
    private int id;
    private int choice;
    private Game game;

    public Display(Game game, int x, int y, int id) {
        super(getSkinURL(id), x, y);
        this.game = game;
        this.r = getSkinURL(id);
        this.id = id;
    } // Display


    public void update(int i) {
        this.r = getSkinURL(i);
        this.setSprite(r);
    }

    @Override
    public void draw(Graphics g) {
        Color[] colors = {Colors.getTeamColors(game.teams[id])[0], new Color(11,12,15, 0)};

        ((Graphics2D) g).setPaint(new LinearGradientPaint(0, 0, 0, 600, new float[]{0.0f, 1.0f}, colors));

        g.fillRect((int) (x - 15), (int) (y - 40), 310, 600);
        g.drawImage(this.sprite.image, (int) x + 84, (int) y + 84, sprite.getWidth() * 2, sprite.getHeight() * 2, null);
    }

    public static String getSkinURL(int i) {
        return switch (i) {
            case 1 -> "skins/locked.png";
            case 2 -> "skins/globey.png";
            case 3 -> "skins/soldier.png";
            case 4 -> "skins/rambo.png";
            default -> "skins/default.png";
        };
    }


    public String getR() {
        return r;
    }

    public int getId() {
        return id;
    }

    @Override
    public void collidedWith(Entity other) {
    }
}
