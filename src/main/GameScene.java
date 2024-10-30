package main;

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
    public Set<Character> keysDown = new HashSet<>();

    GameScene(Game game) {
        super(game);
        background = SpriteStore.get().getSprite("wall.png");
        try {
            URL url = this.getClass().getClassLoader().getResource("main/sprites/"+"wall"+".png");
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

        Player player = new Player(this, "rambo.png", 220, 250, 100, 1);
        entities.add(player);
        entities.add(new Bar(player));
        entities.add(new AmmoBar(player));

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


        try {

            // move each entity
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.move(delta);
            }

            // draw all entities
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.draw(g);
            }

            // ! adding stuff
//            for (Entity e : entities) {
//                e.move(delta);
//            }
//
//            // draw all entities
//            for (Entity e : entities) {
//                e.draw(g);
//            }
        } catch (Exception e) {
            System.out.println(e);
        }


        // TODO Optimize collisions

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
            keysDown.add(e.getKeyChar());
        } // keyPressed

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(e.getKeyChar());
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
}