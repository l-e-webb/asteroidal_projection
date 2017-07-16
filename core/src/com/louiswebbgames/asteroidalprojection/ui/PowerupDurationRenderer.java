package com.louiswebbgames.asteroidalprojection.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Player;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class PowerupDurationRenderer extends Table {

    Player player;
    Label tripleLaserLabel;
    Label piercingLaserLabel;

    public PowerupDurationRenderer(Player player) {
        super();
        this.player = player;
        setSize(200, 200);
        pad(0, 0, UiConstants.PADDING, UiConstants.PADDING);
        align(Align.bottomRight);
        tripleLaserLabel = new Label("", UiConstants.basicLabelStyle);
        piercingLaserLabel = new Label("", UiConstants.basicLabelStyle);
        add(new Image(Assets.instance.tripleLaserDrawable, Scaling.stretch))
                .right().size(UiConstants.ICON_SIZE);
        add(tripleLaserLabel).left().padLeft(UiConstants.PADDING);
        row();
        add(new Image(Assets.instance.piercingLaserDrawable, Scaling.stretch))
                .right().size(UiConstants.ICON_SIZE);
        add(piercingLaserLabel).left().padLeft(UiConstants.PADDING);
    }

    @Override
    public void act(float delta) {
        setPosition(getStage().getViewport().getWorldWidth(), 0, Align.bottomRight);
        int tripleLaserDuration = (int) player.getTripleLaserDuration();
        int piercingLaserDuration = (int) player.getPiercingLaserDuration();
        String tripleLaserStr;
        String piercingLaserStr;
        if (tripleLaserDuration > 0) {
            tripleLaserStr = tripleLaserDuration >= 10 ? tripleLaserDuration + "s" : "0" + tripleLaserDuration + "s";
        } else {
            tripleLaserStr = UiText.OFF;
        }
        if (piercingLaserDuration > 0) {
            piercingLaserStr = piercingLaserDuration >= 10 ? piercingLaserDuration + "s" : "0" + piercingLaserDuration + "s";
        } else {
            piercingLaserStr = UiText.OFF;
        }
        tripleLaserLabel.setText(tripleLaserStr);
        piercingLaserLabel.setText(piercingLaserStr);
    }
}
