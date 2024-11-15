package main;

import main.entities.*;
import main.utility.*;
import main.utility.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
    private final ArrayList<Chest> chests = new ArrayList<>();
    private int[] killCount = new int[4];
    public static final int KILLS_TO_WIN = 30;
    private static final int[][] SPAWN_POINTS = {{220, 250}, {1380, 250}, {220, 550}, {1380, 550}};
    private final String[] backgroundTracks = new String[]{"ricochetlove.wav", "iamtheking.wav", "killingmachine.wav"};
    private boolean paused;
    private final Sprite pausePrompt;
    private final Button exitButton;

    /**
     * Game Scene Constructor
     *
     * @param game Game object
     */
    GameScene(Game game) {
        super(game);
        players = new Player[game.playerCount];
        background = SpriteStore.get().getSprite("city.png");
        exitButton = new Button("buttons/exit.png", 40, HEIGHT - 146, this::goToMenu);

        paused = false;
        pausePrompt = SpriteStore.get().getSprite("paused.png");

        // get the wall hitbox
        try {
            BufferedImage maskImage = ImageIO.read(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResource("main/sprites/" + "city_hitbox" + ".png")));
            wall = new Mask(maskImage);
        } catch (IOException e) {
            e.printStackTrace();
        } // try catch
    } // GameScene

    /**
     * Initializes the game, spawns players, and starts background track
     */
    @Override
    public void init() {
        killCount = new int[4];
        entities.add(new Score(killCount, 20, 20));

        GameTime.setTime(System.currentTimeMillis());

        // Spawn each player and attach ammo and health bar
        for (int i = 0; i < game.playerCount; i++) {
            if (game.types[i])
                players[i] = new Player(this, Display.getSkinURL(game.skins[i]), 0, 0, 100, game.teams[i], i);
            else {
                players[i] = new AIPlayer(this, Display.getSkinURL(game.skins[i]), 0, 0, 100, game.teams[i], i, players, chests);
            } // if

            spawnPlayer(players[i], SPAWN_POINTS[players[i].getTeam()]);
            entities.add(players[i]);
            entities.add(new Bar(players[i]));
            entities.add(new AmmoBar(players[i]));
        } // for

    } // init

    /**
     * Updates the game each tick
     */
    @Override
    public void update() {

        // time since last tick
        long delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();

        // adds a random track if none are playing
        if(AudioManager.music.isEmpty()) {
            AudioManager.playSound(backgroundTracks[(int) (Math.random() * backgroundTracks.length)], false, true);
        } // if

        // get graphics context for the accelerated surface and draw background image
        Graphics2D g = (Graphics2D) game.strategy.getDrawGraphics();
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
            if (p != null && p.isDead && p.getRespawnTime() <= GameTime.getTime()) {
                spawnPlayer(p, SPAWN_POINTS[(int) (Math.random() * SPAWN_POINTS.length)]);
            } // if
        } // for

        // add chests every few seconds, frequency increases if there are more player
        if (!paused && Math.random() < 0.00005 * game.playerCount * delta) {
            Chest c = new Chest(this, (int) (Math.random() * (WIDTH - 400) + 200), (int) (Math.random() * (HEIGHT - 400) + 200));
            entities.add(c);
            chests.add(c);
        } // if

        // move each entity
        if (!paused) {
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.move(delta);
            } // for
            GameTime.update(delta);
        } // if

        // handle entity collisions
        for (int i = 0; i < entities.size(); i++) {
            Entity me = entities.get(i);
            if (me instanceof Bullet) {

                // handle bullet and wall collisions
                if (touchingWall(me)) ((Bullet) me).collidedWith();

                // handle bullet and player collisions
                for (Player him : players) me.collidedWith(him);
            } else if (me instanceof Chest) {

                // handle chest collisions
                for (Player him : players) {
                    if (him != null && me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                        chests.remove(me);
                    } // if
                } // for
            } // if else
        } // for

        // draw all entities
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity e = entities.get(i);
            e.draw(g);
        } // for

        if (paused) {
            pausePrompt.draw(g, 0, 0);
            exitButton.draw(g);
        }

        // remove destroyed entities
        entities.removeAll(removeEntities);
        removeEntities.clear();

        // clear graphics and flip buffer
        g.dispose();
        game.strategy.show();
    } // update


    /**
     * Handle key being pressed by adding to keysDown
     *
     * @param e key being pressed
     */
    @Override
    protected void handleKeyPressed(KeyEvent e) {
        keysDown.add(e.getKeyCode());
    } // keyPressed

    /**
     * Handle key being released by removing from keysDown
     *
     * @param e key being released
     */
    @Override
    protected void handleKeyReleased(KeyEvent e) {
        keysDown.remove(e.getKeyCode());
    } // keyReleased

    /**
     * Handle key being typed
     *
     * @param e key being typed
     */
    @Override
    protected void handleKeyTyped(KeyEvent e) {
        if (e.getKeyChar() == 27) paused = !paused;
    } // handleKeyTyped

    /**
     * Handles the mouse input for clicking on buttons
     *
     * @param e the mouse action (i.e. click, move)
     */
    @Override
    protected void handleMouseEvent(MouseEvent e) {
        if (paused) exitButton.update(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON1);
    } // handleMouseEvent

    /**
     * Button action called by menuButton, returns to menu
     */
    private void goToMenu() {
        game.setScene(new MenuScene(game));
    } // goToMenu

    /**
     * Check if hitbox of wall overlaps Entity hitbox
     *
     * @param e Entity to check
     * @return if hitbox touches Entity hitbox
     */
    public boolean touchingWall(Entity e) {
        return wall.overlaps(e.hitbox);
    } // touchingWall

    /**
     * Removes an entity from the game
     *
     * @param e Entity to remove
     */
    public void removeEntity(Entity e) {
        removeEntities.add(e);
    } // removeEntity

    /**
     * Handle player deaths
     *
     * @param p player who died
     * @param killCredit team of the bullet that killed the player
     */
    public void playerDied(Player p, int killCredit) {

        // add a Corpse
        entities.add(new Corpse((this), ("skins/corpse" + p.getCorpseID() + ".png"), (int) p.x, (int) p.y));

        // set respawn timer for player
        p.isDead = true;
        p.setRespawnTime(GameTime.getTime() + 3000);
        p.setCoord(new int[]{100, -100});

        // give a kill to team that killed player
        killCount[killCredit]++;
    } // playerDied

    /**
     * Reset stats and spawn the player
     *
     * @param p        player to spawn
     * @param location where to spawn player
     */
    private void spawnPlayer(Player p, int[] location) {
        p.isDead = false;
        p.dx = 0;
        p.dy = 0;
        p.setKbDx(0);
        p.setRecoilDx(0);
        p.setWeapon(0);
        p.hp = p.getMaxHp();
        p.setCoord(location);
        p.setSpawntime(GameTime.getTime());
        p.setDirection(location[0] > WIDTH / 2);
    } // spawnPlayer
} // GameScene