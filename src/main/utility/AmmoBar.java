package main.utility;

import main.Entity;
import main.entities.Player;

import java.awt.*;

public class AmmoBar extends Bar {

    public AmmoBar(Player follow) {
        super(follow);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(113, 76, 22));
        g.fillRect(x, y, width, 4);
        g.setColor(new Color(228, 184, 99));
        g.fillRect(x, y, (int) (follow.getAmmo() / follow.getMaxAmmo() * width), 4);
        g.setColor(new Color(0, 0, 0, 100));
        g.drawRect(x, y, width, 4);
    }

    @Override public void move(long delta) {
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 20;
    }
}
