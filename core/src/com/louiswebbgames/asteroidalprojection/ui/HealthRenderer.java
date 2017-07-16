package com.louiswebbgames.asteroidalprojection.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Player;
import com.louiswebbgames.asteroidalprojection.utility.Log;

public class HealthRenderer extends Label {

    protected Player player;

    public HealthRenderer(Player player) {
        super("", UiConstants.basicLabelStyle);
        this.player = player;
        setSize(175, 100);
        setAlignment(Align.center);
    }

    @Override
    public void act(float delta) {
        setPosition(0, getStage().getViewport().getWorldHeight(), Align.topLeft);
        setText(UiText.HEALTH + "\n" + player.currentHealth());
    }

}
