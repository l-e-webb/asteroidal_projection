package com.louiswebbgames.hyperbocalypse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.louiswebbgames.hyperbocalypse.gameplay.PlayStage;

public abstract class GameScreen implements Screen {

    Stage stage;
    Viewport viewport;

    @Override
    public void show() {
        init();
        Gdx.input.setInputProcessor(stage);
    }

    public abstract void init();

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        stage.draw();
    }

    protected void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {

    }
}
