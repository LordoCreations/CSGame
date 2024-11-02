package main;

import main.entities.Bullet;
import main.entities.Weapon;
import main.utility.AmmoBar;
import main.utility.Bar;
import main.utility.Mask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import main.entities.Player;

import static main.Game.height;
import static main.Game.width;

public class GameScene extends Scene {
    private long lastLoopTime = System.currentTimeMillis();
    public final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<Entity> removeEntities = new ArrayList<>();
    private Sprite background;
    private Mask wall;
    public Set<Integer> keysDown = new HashSet<>();
    private Player[] players = new Player[4];

    private static int[][] SPAWN_POINTS = {{220, 250}, {1380, 250}};

    GameScene(Game game) {
        super(game);
        background = SpriteStore.get().getSprite("wall.png");
        try {
            URL url = this.getClass().getClassLoader().getResource("main/sprites/" + "wall" + ".png");
            BufferedImage maskImage = ImageIO.read(url);
            wall = new Mask(maskImage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() {
        // Initialize game components, load assets, etc.
        System.out.println("e");
        game.addKeyListener(new KeyInputHandler());

        players[0] = new Player(this, "rambo.png", 220, 250, 100, 1, 0);
        players[1] = new Player(this, "globey.png", 220, 250, 100, 2, 1);

        for (Player player : players) {
            if (player == null) continue;
            entities.add(player);
            entities.add(new Bar(player));
            entities.add(new AmmoBar(player));
        }

        players[0].setWeapon(4);
        players[1].setWeapon(5);

    }

    @Override
    public void update() {
        // calc. time since last update, will be used to calculate
        // entities movement
        long delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();

        // get graphics context for the accelerated surface and make it black
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        background.draw(g, 0, 0);


        // move each entity
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.move(delta);
        }

        // TODO Optimize collisions
        for (int i = 0; i < entities.size(); i++) {
            Entity me = entities.get(i);
            if (me instanceof Bullet) {
                if (touchingWall(me)) {
                    removeEntities.add(me);
                }
                for (int j = 0; j < entities.size(); j++) {
                    Entity him = entities.get(j);
                    if (him instanceof Player && me.collidesWith(him) && ((Player) him).getTeam() != ((Bullet) me).getTeam()) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                        removeEntities.add(me);
                    }
                }
            }
        }

        // draw all entities
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.draw(g);
        }


        // remove dead entities
        entities.removeAll(removeEntities);
        removeEntities.clear();


        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    }

    /* inner class KeyInputHandler
     * handles keyboard input from the user
     */
    private class KeyInputHandler extends KeyAdapter {

        /* The following methods are required
         * for any class that extends the abstract
         * class KeyAdapter.  They handle keyPressed,
         * keyReleased and keyTyped events.
         */
        @Override
        public void keyPressed(KeyEvent e) {
            keysDown.add(e.getKeyCode());
        } // keyPressed

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(e.getKeyCode());
        } // keyReleased

        @Override
        public void keyTyped(KeyEvent e) {
            // if escape is pressed, end game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            } // if escape pressed
        } // keyTyped

    } // class KeyInputHandler

    public boolean touchingWall(Entity e) {
        return wall.overlaps(e.hitbox);
    }

    public void removeEntity(Entity e) {
        removeEntities.add(e);
    }

    public void playerDied(Player p, int killCredit) {
        System.out.printf("Player %d died to team %d%n", p.getID(), killCredit);
        p.setDirection(false);
        p.setWeapon((int) (Math.random() * 6));
        p.hp = p.getMaxHp();
        p.setCoord(SPAWN_POINTS[(int) (Math.random() * SPAWN_POINTS.length)]);
        p.setSpawntime(System.currentTimeMillis());
    }
}