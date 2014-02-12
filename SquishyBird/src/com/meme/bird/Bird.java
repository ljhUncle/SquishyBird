package com.meme.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by meme on 14-2-12.
 */
public class Bird extends Actor {
    PlayScreen screen;

    public float x;
    public float y;
    public float width;
    public float height;
    public float stateTime;

    public Animation animation;

    public boolean isBird;

    public Bird(PlayScreen screen) {
        this.screen = screen;
        this.stateTime = 0.0f;
        this.isBird = true;
        this.show();
    }

    public void show() {
        TextureRegion[] frames = new TextureRegion[3];
        frames[0]  = new TextureRegion(new Texture(Gdx.files.internal("data/bird1.png")));
        frames[1]  = new TextureRegion(new Texture(Gdx.files.internal("data/bird2.png")));
        frames[2]  = new TextureRegion(new Texture(Gdx.files.internal("data/bird3.png")));

        this.x = -frames[0].getRegionWidth();
        this.y = (screen.game.screenHeight + screen.bottomHeight - frames[0].getRegionHeight()) / 2.0f;
        this.width = frames[0].getRegionWidth();
        this.height = frames[0].getRegionHeight();

        animation = new Animation(0.1f, frames);
        animation.setPlayMode(animation.LOOP_PINGPONG);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion frame = animation.getKeyFrame(stateTime, true);

        if (isBird) {
            x += 2;
        } else {
            x = (screen.game.screenWidth - frame.getRegionWidth()) / 2.0f;
            y = (screen.game.screenHeight + screen.bottomHeight) / 2.0f  - frame.getRegionHeight();
        }
        batch.draw(frame, x, y);

        if (x > screen.game.screenWidth) {
            x = -frame.getRegionWidth();
        }
    }

    public void squish() {
        this.isBird = false;
        Texture texture = new Texture(Gdx.files.internal("data/blood.png"));
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight() / 6);
        TextureRegion[] frames= new TextureRegion[30];
        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        animation = new Animation(0.005f,frames);
        animation.setPlayMode(animation.NORMAL);
    }
}
