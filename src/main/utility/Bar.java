package main.utility;

import main.Colors;
import main.Entity;
import main.entities.Player;

import java.awt.*;

public class Bar extends Entity {
    protected int x;
    protected int y;
    protected final Player follow;
    protected final int width = 40;
    private final Color[] barColor;

    public Bar(Player follow) {
        super("test.png", 0, 0);
        this.follow = follow;
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 10;

        barColor = Colors.getTeamColors(follow.getTeam());
    }

    public void draw(Graphics g) {
        g.setColor(barColor[1]);
        g.fillRoundRect(x, y, width, 4, 2, 2);
        g.setColor(barColor[0]);
        g.fillRoundRect(x, y, (int) (follow.getHp() / follow.getMaxHp() * width), 4, 2, 2);
        g.setColor(new Color(0, 0, 0, 100));
        g.drawRoundRect(x, y, width, 4, 2, 2);
    }

    @Override
    public void collidedWith(Entity other) {}

    @Override public void move(long delta) {
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 10;
    }
}
