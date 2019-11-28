package framework.gl;

import framework.Game;

public abstract class Screen {

    protected final Game game;
    protected final Graphics graphics;


    public Screen(Game game) {
        this.game = game;
        graphics = game.getGraphics();
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
