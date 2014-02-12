package com.meme.bird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class SquishyBird extends Game {
    public int screenWidth;
    public int screenHeight;

    PlayScreen playScreen;

    @Override
    public  void create() {

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        playScreen = new PlayScreen(this);
        this.setScreen(playScreen);
    }
}
