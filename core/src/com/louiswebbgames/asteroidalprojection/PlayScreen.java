package com.louiswebbgames.asteroidalprojection;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.Projection;

public class PlayScreen extends GameScreen {

    @Override
    public void init() {
        float projectionScale = Projection.getProjectionScale();
        viewport = new FitViewport(projectionScale * 2, projectionScale * 2);
        stage = new PlayStage(viewport);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.getCamera().position.x = 0;
        viewport.getCamera().position.y = 0;
    }
}
