package main;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class SceneManager {
    private Scene currentScene;
    private final Game game;

    SceneManager(Game game) {
        this.game = game;
    }

    public void setScene(Scene scene) {
        for (KeyListener k : game.getKeyListeners()) {
            game.removeKeyListener(k);
        }

        for (MouseListener k : game.getMouseListeners()) {
            game.removeMouseListener(k);
        }

        if (currentScene != null) {
            // TODO handle exiting the current scene
        }

        currentScene = scene;
        currentScene.init();
    }

    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }
}