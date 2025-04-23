package com.TheWayofKings.util;

import com.TheWayofKings.game.MainGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {

    private final Game game;
    private final MainGame mainGame;

    public GameScreen(Game game) {
        this.game = game;
        this.mainGame = new MainGame();
        this.mainGame.setGame(game);
        this.mainGame.setGameScreen(this);
        this.mainGame.create();// todo se inicializa dentro de MainGame

    }

    public MainGame getMainGame() {
        return mainGame;
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        mainGame.render(); // delega directamente a MainGame
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        mainGame.dispose();
    }
}
