package com.louiswebbgames.asteroidalprojection;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.Projection;
import com.louiswebbgames.asteroidalprojection.ui.HealthRenderer;
import com.louiswebbgames.asteroidalprojection.ui.ScoreRenderer;

public class PlayScreen extends GameScreen {

    FitViewport uiViewport;
    Stage uiStage;

    @Override
    public void init() {
        float projectionScale = Projection.getProjectionScale();
        viewport = new FitViewport(projectionScale * 2, projectionScale * 2);
        uiViewport = new FitViewport(800, 800);
        stage = new PlayStage(viewport);
        uiStage = new Stage(uiViewport);
        uiStage.addActor(new ScoreRenderer((PlayStage) stage));
        uiStage.addActor(new HealthRenderer(((PlayStage)stage).getPlayer()));
    }

    @Override
    protected void update(float delta) {
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
    }
}
