package com.TheWayofKings.game;

import com.TheWayofKings.screens.MainMenuScreen;
import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this)); // usa el constructor básico
    }
}


