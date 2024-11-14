package main.utility;

import main.*;

import java.awt.*;

public class Display extends Entity {
    private String r;
    private final int id;
    private final Game game;
    private Sprite unused;
    private boolean inUse;

    public Display(Game game, int x, int y, int id) {
        super(getSkinURL(id), x, y);
        unused = SpriteStore.get().getSprite("displays/notInUse.png");
        this.game = game;
        this.r = getSkinURL(id);
        this.id = id;
        this.inUse = true;
    } // Display


    public void update(int i) {
        this.r = getSkinURL(i);
        this.setSprite(r);
    }

    public void setIfUsing(boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public void draw(Graphics g) {
        Color[] colors = {Colors.getTeamColors(game.teams[id])[0], new Color(11,12,15, 0)};

        ((Graphics2D) g).setPaint(new LinearGradientPaint(0, 0, 0, 600, new float[]{0.0f, 1.0f}, colors));

        g.fillRect((int) (x - 15), (int) (y - 40), 310, 600);
        g.drawImage(this.sprite.image, (int) x + 74, (int) y + 74, sprite.getWidth() * 3, sprite.getHeight() * 3, null);
        if (!inUse) unused.draw(g, (int) x + 31, (int) y + 31);
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
    public void collidedWith(Entity other) {}
}
