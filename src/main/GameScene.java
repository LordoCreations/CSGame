package main;

import main.entities.*;
import main.utility.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static main.Game.HEIGHT;
import static main.Game.WIDTH;

/**
 * <h1>GameScene</h1>
 * <hr/>
 * Scene for the Game. Manages some collision logic, player and chest spawning, death timers, etc.
 *
 * @author Anthony and Luke
 * @see SceneManager
 * @see Player
 * @see Chest
 * @since 13-11-2024
 */

public class GameScene extends Scene {
    private long lastLoopTime = System.currentTimeMillis();
    public final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<Entity> removeEntities = new ArrayList<>();
    private final Sprite background;
    private final Color backgroundColor = new Color(30, 32, 35);
    private Mask wall;
    public Set<Integer> keysDown = new HashSet<>();
    private final Player[] players;
    private int[] killCount = new int[4];
    public static final int KILLS_TO_WIN = 30;
    private static final int[][] SPAWN_POINTS = {{220, 250}, {1380, 250}, {220, 550}, {1380, 550}};
    private final String[] backgroundTracks = new String[]{"ricochetlove.wav"};
    private Graphics2D g;

    /**
     * Game Scene Constructor
     * @param game Game object
     */
    GameScene(Game game) {
        super(game);
        players = new Player[game.playerCount];
        background = SpriteStore.get().getSprite("city.png");

        // get the wall hitbox
        try {
            BufferedImage maskImage = ImageIO.read(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResource("main/sprites/" + "city_hitbox" + ".png")));
            wall = new Mask(maskImage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } // try catch
    } // GameScene

    /**
     * Initializes the game, spawns players, and starts background track
     */
    @Override
    public void init() {
        killCount = new int[4];
        entities.add(new Score(killCount, 20, 20));

        // Spawn each player and attach ammo and health bar
        for (int i = 0; i < game.playerCount; i++) {
            if (game.types[i])
                players[i] = new Player(this, Display.getSkinURL(game.skins[i]), 0, 0, 100, game.teams[i], i);
            else {
                players[i] = new AIPlayer(this, Display.getSkinURL(game.skins[i]), 0, 0, 100, game.teams[i], i, players);
            } // if

            spawnPlayer(players[i], SPAWN_POINTS[players[i].getTeam()]);
            entities.add(players[i]);
            entities.add(new Bar(players[i]));
            entities.add(new AmmoBar(players[i]));
        } // for

        AudioManager.playSound(backgroundTracks[(int) (Math.random() * backgroundTracks.length)], true);
    } // init

    /**
     * Updates the game each tick
     */
    @Override
    public void update() {

        // time since last tick
        long delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();

        // get graphics context for the accelerated surface and draw background image
        g = (Graphics2D) game.strategy.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        background.draw(g, 0, 0);

        // end game if kills to win reached
        for (int i = 0; i < killCount.length; i++) {
            if (killCount[i] >= KILLS_TO_WIN) {
                game.setScene(new EndScene(game, i, killCount));
                return;
            } // if
        } // for

        // respawn dead players
        for (Player p : players) {
            if (p != null && p.isDead && p.getRespawnTime() <= System.currentTimeMillis()) {
                spawnPlayer(p, SPAWN_POINTS[(int) (Math.random() * SPAWN_POINTS.length)]);
            } // if
        } // for

        // add chests every few seconds, frequency increases if there are more player
        if (Math.random() < 0.00005 * game.playerCount * delta) {
            entities.add(new Chest(this, (int) (Math.random() * (WIDTH - 400) + 200), (int) (Math.random() * (HEIGHT - 400) + 200)));
        } // if

        // move each entity
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.move(delta);
        } // for

        // handle entity collisions
        for (int i = 0; i < entities.size(); i++) {
            Entity me = entities.get(i);

            if (me instanceof Bullet) {

                // handle bullet and player collisions
                if (touchingWall(me)) ((Bullet) me).collidedWith();

                // handle bullet and wall collisions
                for (Player him : players) {
                    me.collidedWith(him);
                } // for
            } else if (me instanceof Chest) {

                // handle chest collisions
                for (Player him : players) {
                    if (him != null && me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    } // if
                } // for
            } // if else
        } // for

        // draw all entities
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity e = entities.get(i);
            e.draw(g);
        } // for

        // remove destroyed entities
        entities.removeAll(removeEntities);
        removeEntities.clear();

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();

        // TODO remove
        if (AudioManager.sfx.size() >= 10) {
            //System.out.println("large amount of sounds: " + AudioManager.sfx.size());
        } // if
        if (AudioManager.music.size() >= 2) {
            //System.out.println("oversize music array: " + AudioManager.music.size());
        } // if
    } // update


    /**
     * Handle key being pressed by adding to keysDown
     * @param e key being pressed
     */
    @Override
    protected void handleKeyPressed(KeyEvent e) {
        keysDown.add(e.getKeyCode());
    } // keyPressed

    /**
     * Handle key being released by removing from keysDown
     * @param e key being released
     */
    @Override
    protected void handleKeyReleased(KeyEvent e) {
        keysDown.remove(e.getKeyCode());
    } // keyReleased

    /**
     * Check if hitbox of wall overlaps Entity hitbox
     * @param e Entity to check
     * @return if hitbox touches Entity hitbox
     */
    public boolean touchingWall(Entity e) {
        return wall.overlaps(e.hitbox);
    } // touchingWall

    /**
     * Removes an entity from the game
     * @param e Entity to remove
     */
    public void removeEntity(Entity e) {
        removeEntities.add(e);
    } // removeEntity

    /**
     * Handle player deaths
     * @param p player who died
     * @param killCredit team of the bullet that killed the player
     */
    public void playerDied(Player p, int killCredit) {
        System.out.printf("Player %d died to team %d%n", p.getID(), killCredit);

        // set respawn timer for player
        p.isDead = true;
        p.setRespawnTime(System.currentTimeMillis() + 3000);
        p.setCoord(new int[]{100, -100});
        p.setWeapon(0);

        // give a kill to team that killed player
        killCount[killCredit]++;

        // add a Corpse
        entities.add(new Corpse((this), ("skins/corpse" + p.getCorpseID() + ".png"), (int) p.x, (int) p.y));
    } // playerDied

    /**
     * Spawn the player
     * @param p player to spawn
     * @param location where to spawn player
     */
    private void spawnPlayer(Player p, int[] location) {
        p.isDead = false;
        p.setWeapon(0);
        p.hp = p.getMaxHp();
        p.setCoord(location);
        p.setSpawntime(System.currentTimeMillis());
        p.setDirection(location[0] > WIDTH / 2);
    } // spawnPlayer
} // GameScene