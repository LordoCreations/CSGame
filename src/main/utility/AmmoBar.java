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
        g.fillRoundRect(x, y, width, 4, 2, 2);
        g.setColor(new Color(228, 184, 99));
        g.fillRoundRect(x, y, (int) (follow.getAmmo() / follow.getMaxAmmo() * width), 4, 2, 2);
        g.setColor(new Color(213, 139, 86));
        g.drawRoundRect(x, y, width, 4, 2, 2);
    }

    @Override
    public void collidedWith(Entity other) {}

    @Override public void move(long delta) {
        this.x = follow.getX() + (follow.getWidth() - width)/2;
        this.y = follow.getY() - 20;
    }
}
