package main;

import main.entities.*;
import main.utility.*;

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
    private int[] killCount = new int[4];
    public static final int KILLS_TO_WIN = 30;
    private final String[] backgroundTracks = new String[] {"ricochetlove.wav"};
    private Graphics2D g;

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

        killCount = new int[] {0, 0, 0, 0};
        entities.add(new Score(killCount, 20, 20));

        for (int i = 0; i < 4; i++) {
            // TODO add player or not
            if (game.types[i])
                players[i] = new Player(this, Display.getSkinURL(game.skins[i]), 0, 0, 100, game.teams[i], i);
            else {
                players[i] = new AIPlayer(this, Display.getSkinURL(game.skins[i]), 0, 0, 100, game.teams[i], i, players);
            }

            spawnPlayer(players[i], SPAWN_POINTS[players[i].getTeam()]);
            entities.add(players[i]);
            entities.add(new Bar(players[i]));
            entities.add(new AmmoBar(players[i]));
        }

        AudioManager.playSound(backgroundTracks[(int) (Math.random() * backgroundTracks.length)], true);

    }

    @Override
    public void update() {
        // calc. time since last update, will be used to calculate
        // entities movement
        long delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();

        // get graphics context for the accelerated surface and make it black
        g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        background.draw(g, 0, 0);

        // end game if kills to win reached
        for (int i = 0; i < killCount.length; i++) {
            if (killCount[i] >= KILLS_TO_WIN) game.setScene(new EndScene(game, i));
        } // for

        // respawn dead players
        for(Player p : players) {
            if(p != null && p.isDead && p.getRespawnTime() <= System.currentTimeMillis()){
                spawnPlayer(p, SPAWN_POINTS[(int) (Math.random() * SPAWN_POINTS.length)]);
            } // if
        } // for

        // add Chest
        if (Math.random() < 0.0001 * delta) {
            entities.add(new Chest(this, "chest.png", (int) (Math.random() * (WIDTH-400) + 200), (int) (Math.random() * (HEIGHT-400) + 200)));
        } // if

        // move each entity
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.move(delta);
        } // for

        // TODO Optimize collisions
        for (int i = 0; i < entities.size(); i++) {
            Entity me = entities.get(i);
            if (me instanceof Bullet) {
                if (touchingWall(me)) {
                    ((Bullet) me).collidedWith();
                }
                for (Player him : players) {
                    if (him == null) continue;
                    if (me.collidesWith(him) && ((Bullet) me).getTeam() != him.getTeam()) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    }
                }
            } else if (me instanceof Chest) {
                for (Player him : players) {
                    if (him == null) continue;
                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
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

        // clear removed sounds
        AudioManager.clearRemovedSounds();

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();

        // TODO remove
        if(AudioManager.sfx.size() >= 10) {
            System.out.println("large amount of sounds: " + AudioManager.sfx.size());
        } // if
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

    public void removeEntity(Entity e) { removeEntities.add(e); }

    public void playerDied(Player p, int killCredit, double bulletSpeed) {
        System.out.printf("Player %d died to team %d%n", p.getID(), killCredit);
        killCount[killCredit]++;
        p.isDead = true;
        Corpse c = new Corpse((this), ("skins/corpse" + p.getCorpseID() + ".png"), (int) p.x, (int) p.y);
        c.setDx(bulletSpeed);
        entities.add(c);
        p.setRespawnTime(System.currentTimeMillis() + 3000);
        p.setCoord(new int[]{100, -100});
        p.setWeapon(0);

    } // playerDied

    private void spawnPlayer(Player p, int[] location) {
        p.isDead = false;
        p.setWeapon((int) (Math.random() * Game.weaponCount));
        p.hp = p.getMaxHp();
        p.setCoord(location);
        p.setSpawntime(System.currentTimeMillis());
        p.setDirection(location[0] > WIDTH/2);
    }
}