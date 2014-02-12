package com.meme.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by meme on 14-2-12.
 */
public class PlayScreen implements Screen, GestureListener {
    public int bottomHeight;
    public int groundHeight;
    public int cityWidth;
    public int pipeDelta;
    public float pipeUpX;
    public float pipeUpY;
    public float pipeDownX;
    public float pipeDownY;

    public SquishyBird game;

    public Stage stage;
    public Image pipeUp;
    public Image pipeDown;
    public Bird bird;

    public Texture bottom;
    public Texture ground;
    public Texture city;
    public Texture trees;
    public SpriteBatch batch;

    public Sound slide;
    public Sound clang;

    public PlayScreen(SquishyBird game) {
        this.game = game;
    }

    @Override
    public void show() {
        bottomHeight = game.screenHeight / 8;
        groundHeight = game.screenWidth / 14;
        cityWidth = game.screenWidth / 3;
        pipeDelta = game.screenHeight / 10;

        stage = new Stage();
        this.createPipe(true);
        stage.addActor(pipeUp);
        this.createPipe(false);
        stage.addActor(pipeDown);
        bird = new Bird(this);
        stage.addActor(bird);

        Pixmap pixmap;
        pixmap = new Pixmap(game.screenWidth, bottomHeight, Pixmap.Format.RGBA8888);
        pixmap.setColor(221/255.0f, 216/255.0f, 148/255.0f, 1);
        pixmap.fill();

        bottom = new Texture(pixmap);
        ground = new Texture(Gdx.files.internal("data/ground.png"));
        city = new Texture(Gdx.files.internal("data/city.png"));
        trees = new Texture(Gdx.files.internal("data/trees.png"));
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(new GestureDetector(this));

        slide = Gdx.audio.newSound(Gdx.files.internal("sound/slide.wav"));
        clang = Gdx.audio.newSound(Gdx.files.internal("sound/clang.wav"));
    }

    @Override
    public  void render(float delta) {

        Gdx.gl.glClearColor(113 / 255.0f, 197 / 255.0f, 207 / 255.0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (int i = 0; i <= game.screenWidth; i += cityWidth) {
            batch.draw(city, i, bottomHeight + groundHeight + 20, cityWidth, city.getHeight() + 30);
            batch.draw(trees, i, bottomHeight + groundHeight, cityWidth, trees.getHeight() + 30);
        }
        batch.end();

        stage.act();
        stage.draw();

        batch.begin();
        batch.draw(bottom, 0, 0);
        for (int i = 0; i <= game.screenWidth; i += groundHeight) {
            batch.draw(ground, i, bottomHeight, groundHeight, groundHeight);
        }
        batch.end();

        this.update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public  void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public void update() {
        if (pipeDown.getTop() >= pipeUp.getY()) {
            clang.play();
//            Gdx.input.vibrate(200);
            return;
        }
        if (pipeDown.getX() <= bird.x + bird.width * 2.0f / 3.0f && pipeDown.getRight() >= bird.x + bird.width / 3.0f) {
            if (pipeDown.getTop() >= bird.y) {
                if (pipeUp.getY() <= bird.y + bird.height) {
                    bird.squish();
                } else {
                    bird.y = pipeDown.getTop();
                }
            }
        }
    }

    public void createPipe(boolean isUp) {
        if (isUp) {
            Texture texture = new Texture(Gdx.files.internal("data/pipeup.png"));
            pipeUp = new Image(texture);
            pipeUp.setSize(180.0f,pipeUp.getHeight());
            pipeUpX = (game.screenWidth - pipeUp.getWidth()) / 2.0f;
            pipeUpY = (game.screenHeight + bottomHeight) / 2.0f + pipeDelta;
            pipeUp.setPosition(pipeUpX, pipeUpY);
        } else {
            Texture texture = new Texture(Gdx.files.internal("data/pipedown.png"));
            pipeDown = new Image(texture);
            pipeDown.setSize(180.0f,pipeDown.getHeight());
            pipeDownX = (game.screenWidth - pipeDown.getWidth()) / 2.0f;
            pipeDownY = (game.screenHeight + bottomHeight) / 2.0f - pipeDelta - pipeDown.getHeight();
            pipeDown.setPosition(pipeDownX, pipeDownY);
        }
    }

    // Gesture

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public  boolean tap(float x, float y, int count, int button) {

        slide.play();

        MoveToAction upMoveDown = Actions.moveTo(pipeUpX, pipeUpY - pipeDelta, 0.1f);
        MoveToAction upMoveUp = Actions.moveTo(pipeUpX, pipeUpY, 0.1f);
        SequenceAction upAlpha = Actions.sequence(upMoveDown, upMoveUp);

        MoveToAction downMoveUp = Actions.moveTo(pipeDownX, pipeDownY + pipeDelta, 0.1f);
        MoveToAction downMoveDown = Actions.moveTo(pipeDownX, pipeDownY, 0.1f);
        SequenceAction downAlpha = Actions.sequence(downMoveUp, downMoveDown);

        pipeUp.addAction(upAlpha);
        pipeDown.addAction(downAlpha);

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
