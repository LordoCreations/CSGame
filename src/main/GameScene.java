package main;

import main.entities.*;
import main.utility.AmmoBar;
import main.utility.Bar;
import main.utility.Display;
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

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

public class GameScene extends Scene {
    private long lastLoopTime = System.currentTimeMillis();
    public final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<Entity> removeEntities = new ArrayList<>();
    private final Sprite background;
    private Mask wall;
    public Set<Integer> keysDown = new HashSet<>();
    private final Player[] players = new Player[4];
    private final Color backgroundColor = new Color(30, 32, 35);

    private static final int[][] SPAWN_POINTS = {{220, 250}, {1380, 250}, {220, 550}, {1380, 550}};

    GameScene(Game game) {
        super(game);
        background = SpriteStore.get().getSprite("city.png");
        try {
            URL url = this.getClass().getClassLoader().getResource("main/sprites/" + "city_hitbox" + ".png");
            BufferedImage maskImage = ImageIO.read(url);
            wall = new Mask(maskImage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() {

        // Initialize game components, load assets, etc.
        game.addKeyListener(new KeyInputHandler());

        players[0] = new Player(this, Display.getSkinURL(game.skins[0]), 220, 250, 100, 0, 0);
        players[1] = new Player(this, Display.getSkinURL(game.skins[1]), 220, 250, 100, 1, 1);
        players[2] = new AIPlayer(this, Display.getSkinURL(game.skins[2]), 220, 250, 100, 2, 2, players);

        for (Player player : players) {
            if (player == null) continue;
            spawnPlayer(player, SPAWN_POINTS[player.getID()]);
            // TODO set player Skin (array in Game.java)
            entities.add(player);
            entities.add(new Bar(player));
            entities.add(new AmmoBar(player));

        }

    }

    @Override
    public void update() {
        // calc. time since last update, will be used to calculate
        // entities movement
        long delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();

        // get graphics context for the accelerated surface and make it black
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        background.draw(g, 0, 0);

        // respawn dead players
        for(Player p : players) {
            if(p != null && p.isDead && p.getRespawnTime() <= System.currentTimeMillis()){
                spawnPlayer(p, SPAWN_POINTS[(int) (Math.random() * SPAWN_POINTS.length)]);
            } // if
        } // for

        // add Chest
        if (Math.random() < 0.0001 * delta) {
            entities.add(new Chest(this, "chest.png", (int) (Math.random() * (WIDTH-400) + 200), (int) (Math.random() * (HEIGHT-400) + 200)));
        }

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
                for (Player him : players) {
                    if (him == null) continue;
                    if (me.collidesWith(him) && ((Bullet) me).getTeam() != him.getTeam()) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                        removeEntities.add(me);
                    }
                }
            } else if (me instanceof Chest) {
                for (Player him : players) {
                    if (him == null) continue;
                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                        removeEntities.add(me);
                    }
                }
            }
        }

        // draw all entities
        for (int i = entities.size()-1; i >= 0; i--) {
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
        p.isDead = true;
        Corpse c = new Corpse((this), ("corpse" + p.getCorpseID() +".png"), (int) p.x, (int) p.y);
        entities.add(c);
        p.setRespawnTime(System.currentTimeMillis() + 3000);
        p.setCoord(new int[]{100, -100});
        p.setWeapon(0);

    }

    private void spawnPlayer(Player p, int[] location) {
        p.isDead = false;
        p.setDirection(false);
        p.setWeapon((int) (Math.random() * 6));
        p.hp = p.getMaxHp();
        p.setCoord(location);
        p.setSpawntime(System.currentTimeMillis());
    }
}