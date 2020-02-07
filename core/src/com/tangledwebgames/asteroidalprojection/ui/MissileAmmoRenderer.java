package com.tangledwebgames.asteroidalprojection.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Player;
import com.tangledwebgames.asteroidalprojection.utility.Assets;

public class MissileAmmoRenderer extends Table {

    Player player;
    Label ammoLabel;

    public MissileAmmoRenderer(Player player) {
        super();
        this.player = player;
        setSize(200, 200);
        pad(0, UiConstants.PADDING, UiConstants.PADDING, 0);
        align(Align.bottomLeft);
        //add(new Label("Missiles", UiConstants.basicLabelStyle)).colspan(2);
        //row();
        add(new Image(Assets.instance.missileAmmoDrawable, Scaling.stretch))
                .right().size(UiConstants.ICON_SIZE);
        ammoLabel = new Label("", UiConstants.basicLabelStyle);
        add(ammoLabel).left();
    }

    @Override
    public void act(float delta) {
        setPosition(0, 0, Align.bottomLeft);
        ammoLabel.setText(" x " + player.getMissileAmmo());
        super.act(delta);
    }
}
