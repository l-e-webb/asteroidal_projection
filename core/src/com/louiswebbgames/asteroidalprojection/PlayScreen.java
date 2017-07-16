package com.louiswebbgames.asteroidalprojection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.Projection;
import com.louiswebbgames.asteroidalprojection.ui.*;
import com.louiswebbgames.asteroidalprojection.utility.Constants;

public class PlayScreen extends GameScreen {

    FitViewport uiViewport;
    PlayStage playStage;
    Stage uiStage;
    PauseMenu pauseMenu;
    GameOverMenu gameOverMenu;

    @Override
    public void init() {
        float projectionScale = Projection.getProjectionScale();
        viewport = new FitViewport(projectionScale * 2, projectionScale * 2);
        uiViewport = new FitViewport(Constants.UI_VIEWPORT_WIDTH, Constants.UI_VIEWPORT_HEIGHT);
        playStage = new PlayStage(viewport);
        stage = playStage;
        initUi();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playStage);
        inputMultiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        playStage.initGame();
    }

    @Override
    protected void update(float delta) {
        if (!playStage.gameOver() &&
                (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.P))) {
            playStage.togglePaused();
            pauseMenu.setPaused(playStage.isPaused());
        }
        if (playStage.gameOver() && !gameOverMenu.isVisible()) {
            gameOverMenu.activate();
        }
        super.update(delta);
        uiStage.act(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.getCamera().position.x = 0;
        viewport.getCamera().position.y = 0;
        uiViewport.update(width, height);
    }

    public void initUi() {
        uiStage = new Stage(uiViewport);
        uiStage.addActor(new ScoreRenderer(playStage));
        uiStage.addActor(new HealthRenderer(playStage.getPlayer()));
        uiStage.addActor(new MissileAmmoRenderer(playStage.getPlayer()));
        uiStage.addActor(new PowerupDurationRenderer(playStage.getPlayer()));
        pauseMenu = new PauseMenu(playStage);
        pauseMenu.setVisible(false);
        uiStage.addActor(pauseMenu);
        gameOverMenu = new GameOverMenu(playStage);
        uiStage.addActor(gameOverMenu);
    }
}
