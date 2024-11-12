package main;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Scene {
    protected Game game;

    Scene(Game game) {
        this.game = game;


    }

    protected void handleMouseEvent(MouseEvent e) {}
    protected void handleKeyPressed(KeyEvent e) {}
    protected void handleKeyReleased(KeyEvent e) {}
    protected void handleKeyTyped(KeyEvent e) {}


    public abstract void init();
    public abstract void update();



}