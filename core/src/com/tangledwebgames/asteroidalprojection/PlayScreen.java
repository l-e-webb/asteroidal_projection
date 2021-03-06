package com.tangledwebgames.asteroidalprojection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.geometry.Projection;
import com.tangledwebgames.asteroidalprojection.ui.*;
import com.tangledwebgames.asteroidalprojection.utility.Constants;
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;

public class PlayScreen extends GameScreen {

    FitViewport uiViewport;
    PlayStage playStage;
    Stage uiStage;
    PauseMenu pauseMenu;
    GameOverMenu gameOverMenu;
    MainMenu mainMenu;

    public ScreenState state;

    @Override
    public void init() {
        viewport = new FitViewport( 2, 2);
        uiViewport = new FitViewport(Constants.UI_VIEWPORT_WIDTH, Constants.UI_VIEWPORT_HEIGHT);
        Projection.init();
        playStage = new PlayStage(viewport);
        stage = playStage;
        initCoreUi();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playStage);
        inputMultiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        playStage.initGame(true);
        state = ScreenState.TITLE;
        mainMenu.activate();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SoundManager.stopMusic();
    }

    public void initGame() {
        if (state == ScreenState.TITLE) {
            initGameUi();
        }
        playStage.initGame(false);
        state = ScreenState.PLAY;
        mainMenu.deactivate();
        SoundManager.playMusic();
    }

    @Override
    protected void update(float delta) {
        if (state == ScreenState.PLAY &&
                (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.P))) {
            togglePause();
        }
        if (playStage.isGameOver() && state != ScreenState.GAME_OVER) {
            gameOverMenu.activate();
            state = ScreenState.GAME_OVER;
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

    public void initCoreUi() {
        uiStage = new Stage(uiViewport);
        pauseMenu = new PauseMenu(this, true);
        uiStage.addActor(pauseMenu);
        gameOverMenu = new GameOverMenu(this);
        uiStage.addActor(gameOverMenu);
        mainMenu = new MainMenu(this);
        uiStage.addActor(mainMenu);
    }

    public void initGameUi() {
        uiStage.addActor(new ScoreRenderer(playStage));
        uiStage.addActor(new HealthRenderer(playStage.getPlayer()));
        uiStage.addActor(new MissileAmmoRenderer(playStage.getPlayer()));
        uiStage.addActor(new PowerupDurationRenderer(playStage.getPlayer()));
        pauseMenu.remove();
        pauseMenu = new PauseMenu(this, false);
        uiStage.addActor(pauseMenu);
    }

    public void togglePause() {
        playStage.togglePaused();
        boolean paused = playStage.isPaused();
        pauseMenu.setPaused(paused);
        if (!paused && state == ScreenState.TITLE) {
            mainMenu.activate();
        }
    }

    public enum ScreenState {
        TITLE,
        PLAY,
        GAME_OVER
    }
}
