package com.louiswebbgames.asteroidalprojection.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.Projection;

public class ScoreRenderer extends Label {

    protected PlayStage playStage;

    public ScoreRenderer(PlayStage playStage) {
        super("", UiConstants.basicLabelStyle);
        setSize(175, 50);
        this.playStage = playStage;
        setAlignment(Align.center);
        setDebug(true);
    }

    @Override
    public void act(float delta) {
        setText("Score: " + (playStage).getScore());
        setPosition(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight(), Align.topRight);
    }
}
