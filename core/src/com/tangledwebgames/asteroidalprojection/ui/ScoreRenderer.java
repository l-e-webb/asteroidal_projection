package com.tangledwebgames.asteroidalprojection.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;

public class ScoreRenderer extends Label {

    protected PlayStage playStage;

    public ScoreRenderer(PlayStage playStage) {
        super("", UiConstants.basicLabelStyle);
        setSize(150, 100);
        this.playStage = playStage;
        setAlignment(Align.center);
    }

    @Override
    public void act(float delta) {
        setText(UiText.SCORE + "\n" + playStage.getScore());
        setPosition(
                getStage().getViewport().getWorldWidth(),
                getStage().getViewport().getWorldHeight(),
                Align.topRight
        );
    }
}
