package main.utility;

import main.Entity;
import main.entities.Player;

import java.awt.*;

public class Bar extends Entity {
    protected int x;
    protected int y;
    protected final Player follow;
    protected final int width = 40;

    public Bar(Player follow) {
        super("test.png", 0, 0);
        this.follow = follow;
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 10;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(38, 78, 50));
        g.fillRoundRect(x, y, width, 4, 2, 2);
        g.setColor(new Color(124, 207, 149));
        g.fillRoundRect(x, y, (int) (follow.getHp() / follow.getMaxHp() * width), 4, 2, 2);
        g.setColor(new Color(84, 156, 106));
        g.drawRoundRect(x, y, width, 4, 2, 2);
    }

    @Override
    public void collidedWith(Entity other) {}

    @Override public void move(long delta) {
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 10;
    }
}
