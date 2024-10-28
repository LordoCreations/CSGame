package main;

public abstract class Scene {
    protected Game game;

    Scene(Game game) {
        this.game = game;
    }

    public abstract void init();
    public abstract void update();
}