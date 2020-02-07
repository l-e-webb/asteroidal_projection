package com.tangledwebgames.asteroidalprojection.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Player;
import com.tangledwebgames.asteroidalprojection.utility.Assets;

public class HealthRenderer extends Table {

    protected Player player;
    protected Label label;
    protected Image[] images;

    public HealthRenderer(Player player) {
        images = new Image[GameplayConstants.PLAYER_MAX_HEALTH];
        label = new Label("", UiConstants.basicLabelStyle);
        label.setAlignment(Align.center);
        this.player = player;
        setSize(225, 0);
        for (int i = 0; i < images.length; i++) {
            images[i] = new Image(Assets.instance.healthBar);
        }
        add(label).center().colspan(5).expandX();
        row();
        for (int i = 0; i < images.length; i++) {
            add(images[i]).width(UiConstants.HEALTH_BAR_WIDTH)
                    .height(UiConstants.HEALTH_BAR_HEIGHTS[i])
                    .top().expandX();
        }
        setHeight(getPrefHeight());
        label.setText(UiText.HEALTH);
    }

    @Override
    public void act(float delta) {
        setPosition(0, getStage().getViewport().getWorldHeight(), Align.topLeft);
        for (int i = 0; i < images.length; i++) {
            if (GameplayConstants.PLAYER_MAX_HEALTH - i <= player.currentHealth()) {
                images[i].setDrawable(Assets.instance.healthBar);
            } else {
                images[i].setDrawable(Assets.instance.squareButtonDark);
            }
        }
    }

}
